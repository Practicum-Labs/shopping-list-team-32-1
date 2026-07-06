<#
.SYNOPSIS
    Собирает DEBUG APK и публикует его в GitHub Release (черновая раздача для демо).

.DESCRIPTION
    Шаги, которые выполняет скрипт:
      1. Проверяет, что установлен и авторизован gh CLI.
      2. Собирает debug APK: gradlew :app:assembleDebug
      3. Копирует app-debug.apk -> shopping-list-<Tag>.apk
      4. Создаёт (или обновляет) GitHub Release с этим тегом и прикрепляет APK.

    Запускать можно из любой папки — скрипт сам переходит в корень репозитория.

.PARAMETER Tag
    ОБЯЗАТЕЛЬНЫЙ. Тег релиза, например v1.0.0. Если тега ещё нет в репозитории,
    gh создаст его из текущего HEAD (см. -Target).

.PARAMETER Title
    Заголовок релиза. По умолчанию равен Tag.

.PARAMETER Notes
    Текст описания релиза (строкой). Взаимоисключим с -NotesFile.

.PARAMETER NotesFile
    Путь к markdown-файлу с описанием релиза. Взаимоисключим с -Notes.

.PARAMETER Target
    Ветка или SHA, на который будет указывать создаваемый тег. По умолчанию develop.

.PARAMETER Draft
    Создать релиз как черновик (не опубликован, виден только вам).

.PARAMETER Prerelease
    Пометить релиз как предрелиз.

.PARAMETER SkipBuild
    Не пересобирать APK (использовать уже собранный app-debug.apk).

.EXAMPLE
    .\scripts\draft.ps1 -Tag v1.0.0

.EXAMPLE
    .\scripts\draft.ps1 -Tag v1.0.0 -Title "MVP" -NotesFile .\docs\release-notes.md -Draft

.EXAMPLE
    .\scripts\draft.ps1 -Tag v1.0.1 -Notes "Багфиксы" -Target main
#>

[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string]$Tag,

    [string]$Title,

    [string]$Notes,

    [string]$NotesFile,

    [string]$Target = "develop",

    [switch]$Draft,

    [switch]$Prerelease,

    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"

# Корень репозитория = папка на уровень выше этого скрипта.
$RepoRoot = Split-Path -Parent $PSScriptRoot
Set-Location $RepoRoot
Write-Host "Repo root: $RepoRoot" -ForegroundColor Cyan

# --- Проверки ---------------------------------------------------------------
if ($Notes -and $NotesFile) {
    throw "Укажите либо -Notes, либо -NotesFile, но не оба сразу."
}
if ($NotesFile -and -not (Test-Path $NotesFile)) {
    throw "Файл описания не найден: $NotesFile"
}
if (-not (Get-Command gh -ErrorAction SilentlyContinue)) {
    throw "gh CLI не установлен. Установите: https://cli.github.com/"
}

# gh auth status пишет в stderr даже при успехе — проверяем по коду возврата.
gh auth status *> $null
if ($LASTEXITCODE -ne 0) {
    throw "gh не авторизован. Выполните: gh auth login"
}

# --- Сборка -----------------------------------------------------------------
$ApkPath = Join-Path $RepoRoot "app\build\outputs\apk\debug\app-debug.apk"

if (-not $SkipBuild) {
    Write-Host "Собираю debug APK..." -ForegroundColor Cyan
    & "$RepoRoot\gradlew.bat" ":app:assembleDebug" --stacktrace
    if ($LASTEXITCODE -ne 0) {
        throw "Сборка провалилась (gradlew exit $LASTEXITCODE)."
    }
}

if (-not (Test-Path $ApkPath)) {
    throw "APK не найден: $ApkPath (собери без -SkipBuild)."
}

# Понятное имя артефакта с версией.
$AssetName = "shopping-list-$Tag.apk"
$AssetPath = Join-Path $env:TEMP $AssetName
Copy-Item $ApkPath $AssetPath -Force
Write-Host "APK готов: $AssetPath" -ForegroundColor Green

# --- Формируем аргументы gh release create ----------------------------------
if (-not $Title) { $Title = $Tag }

$ghArgs = @("release", "create", $Tag, $AssetPath, "--title", $Title, "--target", $Target)

if ($NotesFile)   { $ghArgs += @("--notes-file", (Resolve-Path $NotesFile).Path) }
elseif ($Notes)   { $ghArgs += @("--notes", $Notes) }
else              { $ghArgs += "--generate-notes" }   # авто-описание из коммитов/PR

if ($Draft)       { $ghArgs += "--draft" }
if ($Prerelease)  { $ghArgs += "--prerelease" }

Write-Host "Создаю релиз $Tag ..." -ForegroundColor Cyan
& gh @ghArgs
if ($LASTEXITCODE -ne 0) {
    throw "gh release create завершился с ошибкой (exit $LASTEXITCODE)."
}

Write-Host "Готово. Релиз $Tag опубликован." -ForegroundColor Green

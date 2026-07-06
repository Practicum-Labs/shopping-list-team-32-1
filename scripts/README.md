# Публикация сборки в GitHub Release

Скрипты собирают **debug APK** (`app-debug.apk`, подписан debug-ключом, ставится на телефон сразу) и создают GitHub Release с прикреплённым APK.

- `release.ps1` — Windows / PowerShell (основной для этого проекта)
- `release.sh` — Linux / macOS / CI (bash)

> ⚠️ Это debug-сборка: подходит для демо и раздачи внутри команды, **не** для Google Play. Для «настоящего» релиза нужен release-APK с подписью (см. раздел ниже).

## Предпосылки (сделать один раз)

1. **JDK 17** и Android SDK (обычно уже стоят вместе с Android Studio).
2. **GitHub CLI** — https://cli.github.com/
3. Авторизация в gh:
   ```
   gh auth login
   ```
4. Запускать из корня репозитория (или из любой папки — скрипты сами переходят в корень).

## Запуск

### PowerShell
```powershell
# Минимально: собрать и опубликовать релиз v1.0.0 (описание сгенерируется из коммитов)
.\scripts\release.ps1 -Tag v1.0.0

# С заголовком и описанием из файла, как черновик (draft) для проверки перед публикацией
.\scripts\release.ps1 -Tag v1.0.0 -Title "MVP" -NotesFile .\docs\release-notes.md -Draft

# Тег от ветки main, короткое описание строкой
.\scripts\release.ps1 -Tag v1.0.1 -Notes "Багфиксы" -Target main

# Не пересобирать (использовать уже собранный APK)
.\scripts\release.ps1 -Tag v1.0.1 -SkipBuild
```

Справка по параметрам: `Get-Help .\scripts\release.ps1 -Detailed`

### bash (Linux/macOS)
```bash
chmod +x scripts/release.sh    # один раз
./scripts/release.sh -t v1.0.0
./scripts/release.sh -t v1.0.0 -T "MVP" -f docs/release-notes.md --draft
```

## Параметры

| PowerShell | bash | Обязательный | Назначение |
|---|---|---|---|
| `-Tag` | `-t, --tag` | да | Тег релиза, напр. `v1.0.0`. Если тега нет — создастся из `-Target`. |
| `-Title` | `-T, --title` | нет | Заголовок релиза (по умолчанию = тег). |
| `-Notes` | `-n, --notes` | нет | Описание строкой. |
| `-NotesFile` | `-f, --notes-file` | нет | Описание из markdown-файла. |
| `-Target` | `--target` | нет | Ветка/SHA для тега (по умолчанию `develop`). |
| `-Draft` | `--draft` | нет | Черновик (не опубликован публично). |
| `-Prerelease` | `--prerelease` | нет | Пометить как предрелиз. |
| `-SkipBuild` | `--skip-build` | нет | Не пересобирать APK. |

Если не задать ни `-Notes`, ни `-NotesFile` — описание сгенерируется автоматически из коммитов/PR (`--generate-notes`).

## Что делает скрипт по шагам

1. Проверяет, что `gh` установлен и авторизован.
2. `gradlew :app:assembleDebug` → `app/build/outputs/apk/debug/app-debug.apk`.
3. Копирует APK во временную папку под именем `shopping-list-<Tag>.apk` (чтобы в релизе был понятный файл с версией).
4. `gh release create <Tag> <apk> ...` — создаёт релиз и прикрепляет APK.

## Совет: сначала черновик

Прогоните с `-Draft` / `--draft` — релиз создастся скрытым, проверьте описание и что APK прикрепился, затем нажмите **Publish** на странице релиза в GitHub (или удалите черновик и перезапустите без флага).

---

## Позже: release-APK с подписью (не сейчас)

Чтобы выкладывать подписанный release-APK вместо debug, нужно:

1. Сгенерировать keystore (один раз):
   ```
   keytool -genkeypair -v -keystore release.keystore -alias shopping-list \
     -keyalg RSA -keysize 2048 -validity 10000
   ```
2. Добавить `signingConfigs` + `signingConfig` в блок `release` в [../app/build.gradle.kts](../app/build.gradle.kts), читая пароли из `local.properties`/переменных окружения (keystore и пароли **не коммитить**).
3. В скриптах поменять таск на `:app:assembleRelease` и путь на `.../apk/release/app-release.apk`.

Когда дойдём до этого — скажите, донастрою.

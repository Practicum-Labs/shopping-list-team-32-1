#!/usr/bin/env bash
#
# Собирает debug APK и публикует его в GitHub Release (bash-вариант для Linux/macOS/CI).
#
# Использование:
#   scripts/release.sh -t v1.0.0 [опции]
#
# Обязательные:
#   -t, --tag <tag>          Тег релиза, напр. v1.0.0
#
# Необязательные:
#   -T, --title <str>        Заголовок релиза (по умолчанию = tag)
#   -n, --notes <str>        Текст описания (взаимоисключимо с --notes-file)
#   -f, --notes-file <path>  Markdown-файл с описанием
#       --target <ref>       Ветка/SHA для тега (по умолчанию develop)
#       --draft              Черновик
#       --prerelease         Предрелиз
#       --skip-build         Не пересобирать APK
#
# Примеры:
#   scripts/release.sh -t v1.0.0
#   scripts/release.sh -t v1.0.0 -T "MVP" -f docs/release-notes.md --draft
#
set -euo pipefail

TAG=""; TITLE=""; NOTES=""; NOTES_FILE=""; TARGET="develop"
DRAFT=false; PRERELEASE=false; SKIP_BUILD=false

while [[ $# -gt 0 ]]; do
  case "$1" in
    -t|--tag)        TAG="$2"; shift 2;;
    -T|--title)      TITLE="$2"; shift 2;;
    -n|--notes)      NOTES="$2"; shift 2;;
    -f|--notes-file) NOTES_FILE="$2"; shift 2;;
    --target)        TARGET="$2"; shift 2;;
    --draft)         DRAFT=true; shift;;
    --prerelease)    PRERELEASE=true; shift;;
    --skip-build)    SKIP_BUILD=true; shift;;
    *) echo "Неизвестный аргумент: $1" >&2; exit 1;;
  esac
done

[[ -n "$TAG" ]] || { echo "Ошибка: обязателен --tag" >&2; exit 1; }
[[ -z "$NOTES" || -z "$NOTES_FILE" ]] || { echo "Ошибка: укажите либо --notes, либо --notes-file" >&2; exit 1; }

# Корень репозитория = папка на уровень выше этого скрипта.
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"
echo "Repo root: $REPO_ROOT"

command -v gh >/dev/null 2>&1 || { echo "gh CLI не установлен: https://cli.github.com/" >&2; exit 1; }
gh auth status >/dev/null 2>&1 || { echo "gh не авторизован. Выполните: gh auth login" >&2; exit 1; }

APK_PATH="$REPO_ROOT/app/build/outputs/apk/debug/app-debug.apk"

if [[ "$SKIP_BUILD" == false ]]; then
  echo "Собираю debug APK..."
  ./gradlew :app:assembleDebug --stacktrace
fi

[[ -f "$APK_PATH" ]] || { echo "APK не найден: $APK_PATH (собери без --skip-build)" >&2; exit 1; }

ASSET="${TMPDIR:-/tmp}/shopping-list-${TAG}.apk"
cp -f "$APK_PATH" "$ASSET"
echo "APK готов: $ASSET"

[[ -n "$TITLE" ]] || TITLE="$TAG"

args=(release create "$TAG" "$ASSET" --title "$TITLE" --target "$TARGET")
if   [[ -n "$NOTES_FILE" ]]; then args+=(--notes-file "$NOTES_FILE")
elif [[ -n "$NOTES" ]];      then args+=(--notes "$NOTES")
else args+=(--generate-notes); fi
[[ "$DRAFT" == true ]]      && args+=(--draft)
[[ "$PRERELEASE" == true ]] && args+=(--prerelease)

echo "Создаю релиз $TAG ..."
gh "${args[@]}"
echo "Готово. Релиз $TAG опубликован."

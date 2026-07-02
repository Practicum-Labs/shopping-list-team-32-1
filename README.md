# Shopping List — Проектный месяц, 32 когорта Android

Командное Android-приложение для ведения списков покупок, разработанное в рамках Проектного месяца Яндекс Практикума.

---

## Стек технологий

| Слой | Выбор | Обоснование |
|---|---|---|
| **UI** | [Jetpack Compose](https://developer.android.com/develop/ui/compose) + Material 3 | Современный декларативный UI-фреймворк от Google — меньше кода, чем с XML, удобный Preview прямо в Android Studio |
| **Архитектура** | MVVM + Clean Architecture | Разделяет UI, бизнес-логику и данные — каждый слой можно менять и тестировать независимо |
| **БД** | [Room](https://developer.android.com/training/data-storage/room) | Официальная библиотека для SQLite от Google — проверяет SQL-запросы на этапе компиляции, не даёт сделать ошибку в рантайме |
| **DI** | [Koin](https://insert-koin.io) | Простой DI на Kotlin DSL, не требует кодогенерации — легко читается и быстро настраивается |
| **Навигация** | [Navigation Compose](https://developer.android.com/develop/ui/compose/navigation) | Официальное решение для навигации в Compose |

**Тип проекта:** Монолитный (single-module)

---

## Архитектура

Проект построен по принципам **Clean Architecture** с паттерном **MVVM** в слое представления.

```
app/src/main/java/com/practicum/shopping_list/
├── core/               # Общие ресурсы: БД (Room database), навигация, тема, утилиты
├── data/               # Слой данных
│   ├── local/
│   │   ├── dao/        # Room DAO-интерфейсы
│   │   └── entity/     # Room Entity-классы
│   ├── mapper/         # Преобразование Entity ↔ Domain Model
│   ├── repository/     # Реализации репозиториев
│   └── di/             # Koin-модуль: dataModule
├── domain/             # Бизнес-логика (чистый Kotlin, без Android-зависимостей)
│   ├── model/          # Domain-модели
│   ├── repository/     # Интерфейсы репозиториев
│   ├── usecase/        # Use case'ы
│   └── di/             # Koin-модуль: domainModule
└── presentation/       # UI-слой
    ├── ui/
    │   ├── common/     # Переиспользуемые Composable-компоненты
    │   ├── lists/      # Экран списков покупок
    │   ├── editor/     # Экран товаров внутри списка
    │   └── root/       # RootActivity — точка входа
    └── di/             # Koin-модуль: viewModelModule
```

**Поток данных:** `UI → ViewModel → UseCase → Repository interface → RepositoryImpl → Room DAO`

Три Koin-модуля (`dataModule`, `domainModule`, `viewModelModule`) инициализируются в `App.kt`.

---

## Правила командной разработки

### Git-flow

- `main` — релизная ветка; прямые коммиты запрещены; force push запрещён; удаление запрещено
- `develop` — интеграционная ветка; все фичи вливаются через Pull Request
- Фичевые ветки — именование: `feature/<короткое-описание>`
- Релизная ветка — `release-1.0.0`, ответвляется от `develop`, PR в `main`

### Правила Pull Request

- Минимум **1 approving review** для merge в `main`
- Stale review сбрасывается после новых коммитов
- Перед merge все conversations должны быть закрыты/resolved

### Разделение задач

Каждый разработчик реализует свой блок «насквозь» — от Room entity до Compose UI.

---

## Команда и задачи

| Участник | Реализованные задачи |
|---|---|
| **Pavel Michka** | Инициализация проекта, подключение библиотек, настройка Clean Architecture пакетов, RootActivity, .gitignore |
| **Sergey Ivanov** | _в процессе_ |
| **Sergey Gnedovsky** | _в процессе_ |
| **Daniil Kvasnikov** | _в процессе_ |

> Раздел будет дополняться по мере завершения задач.

---

## Видеодемонстрация

> Ссылка на видео-демо будет добавлена после завершения разработки MVP.
>
> _Разместите видео в Google Drive / Яндекс Диске с открытым доступом и вставьте ссылку сюда._

---

## Требования к сборке

- **minSdk:** 28 (Android 9.0 Pie)
- **targetSdk:** 36
- Релизная сборка: ProGuard + `minifyEnabled = true` + подпись приложения

Подробное ТЗ и критерии оценки: [docs/requirements/technical-requirements.md](docs/requirements/technical-requirements.md)

# Shopping List - Проектный месяц, 32 когорта, команда №1

Командное Android-приложение для ведения списков покупок, разработанное в рамках Проектного месяца Яндекс Практикума.

---

## Стек технологий

| Слой | Выбор | Обоснование                                                                                                                                                                |
|---|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Язык** | Kotlin | Основной язык Android-разработки: хорошо работает с корутинами, Flow, Compose и современными Jetpack-библиотеками                                                          |
| **UI** | [Jetpack Compose](https://developer.android.com/develop/ui/compose) + Material 3 | Декларативный UI упрощает разработку экранов, состояний и превью. Material 3 дает готовые компоненты и единый визуальный стиль                                             |
| **Архитектура** | MVVM + Clean Architecture | MVVM выбран как понятный команде паттерн для UI-слоя. Clean Architecture разделяет presentation, domain и data, поэтому бизнес-логика не зависит от UI и источников данных |
| **Асинхронность** | Kotlin Coroutines + Flow | Используются для работы с БД, сетью и реактивными состояниями экранов                                                                                                      |
| **БД** | [Room](https://developer.android.com/training/data-storage/room) | Официальная обертка над SQLite: проверяет SQL на этапе компиляции, удобно работает с Flow и миграциями                                                                     |
| **Локальные настройки** | DataStore Preferences | Используется для хранения небольших пользовательских настроек и auth-сессии, где не нужна таблица Room                                                                     |
| **DI** | [Koin](https://insert-koin.io) | Легкий DI-фреймворк с Kotlin DSL. Не требует аннотационной кодогенерации                                                |
| **Network** | [Retrofit](https://square.github.io/retrofit/) + Gson + OkHttp | Retrofit выбран для типобезопасного описания API, Gson для JSON, OkHttp для сетевого клиента и interceptors                                                                |
| **Навигация** | [Navigation Compose](https://developer.android.com/develop/ui/compose/navigation) | Официальное решение для навигации между Compose-экранами                                                                                                                   |
| **Статический анализ** | Detekt + Reviewdog | Detekt контролирует стиль и сложность Kotlin-кода, Reviewdog показывает замечания прямо в Pull Request                                                                     |
| **Unit-тесты и покрытие** | JUnit, kotlinx-coroutines-test, Turbine, Kover | Тесты проверяют мапперы, use case и ViewModel. Kover формирует отчет по покрытию                                                                                           |
| **Release-сборка** | R8/ProGuard + signingConfig + GitHub Actions | Для release включены minify/shrinkResources, подпись через upload key и ручная signed release-сборка в GitHub Actions                                                      |

**Тип проекта:** Монолитный (single-module)

---

## Архитектура

Проект построен по принципам **Clean Architecture** с паттерном **MVVM** в слое представления.
Приложение разделено на три основных слоя: `presentation`, `domain`, `data`.

```
app/src/main/java/com/practicum/shoppinglist/
├── App.kt              # Application, запуск Koin
├── data/               # Слой данных
│   ├── local/
│   │   ├── dao/        # Room DAO
│   │   ├── database/   # Room database и миграции
│   │   ├── datasource/ # DataStore и локальные источники данных
│   │   └── entity/     # Room Entity
│   ├── remote/         # Retrofit API, DTO, сетевые настройки
│   ├── mapper/         # Преобразование DTO/Entity ↔ Domain Model
│   ├── repository/     # Реализации репозиториев
│   └── di/             # Koin-модуль: dataModule
├── domain/             # Бизнес-логика (чистый Kotlin, без Android-зависимостей)
│   ├── model/          # Domain-модели
│   ├── repository/     # Интерфейсы репозиториев
│   ├── usecase/        # Use case'ы
│   └── di/             # Koin-модуль: domainModule
└── presentation/       # UI-слой
    ├── navigation/     # Навигационный граф
    ├── theme/          # Material theme, attrs, colors, dimens, motion
    ├── di/             # Koin-модуль: viewModelModule
    ├── ui/
    │   ├── auth/       # Вход, регистрация, восстановление пароля
    │   ├── common/     # Переиспользуемые Compose-компоненты
    │   ├── main/       # Главный экран со списками покупок
    │   ├── onboarding/ # Стартовый экран
    │   ├── products/   # Экран товаров выбранного списка
    │   └── root/       # RootActivity — точка входа
```

**Поток данных:** `Composable Screen → ViewModel → UseCase → Repository interface → RepositoryImpl → Room DAO / Retrofit API / DataStore`

Основные правила зависимостей:

- `presentation` зависит от `domain` и не обращается напрямую к Room/Retrofit.
- `domain` не зависит от Android UI, Room, Retrofit и DataStore.
- `data` реализует интерфейсы репозиториев из `domain`.
- ViewModel не хранит Android View и работает только с состоянием экрана и use case.
- UI-строки хранятся в ресурсах, размеры - в `Dimens`, цвета — через тему и attrs.
- Для новых зависимостей используется Koin-модуль соответствующего слоя.

Koin-модули (`dataModule`, `domainModule`, `viewModelModule`, `themeModule`) инициализируются в `App.kt`.

---

## Правила командной разработки

### Архитектурные правила

- Для новых экранов используется связка `Route + Screen + ViewModel + UiState`.
- `Route` получает ViewModel через Koin и связывает ViewModel с навигацией.
- `Screen` должен быть максимально stateless: принимает состояние и callbacks.
- Бизнес-операции оформляются через use case в `domain`.
- Все операции с локальными данными проходят через репозитории и Room DAO.
- Сетевой слой находится в `data/remote`, модели API не используются напрямую в UI.
- Секреты, ключи подписи и локальные настройки не коммитятся в репозиторий.

### Качество кода

- Перед Pull Request нужно проверить сборку и статический анализ:

```bash
./gradlew detektAll :app:assembleDebug
```

- Для release-сборки дополнительно проверяется:

```bash
./gradlew :app:assembleRelease :app:bundleRelease
```

- Detekt настроен на контроль сложности:
  - тело функции меньше 50 строк;
  - количество аргументов функции меньше 6;
  - тело класса меньше 350 строк.

### Git-flow

- `main` - релизная ветка; прямые коммиты запрещены; force push запрещён; удаление запрещено
- `develop` - интеграционная ветка; все фичи вливаются через Pull Request
- Фичевые ветки - именование: `feature/<короткое-описание>`
- Релизная ветка - `release-1.0.0`, ответвляется от `develop`, PR в `main`

### Правила Pull Request

- Минимум **1 approving review** для merge в `main`
- Stale review сбрасывается после новых коммитов
- Перед merge все conversations должны быть закрыты/resolved

### Разделение задач

Каждый разработчик реализует свой блок «насквозь» — от Room entity до Compose UI.

---

## Команда и задачи
#### Команда №1
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

- **minSdk:** 24 (Android 7.0 Nougat)
- **targetSdk:** 36
- Релизная сборка: ProGuard + `minifyEnabled = true` + подпись приложения

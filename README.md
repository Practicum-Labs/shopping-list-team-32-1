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
| **Сеть** | [Retrofit](https://square.github.io/retrofit/) + Gson + OkHttp | REST-клиент для авторизации/регистрации/восстановления пароля |
| **Хранение сессии/настроек** | [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore) | Токены авторизации (access/refresh/userId) и выбор темы |

**Тип проекта:** Монолитный (single-module)

---

## Архитектура

Проект построен по принципам **Clean Architecture** с паттерном **MVVM** в слое представления.

```
app/src/main/java/com/practicum/shoppinglist/
├── App.kt              # Application: инициализация Koin
├── data/               # Слой данных
│   ├── local/
│   │   ├── dao/        # Room DAO-интерфейсы
│   │   ├── entity/     # Room Entity-классы
│   │   ├── database/   # AppDatabase (Room) + миграции
│   │   └── datasource/ # DataStore: тема, токены авторизации
│   ├── remote/         # Сеть (Retrofit)
│   │   └── auth/       # AuthApi + DTO авторизации
│   ├── mapper/         # Преобразование Entity/DTO ↔ Domain Model
│   ├── repository/     # Реализации репозиториев
│   └── di/             # Koin-модуль: dataModule
├── domain/             # Бизнес-логика (чистый Kotlin, без Android-зависимостей)
│   ├── model/          # Domain-модели (ShoppingList, AuthSession, AuthError, …)
│   ├── repository/     # Интерфейсы репозиториев
│   ├── usecase/        # Use case'ы (в т.ч. usecase/auth)
│   └── di/             # Koin-модуль: domainModule
└── presentation/       # UI-слой (Compose)
    ├── ui/
    │   ├── onboarding/ # Приветственный экран (сплэш при запуске)
    │   ├── auth/       # login / register / recovery
    │   ├── main/       # Экран списков покупок
    │   ├── products/   # Экран товаров внутри списка
    │   └── root/       # RootActivity — точка входа
    ├── navigation/     # ShoppingListNavHost
    ├── theme/          # Тема (theme + attrs), Dimens, Typography, Motion
    └── di/             # Koin-модули: themeModule, viewModelModule
```

> Фичи разрабатываются в отдельных ветках и интегрируются в `develop`:
> авторизация — на `develop`, экран продуктов — на `feature/shoping_list_part2`.
> Дерево выше — целевая структура после интеграции.

**Поток данных:** `UI (Compose) → ViewModel → UseCase → Repository (interface) → RepositoryImpl → Room DAO / Retrofit API / DataStore`

Четыре Koin-модуля (`dataModule`, `domainModule`, `themeModule`, `viewModelModule`) инициализируются в `App.kt`.

---

## Функциональность

Поток экранов: **Приветственный экран** → **Авторизация** → **Мои списки** → **Продукты**.

### Приветственный экран
Показывается при каждом запуске как сплэш. После короткой задержки проверяет
локальную сессию и переходит на **Мои списки** (если пользователь авторизован)
или на экран **Входа**.

### Авторизация _(ветка `develop`)_
Вход, регистрация и восстановление пароля через REST API (Retrofit; базовый URL —
размещённый мок-бэкенд). Токены `access/refresh/user_id` хранятся в DataStore;
поддержаны проверка и обновление токена (`auth/check`, `auth/refresh`).

- **Вход** — email + пароль; валидация email и длины пароля, ошибки под полями, кнопка активна только при валидных данных
- **Регистрация** — email + пароль + повтор пароля (пароль > 6 символов, пароли совпадают, email валиден)
- **Восстановление пароля** — email → запрос письма для восстановления

Подробное ТЗ и контракты API: [docs/requirements/Экран регистрации (0-1-2) [VHeslT].md](docs/requirements/Экран%20регистрации%20(0-1-2)%20%5BVHeslT%5D.md)

### Мои списки
Создание / переименование / удаление списков (с диалогом подтверждения), свайпы,
дублирование, поиск по спискам, иконки из встроенного набора.

### Продукты _(ветка `feature/shoping_list_part2`)_
Добавление товара с количеством и единицей измерения (**л, мл, уп, пач, шт, кг, г**),
пометка «куплено», редактирование/удаление, свайпы, ручная сортировка drag & drop,
сортировка по алфавиту, автодополнение названий из отдельной таблицы БД,
действия «очистить купленные» / «удалить всё» (с подтверждением).
Редактор открывается как bottom sheet, растягивается на весь экран.

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

- **minSdk:** 24 (Android 7.0 Nougat)
- **targetSdk:** 36 · **compileSdk:** 37
- Релизная сборка (ProGuard + `minifyEnabled = true` + подпись APK) — **в планах**, пока не настроена (`isMinifyEnabled = false`)

Подробное ТЗ и критерии оценки: [docs/requirements/technical-requirements.md](docs/requirements/technical-requirements.md)

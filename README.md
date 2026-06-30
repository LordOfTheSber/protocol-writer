# Protocol Writer

Учебное приложение для **написания и чтения протоколов**. Цель проекта —
изучить возможности **Java 21–25** на реальном Spring-приложении.

Протокол — это универсальный документ, состоящий из упорядоченных **секций**
разных типов:

- **Текст** — заголовок и произвольный текст (повестка, вступление и т.п.);
- **Решения** — список поручений со статусом, ответственным и сроком;
- **Таблица** — колонки и строки.

Расширяемость набора секций моделируется через `sealed`-иерархию, а рендер и
статистика — через pattern matching.

## Стек

| Слой    | Технологии                                                        |
|---------|-------------------------------------------------------------------|
| Backend | Java 25, Spring Boot 3.5, Spring Web, Spring Data JPA, Flyway      |
| БД      | PostgreSQL (JSONB для хранения секций), запуск через Docker        |
| Frontend| React 19, TypeScript, Vite, TanStack Query, React Router          |
| Архитектура front | Feature-Sliced Design (FSD)                             |

## Какие фичи Java 21–25 показаны

| Фича | Где смотреть |
|------|--------------|
| **records** | `domain/TextSection`, `Decision`, DTO в `web/dto/*` |
| **sealed interface** | `domain/Section` (`permits TextSection, DecisionListSection, TableSection`) |
| **Pattern matching for switch** + **record patterns** | `domain/render/ProtocolRenderer`, `domain/ProtocolStatistics` |
| **Sequenced Collections** (`getFirst`/`getLast`) | `domain/ProtocolContent` |
| **Virtual Threads** | `application.yml` → `spring.threads.virtual.enabled=true` |

`switch` по `Section` исчерпывающий: добавление нового типа секции вызовет
ошибку компиляции, пока он не обработан во всех местах — это и есть главная
ценность `sealed` + pattern matching.

## Структура репозитория

```
backend/   — Spring Boot приложение (Maven)
  src/main/java/com/example/protocolwriter/
    domain/       — чистая модель: sealed Section, records, рендер, статистика
    persistence/  — JPA-сущность с JSONB-колонкой + репозиторий
    service/      — бизнес-логика (CRUD, рендер, статистика)
    web/          — REST-контроллер, DTO, обработчик ошибок
    config/       — CORS
  src/main/resources/db/migration/ — Flyway-миграции

frontend/  — React + TS + Vite, слои FSD:
  src/
    app/       — провайдеры (QueryClient, Router), роутинг, стили
    pages/     — protocols (список), protocol-read (чтение), protocol-edit (форма)
    widgets/   — protocol-list, protocol-viewer, protocol-editor
    features/  — create-protocol, delete-protocol, section-editor
    entities/  — protocol (типы, API, react-query хуки, бэйджи)
    shared/    — api-клиент, ui-кит, config, lib

docker-compose.yml — PostgreSQL + Adminer
```

## Требования

- **JDK 25** (проект целится в Java 25; см. `backend/pom.xml`)
- **Node.js 20+**
- **Docker** (для PostgreSQL)

## Запуск

### 1. База данных

```bash
docker compose up -d db
# Adminer (просмотр БД) — http://localhost:8081 (server: db, user/pass/db: protocol)
```

### 2. Бэкенд

```bash
cd backend
./mvnw spring-boot:run
# REST API на http://localhost:8080
```

### 3. Фронтенд

```bash
cd frontend
npm install
npm run dev
# UI на http://localhost:5173 (запросы /api проксируются на :8080)
```

## REST API

| Метод  | Путь                                   | Описание                          |
|--------|----------------------------------------|-----------------------------------|
| GET    | `/api/protocols`                       | список протоколов (краткий)       |
| GET    | `/api/protocols/{id}`                  | полный протокол                   |
| POST   | `/api/protocols`                       | создать                           |
| PUT    | `/api/protocols/{id}`                  | обновить                          |
| DELETE | `/api/protocols/{id}`                  | удалить                           |
| GET    | `/api/protocols/{id}/render?format=`   | рендер (`MARKDOWN`/`TEXT`)        |
| GET    | `/api/protocols/{id}/stats`            | статистика по секциям и решениям  |

Пример создания протокола:

```bash
curl -X POST http://localhost:8080/api/protocols \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Протокол совещания №1",
    "author": "Иванов",
    "status": "DRAFT",
    "sections": [
      { "type": "text", "heading": "Вступление", "body": "Совещание открыто." },
      { "type": "decisions", "heading": "Решения", "decisions": [
        { "text": "Подготовить отчёт", "status": "OPEN", "assignee": "Петров", "dueDate": "2026-07-10" }
      ]},
      { "type": "table", "heading": "Участники", "columns": ["Имя", "Роль"],
        "rows": [["Иванов", "Аналитик"], ["Петров", "Менеджер"]] }
    ]
  }'
```

## Тесты бэкенда

```bash
cd backend
./mvnw test
```

Покрыты: рендер (pattern matching), подсчёт статистики, REST-слой (`@WebMvcTest`).

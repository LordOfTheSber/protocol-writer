# Protocol Writer

Учебное приложение для **написания и чтения протоколов**. Цель проекта —
изучить возможности **Java 21–25** на реальном Spring-приложении.

Протокол — это **единый документ в формате Markdown** (по образцу Obsidian):
пишется сплошным текстом с форматированием, а внутрь можно вставлять
**диаграммы**:

- **Mermaid** — блоки ` ```mermaid ` рендерятся в схемы (flowchart, sequence,
  gantt и т.д.);
- **Excalidraw** — рисованные от руки схемы (блоки ` ```excalidraw ` со сценой).

Редактор — **split-режим**: слева Markdown с панелью форматирования, справа
живой предпросмотр с диаграммами.

## Стек

| Слой    | Технологии                                                        |
|---------|-------------------------------------------------------------------|
| Backend | Java 25, Spring Boot 3.5, Spring Web, Spring Data JPA, Flyway      |
| БД      | PostgreSQL (тело протокола — текст), запуск через Docker           |
| Frontend| React 19, TypeScript, Vite, TanStack Query, React Router          |
| Редактор| @uiw/react-md-editor (split), mermaid, @excalidraw/excalidraw     |
| Архитектура front | Feature-Sliced Design (FSD)                             |
| ИИ      | Ollama (open-source LLM: qwen2.5 / llama3.1 / mistral), Spring RestClient |

## Какие фичи Java 21–25 показаны

Бэкенд хранит тело протокола как обычный markdown-текст; диаграммы — это блоки
кода внутри текста, поэтому рендер целиком на фронте. Java-фичи живут в
**анализаторе markdown**, который разбирает текст для оглавления и статистики:

| Фича | Где смотреть |
|------|--------------|
| **records** | `domain/markdown/MarkdownBlock` (Heading/FencedCode/Paragraph), DTO в `web/dto/*` |
| **sealed interface** | `domain/markdown/MarkdownBlock` (наследники в том же файле, `permits` выводится) |
| **Pattern matching for switch** + **record patterns** + guard'ы `when` | `domain/markdown/MarkdownAnalyzer` (статистика и оглавление) |
| **Sequenced Collections** (`getFirst`/`getLast`) | оглавление в `MarkdownAnalyzer`/тестах |
| **Virtual Threads** | `application.yml` → `spring.threads.virtual.enabled=true` |

`switch` по `MarkdownBlock` исчерпывающий: добавление нового типа блока вызовет
ошибку компиляции, пока он не обработан — в этом ценность `sealed` + pattern
matching.

## Структура репозитория

```
backend/   — Spring Boot приложение (Maven)
  src/main/java/com/example/protocolwriter/
    ai/               — интеграция с Ollama (клиент, промпты, structured output)
    domain/markdown/  — sealed MarkdownBlock, анализатор (оглавление, статистика)
    persistence/      — JPA-сущность (тело как text) + репозиторий
    service/          — бизнес-логика (CRUD, статистика, оглавление)
    web/              — REST-контроллер, DTO, обработчик ошибок
    config/           — CORS
  src/main/resources/db/migration/ — Flyway-миграции (V1, V2)

frontend/  — React + TS + Vite, слои FSD:
  src/
    app/       — провайдеры (QueryClient, Router), роутинг, стили
    pages/     — protocols (список), protocol-read (чтение + оглавление/статистика),
                 protocol-edit (split-редактор)
    widgets/   — protocol-list, protocol-viewer, protocol-editor
    features/  — create-protocol, delete-protocol, insert-diagram (Mermaid/Excalidraw),
                 ai-assist (улучшение текста, AI-анализ)
    entities/  — protocol (типы, API, react-query хуки)
    shared/    — api-клиент, ui-кит, markdown (MarkdownView + рендер диаграмм), lib

docker-compose.yml — PostgreSQL + Adminer + Ollama
```

Excalidraw подгружается **лениво** (React.lazy), чтобы тяжёлая библиотека не
попадала в основной бандл — она загружается только при открытии редактора схем
или рендере документа со схемой.

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

| Метод  | Путь                          | Описание                          |
|--------|-------------------------------|-----------------------------------|
| GET    | `/api/protocols`              | список протоколов (краткий)       |
| GET    | `/api/protocols/{id}`         | полный протокол (с markdown-телом)|
| POST   | `/api/protocols`              | создать                           |
| PUT    | `/api/protocols/{id}`         | обновить                          |
| DELETE | `/api/protocols/{id}`         | удалить                           |
| GET    | `/api/protocols/{id}/stats`   | статистика (слова, заголовки, диаграммы) |
| GET    | `/api/protocols/{id}/outline` | оглавление (заголовки по порядку) |
| POST   | `/api/protocols/{id}/analyze` | AI-анализ протокола (LLM, structured output) |
| POST   | `/api/ai/improve`             | улучшение текста (`mode`: IMPROVE / SHORTEN / EXPAND / FIX_GRAMMAR) |

Пример создания протокола:

```bash
curl -X POST http://localhost:8080/api/protocols \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Протокол совещания №1",
    "author": "Иванов",
    "status": "DRAFT",
    "body": "# Повестка\n\nОбсудили план.\n\n```mermaid\nflowchart TD\n  A[Старт] --> B[Готово]\n```\n"
  }'
```

## ИИ-функции (open-source LLM)

Приложение умеет **анализировать протоколы** и **улучшать текст** с помощью
локальной open-source модели через [Ollama](https://ollama.com) — облачные API
и ключи не нужны, всё работает на вашей машине.

Возможности:

- **AI-анализ** (страница чтения): резюме, сильные стороны, проблемы
  (нет сроков/ответственных, расплывчатые формулировки), рекомендации и список
  найденных решений/поручений. Ответ модели типизирован через
  **structured output** Ollama (JSON-схема).
- **ИИ-помощник в редакторе**: улучшить стиль / сократить / развернуть /
  исправить ошибки. Результат показывается в предпросмотре — заменить текст
  можно только явно.

Запуск:

```bash
docker compose up -d ollama
docker compose exec ollama ollama pull qwen2.5:7b   # один раз, ~4.7 ГБ
```

Настройки — в `application.yml` (`app.ai.*`) или через переменные окружения
`OLLAMA_BASE_URL` и `OLLAMA_MODEL`. Модель по умолчанию — `qwen2.5:7b`
(хорошо работает с русским); на слабой машине можно взять `qwen2.5:3b`.
Если Ollama не запущена или модель не скачана, API вернёт `503` с подсказкой.

Интеграция сделана без тяжёлых зависимостей — обычный Spring `RestClient`
(`backend/.../ai/OllamaClient.java`): records под JSON-протокол Ollama,
text blocks для промптов, исчерпывающий `switch` по режимам улучшения.
Долгие вызовы LLM не блокируют платформенные потоки благодаря Virtual Threads.

## Тесты бэкенда

```bash
cd backend
./mvnw test
```

Покрыты: анализатор markdown (pattern matching, оглавление, статистика) и
REST-слой (`@WebMvcTest`).

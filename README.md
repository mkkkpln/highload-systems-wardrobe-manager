# Wardrobe Manager — пояснение к лабораторной работе

## Предметная область

Система Wardrobe Manager предназначена для ведения онлайн-гардероба и подбора образов.
Пользователь может:
- добавлять вещи в гардероб (например: куртка, платье, сумка),
- хранить информацию о характеристиках предметов (тип, бренд, цвет, сезон, фото),
- создавать готовые образы (outfits) из нескольких вещей,
- управлять своим гардеробом через REST API (CRUD-операции).
Таким образом, приложение моделирует повседневный сценарий использования: у каждого пользователя есть свой набор вещей, из которых он формирует комбинации для разных случаев.

## Задача

Реализация монолитного приложения Wardrobe Manager на Spring Boot.
Приложение позволяет пользователю вести свой онлайн-гардероб и подбирать готовые образы.

**Функционал**:

1. CRUD для пользователей – регистрация, редактирование, удаление.
2. CRUD для предметов гардероба – добавление, просмотр, редактирование, удаление.
3. CRUD для образов (outfits) – создание образов из нескольких вещей.
4. Поддержка связей:
   - Пользователь ↔ Предметы гардероба (One-to-Many).
   - Пользователь ↔ Образы (One-to-Many).
   - Образы ↔ Предметы гардероба (Many-to-Many через таблицу outfit_items с дополнительным полем role).
5. Валидация DTO и Entity.
6. Глобальный хендлинг ошибок.
7. Поддержка пагинации и ограничений (≤ 50 записей за запрос).

**Цель**: реализовать систему с чистой архитектурой (controller–service–repository–entity–dto–mapper), обеспечить правильные связи в БД и корректное REST API с валидацией.


## Архитектура базы данных

Схема БД состоит из четырёх основных таблиц:

<img width="555" height="460" alt="image" src="https://github.com/user-attachments/assets/95cd5c46-411c-4169-a122-23a3694dc94b" />



## Структура проекта

```
src/main/java/com/example/highloadsystemswardrobemanager/

├── controller/                 # REST-контроллеры
│   ├── UserController.java
│   ├── WardrobeItemController.java
│   └── OutfitController.java
│
├── dto/                        # DTO-модели с валидацией
│   ├── UserDto.java
│   ├── WardrobeItemDto.java
│   └── OutfitDto.java
│
├── entity/                     # JPA-сущности
│   ├── User.java
│   ├── WardrobeItem.java
│   ├── Outfit.java
│   └── OutfitItem.java
│
├── exception/                  # Обработка ошибок
│   ├── GlobalExceptionHandler.java
│   └── NotFoundException.java
│
├── mapper/                     # MapStruct-мапперы
│   ├── UserMapper.java
│   ├── WardrobeItemMapper.java
│   └── OutfitMapper.java
│
├── repository/                 # Spring Data JPA репозитории
│   ├── UserRepository.java
│   ├── WardrobeItemRepository.java
│   └── OutfitRepository.java
│
├── service/                     # Бизнес-логика
│   ├── UserService.java
│   ├── WardrobeItemService.java
│   └── OutfitService.java
│
└── HighloadSystemsWardrobeManagerApplication.java  # Точка входа

```

## Алгоритм работы

1. Пользователь создаёт аккаунт.
2. Добавляет вещи в свой гардероб (тип, бренд, цвет, сезон, фото).
3. Создаёт образы: объединяет несколько вещей в Outfit, при необходимости задаёт роль каждой вещи (верх, обувь, аксессуар).
4. Просматривает списки пользователей, вещей, образов:
     - с пагинацией (с подсчётом общего числа элементов),
     - или в формате «бесконечной ленты» (scroll, без total count).
5. Редактирует и удаляет объекты.
6. При создании/обновлении Outfit используется транзакция (гарантия согласованности: либо добавятся все вещи, либо не добавится ни одна).

## Быстрый старт

### Через Docker (рекомендуется)
```bash
docker compose down
docker compose up --build -d
docker compose ps
docker compose logs -f app
```
- Приложение: http://localhost:8080

### Swagger
- UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

### Пагинация и «лента»
- Постранично: GET /{resource}/paged?page=0&size=10
  - Заголовок ответа: X-Total-Count — общее количество записей
  - size 1..50

- Бесконечная лента: GET /{resource}/scroll?offset=0&limit=10
  - Без total
  - limit 1..50 (кап)
- Для всех findAll использовать /paged или /scroll.

### Примеры запросов
```bash
# Пагинация с total
curl -s -D - "http://localhost:8080/outfits/paged?page=0&size=10" -o /dev/null | grep -i X-Total-Count

# Лента с капом (limit>50 будет урезан до 50)
curl -s "http://localhost:8080/items/scroll?offset=0&limit=1000"

# Валидация параметров (ожидаем 400)
curl -i "http://localhost:8080/outfits/0"

# Валидация тела (ожидаем 400)
curl -i -X POST "http://localhost:8080/outfits" -H "Content-Type: application/json" -d '{}'
```

### Формат ошибок (GlobalExceptionHandler)
```bash
// 400 — невалидные поля DTO
{
"error": "VALIDATION_FAILED",
"details": [
{ "field": "title", "message": "must not be blank" }
]
}

// 400 — ошибка параметров (@Min/@Max/@Pattern)
{
"error": "CONSTRAINT_VIOLATION",
"message": "getById.id: must be greater than or equal to 1"
}

// 400 — неверный тип параметра
{ "error": "TYPE_MISMATCH", "message": "Invalid value for parameter 'id'" }

// 404 — не найдено
{ "error": "NOT_FOUND", "message": "Outfit not found: 123" }

// 409 — конфликт целостности (уникальность и т.п.)
{ "error": "CONFLICT", "message": "Data integrity violation" }

// 500 — общее
{ "error": "INTERNAL_ERROR", "message": "Unexpected error" }
```

### Покрытие тестами
Минимальный общий процент покрытия кода тестами должен быть 70%. У меня 91%.
<img width="777" height="653" alt="image" src="https://github.com/user-attachments/assets/c14664bb-ec43-4b0c-8c14-06315e2c7ad2" />

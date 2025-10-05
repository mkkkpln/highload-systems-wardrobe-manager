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

src/
 ├─ main/
 │  ├─ java/com/example/highloadsystemswardrobemanager/
 │  │  ├─ controller/
 │  │  │  ├─ OutfitController.java
 │  │  │  ├─ UserController.java
 │  │  │  └─ WardrobeItemController.java
 │  │  ├─ dto/
 │  │  │  ├─ OutfitDto.java
 │  │  │  ├─ OutfitItemLinkDto.java
 │  │  │  ├─ UserDto.java
 │  │  │  └─ WardrobeItemDto.java
 │  │  ├─ entity/       # JPA сущности и enum’ы (в БД сериализуются как строки)
 │  │  ├─ exception/    # NotFoundException, GlobalExceptionHandler
 │  │  ├─ mapper/       # MapStruct/ручные мапперы DTO↔Entity
 │  │  ├─ repository/   # Spring Data JPA репозитории
 │  │  └─ service/
 │  │     ├─ OutfitService.java       # @Transactional create/update
 │  │     ├─ UserService.java
 │  │     └─ WardrobeItemService.java # cap limit ≤ 50, нормализация параметров
 │  └─ resources/
 │     ├─ application.yml / application-docker.yml
 │     └─ db/changelog/** (Liquibase)
 └─ test/java/com/example/highloadsystemswardrobemanager/
    ├─ ErrorHandlingOutfitWebMvcTest.java
    ├─ OutfitControllerTest.java
    ├─ OutfitIntegrationTest.java
    ├─ UserControllerTest.java
    ├─ UserIntegrationTest.java
    ├─ WardrobeItemControllerTest.java
    ├─ WardrobeItemIntegrationTest.java
    └─ WardrobeItemServiceUnitTest.java
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


### Покрытие тестами
Минимальный общий процент покрытия кода тестами должен быть 70%. 

<img width="735" height="582" alt="image" src="https://github.com/user-attachments/assets/1c9bbbf2-1819-4493-959e-9a3066999d7b" />


### Git-коммиты

Коммиты — Conventional Commits:
   - feat(api): add paged/scroll endpoints with caps
   - fix(errors): return structured JSON for 400/404/409/500
   - test(webmvc,unit): cover pagination and error handling
   - chore(docker): remove deprecated compose version key
   - docs(readme): add docker & testing guide

#### (1) «Каждый findAll должен иметь пагинацию. Нельзя отдавать больше 50 записей за запрос»

Outfits

- Контроллер: [OutfitController](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/controller/OutfitController.java)#getPaged (GET /outfits/paged)
- Сервис: [OutfitService](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/service/OutfitService.java)#getPaged(int page, int size). Внутри идёт нормализация параметров:
size капится до ≤ 50 (capTo50(size)), page не меньше нуля (atLeastZero(page)), затем PageRequest.of(...).
- Тесты: OutfitControllerTest#shouldReturnPagedOutfits, OutfitControllerTest#paged_genericException_returns500.

Wardrobe items

- Контроллер: [WardrobeItemController](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/controller/WardrobeItemController.java)#getPagedWithCount (GET /items/paged)
- Сервис: [WardrobeItemService](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/service/WardrobeItemService.java)#getPagedWithCount(int page, int size). Также нормализация: page >= 0, size ≤ 50 → PageRequest.of(...).
- Тесты:
   - WardrobeItemControllerTest#getPagedItems (проверяет заголовок и ответ).
   - WardrobeItemServiceUnitTest#getPagedWithCount_sanitizesParams_andMaps (проверяет, что в репозиторий реально уходит Pageable c size=50, page=0 при входных -3 и 1000).


#### (2) «Минимум один запрос с бесконечной прокруткой (без total)»

Outfits (лента)
- Контроллер: [OutfitController](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/controller/OutfitController.java)#getInfiniteScroll (GET /outfits/scroll?offset&limit). В контроллере стоят валидации (@Min(0) для offset, @Min(1) @Max(50) для limit).
- Сервис: [OutfitService](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/service/OutfitService.java)#getInfiniteScroll(int offset, int limit). Делает: limit = capTo50(limit), offset = atLeastZero(offset), считает pageIndex = offset / limit, затем PageRequest.of(pageIndex, limit).
- Тесты:
   - OutfitControllerTest#shouldReturnInfiniteScrollChunk и #scrollShouldRejectTooLargeLimit (limit > 50 → 400).

Wardrobe items (лента)
- Контроллер: [WardrobeItemController](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/controller/WardrobeItemController.java)#getInfiniteScroll (GET /items/scroll?offset&limit)
- Сервис: [WardrobeItemService](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/service/WardrobeItemService.java)#getInfiniteScroll(int offset, int limit). Тот же подход: capTo50(limit) + pageIndex = offset / limit.
- Тесты:
   - WardrobeItemControllerTest#getInfiniteScroll (успешный кейс).
   - WardrobeItemServiceUnitTest#getInfiniteScroll_capsLimit_andComputesPageIndex и #getInfiniteScroll_limitLessThanOne_becomesOne — проверяет кап лимита и корректный расчёт pageIndex.

#### (3) «Минимум один запрос с пагинацией и общим количеством в HTTP-заголовке»

Outfits
- Контроллер: [OutfitController](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/controller/OutfitController.java)#getPaged (GET /outfits/paged). В ответе заголовок X-Total-Count берётся из PagedResult.totalCount().
- Сервис: [OutfitService](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/service/OutfitService.java)#getPaged возвращает new PagedResult<>(items, pageData.getTotalElements()).
- Тест: OutfitControllerTest#shouldReturnPagedOutfits → проверяет X-Total-Count.

Wardrobe items

- Контроллер: [WardrobeItemController](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/controller/WardrobeItemController.java)#getPagedWithCount (GET /items/paged). Заголовок X-Total-Count = page.getTotalElements().
- Сервис: [WardrobeItemService](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/service/WardrobeItemService.java)#getPagedWithCount.
- Тест: WardrobeItemControllerTest#getPagedItems → проверяет заголовок.

#### (4) «На сложных запросах должны использоваться транзакции (≥2). »

**Transactional #1**: создание образа (Метод: [OutfitService](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/service/OutfitService.java)#create(OutfitDto dto) @Transactional)

- Загружает User по dto.userId (возможен NotFoundException).
- Создаёт Outfit и связывает с пользователем.
- Для каждого [OutfitItemLinkDto](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/dto/OutfitItemLinkDto.java):
   - загружает WardrobeItem по itemId (тоже может бросать NotFoundException);
   - добавляет в связку outfit.addItem(item, role, index).
- Сохраняет Outfit вместе с join-записями.

**Почему нужна транзакция**: это единая бизнес-операция. В ней несколько шагов и несколько таблиц (главная + таблица связей). Если на середине произойдёт ошибка (любая из проверок не прошла, БД отказала по констрейнту и т.п.), всё откатывается, и в БД не останется «полусозданного» образа без связей или с их частью. Это и есть требуемая атомарность.

**Transactional #2**: обновление образа (Метод: [OutfitService](https://github.com/mkkkpln/highload-systems-wardrobe-manager/blob/main/src/main/java/com/example/highloadsystemswardrobemanager/service/OutfitService.java)#update(Long id, OutfitDto dto) @Transactional)

- Загружает Outfit по id (NotFoundException если нет).
- Обновляет заголовочные поля (title и т.п.).
- outfit.clearItems() — очищает все предыдущие связи (join-entity).
- Повторно добавляет связи addItem(...) с новой ролью/порядком.
- Сохраняет.

**Почему нужна транзакция**: здесь есть фаза «снять все связи» → «поставить новые». Если в процессе что-то пойдёт не так (например, один из новых itemId не существует), без транзакции мы останемся в битом состоянии: заголовок обновлён, старые связи удалены, новые не добавились. Транзакция гарантирует: либо всё новое состояние успешно записано, либо откат к прежнему (последовательноcть изменений атомарна).




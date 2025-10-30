## 🌐 Языки / Languages

- 🇬🇧 [English](README.md)

- # ☕ CafeApi — Сервер для кофейни с удалённым заказом

CafeApi — это backend-приложение для кофейни, реализующее модель удалённого заказа и самовывоза. Пользователи могут оформить заказ заранее и забрать его, когда он будет готов. Проект вдохновлён концепцией HotFix и демонстрирует современный стек разработки с Docker, Spring Boot и OpenAPI.

---

## ⚙️ Основной функционал

- 📦 Заказ напитков через API
- ⏳ Отслеживание статуса заказа (создан, готов, получен)
- 👤 Управление пользователями
- 🧾 Хранение истории заказов
- 🔐 Безопасность и валидация входных данных
- 📄 Документация API через OpenAPI (Swagger)

---

## 🛠️ Технологии

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- OpenAPI (Swagger)
- Maven


---

## 🚀 Быстрый старт

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/noverlean/CafeApi.git
   cd CafeApi
   ```
2. Запустите окружение:
   ```bash
   docker-compose up --build
   ```
3. Запустите приложение из IntelliJ или через Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Откройте Swagger-документацию:
   ```код
   http://localhost:8080/swagger-ui/index.html
   ```

---

## 📦Примеры API
- POST /orders — создать заказ
- GET /orders/{id} — получить статус заказа
- GET /orders/user/{userId} — история заказов пользователя
- POST /users — регистрация пользователя

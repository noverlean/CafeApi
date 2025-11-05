## 🌐 Языки / Languages

- 🇷🇺 [Русский](README.ru.md)

# ☕ CafeApi — Coffee Shop Server with Remote Ordering

CafeApi is a backend application for a coffee shop that enables remote ordering and pickup. Inspired by the HotFix concept, it allows users to place orders in advance and pick them up when ready! The project showcases a modern tech stack including Docker, Spring Boot, and OpenAPI.

Recently, I reset my OS and didn’t check how long ago I had updated my origin branch on Git. As a result, here you can see a very old version of the project repository, without OpenAPI declarations and with many samples of backend code.

---

## ⚙️ Core Features

- 📦 Place drink orders via API  
- ⏳ Track order status (created, ready, picked up)  
- 👤 User management  
- 🧾 Order history tracking  
- 🔐 Input validation and basic security  
- 📄 API documentation via OpenAPI (Swagger)

---

## 🛠️ Technologies Used

- Java 17+  
- Spring Boot  
- Spring Web  
- Spring Data JPA  
- PostgreSQL  
- Docker & Docker Compose  
- OpenAPI (Swagger)  
- Maven

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/noverlean/CafeApi.git
   cd CafeApi
   ```
2. Start the environment:
   ```bash
   docker-compose up --build
   ```
3. Run the application from IntelliJ or via Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Open Swagger UI:
   ```code
   http://localhost:8080/swagger-ui/index.html
   ```

---

📦 Sample API Endpoints
- POST /orders — Create a new order
- GET /orders/{id} — Get order status
- GET /orders/user/{userId} — Get user order history
- POST /users — Register a new user   

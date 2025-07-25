# dekadev-simple-api

A clean, secure, and modern Spring Boot REST API boilerplate using:

- ✅ Java 17+
- ✅ Spring Boot 3
- ✅ Spring Security with JWT
- ✅ PostgreSQL as the database
- ✅ Swagger UI for testing APIs
- ✅ JPA (Hibernate) with layered architecture (Controller → Service → Repository)

---

## 🚀 Features

- 🔐 JWT authentication with login endpoint
- 👤 User registration with role-based access
- 📚 Swagger UI with token support
- 🔁 BCrypt password encryption
- 🔒 Role-based authorization using `@PreAuthorize`
- 🧹 Clean project structure

---

## ⚙️ Requirements

- Java 17+
- Maven 3.8+
- PostgreSQL 13+

---

## 📦 Getting Started

### 1. 🛠 Set up PostgreSQL

Create a PostgreSQL database and user:

```sql
CREATE DATABASE dekadev_api;
CREATE USER dekadev_user WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE dekadev_api TO dekadev_user;

# Ecommerce Backend API

A REST API built using Java, Spring Boot, Gradle, and PostgreSQL.

## Tech Stack

* Java & Spring Boot
* Gradle
* PostgreSQL & Flyway
* Docker & Docker Compose
* Spring Boot Actuator

## Prerequisites

* Java
* Docker Desktop
* Git
* Postman or curl

## Getting Started

**1. Start PostgreSQL**

```bash
docker compose up -d
```

**2. Configure environment variables**

Set the database credentials and server port required by `application.yml`.

**3. Run the application**

```bash
./gradlew bootRun
```

API base URL: `http://localhost:8081`

## API Testing

Create a product:

`POST /api/v1/products`

```json
{
  "name": "Wireless Mouse",
  "description": "Wireless mouse",
  "price": 799.00,
  "quantity": 10
}
```

Health check: `GET /actuator/health`

## Run Tests

```bash
./gradlew test
```

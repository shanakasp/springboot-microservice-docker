# Spring Boot Microservices — User & Blog Services

A database-per-service microservice setup.
User Service runs on **port 8081**, Blog Service on **port 8082**.

---

## Architecture

```
┌──────────────────────┐             ┌──────────────────────┐
│ user-postgres (5432) │             │ blog-postgres (5433) │
│   DB: user_db        │             │   DB: blog_db        │
│   users table        │             │   blogs table        │
└───────────▲──────────┘             └───────────▲──────────┘
      │                                    │
    ┌────┴────────────┐                  ┌────┴────────────┐
    │  user-service    │◄────────────────│  blog-service    │
    │  port: 8081      │  HTTP GET       │  port: 8082      │
    │  owns users data │  /api/users/{id}│  owns blogs data │
    └──────────────────┘                  └──────────────────┘
```

**Relationship:** One User -> Many Blogs by logical reference (`createdBy`), validated via API call.

---

## Quick Start

### Option 1: Docker Compose (Recommended)

```bash
# From the root directory
docker-compose up --build
```

Both services, two PostgreSQL containers, and pgAdmin will start automatically.

### Option 2: Run Locally

1. Start PostgreSQL instances and create databases:
```sql
CREATE DATABASE user_db;
CREATE DATABASE blog_db;
```

2. Run User Service:
```bash
cd user-service
mvn spring-boot:run
# Starts on http://localhost:8081
```

3. Run Blog Service:
```bash
cd blog-service
mvn spring-boot:run
# Starts on http://localhost:8082
```

Postgres Web UI - http://localhost:5050/browser/

---

## API Reference

### User Service — `http://localhost:8081`

#### Create a User
```
POST /api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com"
}
```
**Response 201:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "createdAt": "2024-01-15T10:30:00"
}
```

---

#### Get All Users
```
GET /api/users
```
**Response 200:**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "createdAt": "2024-01-15T10:30:00"
  }
]
```

---

#### Get User by ID
```
GET /api/users/{id}
```

---

### Blog Service — `http://localhost:8082`

#### Create a Blog
```
POST /api/blogs
Content-Type: application/json

{
  "blogName": "My First Blog",
  "content": "This is the blog content...",
  "createdBy": 1
}
```
> `createdBy` must be a valid user ID from user-service (validated over HTTP).

**Response 201:**
```json
{
  "id": 1,
  "blogName": "My First Blog",
  "content": "This is the blog content...",
  "createdBy": 1,
  "createdAt": "2024-01-15T11:00:00"
}
```

---

#### Get All Blogs
```
GET /api/blogs
```
**Response 200:**
```json
[
  {
    "id": 1,
    "blogName": "My First Blog",
    "content": "This is the blog content...",
    "createdBy": 1,
    "createdAt": "2024-01-15T11:00:00"
  }
]
```

---

#### Get Blogs by User
```
GET /api/blogs/user/{userId}
```
Returns all blogs created by a specific user.

---

## Validation Rules

| Field      | Rule                            |
|------------|---------------------------------|
| name       | Required, not blank             |
| email      | Required, valid email, unique   |
| blogName   | Required, not blank             |
| content    | Required, not blank             |
| createdBy  | Required, must be a valid user ID in user-service |

---

## Error Responses

**400 Bad Request — Validation failure:**
```json
{
  "email": "Email must be valid",
  "name": "Name is required"
}
```

**400 Bad Request — Business logic error:**
```json
{
  "error": "User not found with id: 99"
}
```

---

## Tech Stack

- Java 17
- Spring Boot 3.2
- Spring Data JPA
- Spring WebFlux (WebClient for inter-service calls)
- PostgreSQL 15
- Lombok
- Docker & Docker Compose

---

## pgAdmin Connections

Open pgAdmin at `http://localhost:5050` and register two servers:

1. User DB server
  - Host: `user-postgres`
  - Port: `5432`
  - Database: `user_db`
  - Username: `postgres`
  - Password: `postgres`

2. Blog DB server
  - Host: `blog-postgres`
  - Port: `5432`
  - Database: `blog_db`
  - Username: `postgres`
  - Password: `postgres`

# Task Management API

A Spring Boot REST API for collaborative task management with JWT authentication and role-based access control.

## Features

- User authentication with JWT tokens
- Role-based access control (ADMIN and USER roles)
- Task management operations
- H2 in-memory database
- Swagger UI documentation

## Technology Stack

- Java 21
- Spring Boot 3.1.5
- Spring Security
- Spring Data JPA
- H2 Database
- JWT Authentication
- SpringDoc OpenAPI (Swagger)
- Lombok
- Maven

## Getting Started

1. Clone the repository:
```bash
git clone <repository-url>
cd jpa--demo
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## API Documentation

Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Authentication
- POST `/api/auth/register` - Register new user
- POST `/api/auth/login` - Login and get JWT token

### Tasks
- GET `/api/tasks` - Get all tasks
- POST `/api/tasks` - Create new task
- GET `/api/tasks/{id}` - Get task by ID
- PUT `/api/tasks/{id}` - Update task
- DELETE `/api/tasks/{id}` - Delete task

## Role-Based Access

### ADMIN Users
- Can create tasks and assign them to any user
- Can view, update, and delete any task
- Full access to all API endpoints

### Normal Users
- Can create tasks only for themselves
- Can view their own tasks
- Can update and delete their own tasks

## Task Creation Examples

### As ADMIN:
```json
{
  "title": "Complete project docs",
  "description": "Write technical documentation",
  "status": "À_FAIRE",
  "assignedUserId": 2
}
```

### As Normal User:
```json
{
  "title": "Complete project docs",
  "description": "Write technical documentation",
  "status": "À_FAIRE"
}
```

## Security

- JWT token-based authentication
- Password encryption using BCrypt
- Role-based authorization
- Stateless session management

## License

[MIT License](LICENSE)

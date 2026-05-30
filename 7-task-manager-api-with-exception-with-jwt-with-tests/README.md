docker run --name task-manager-db ^
  -e POSTGRES_DB=taskmanager ^
  -e POSTGRES_USER=admin ^
  -e POSTGRES_PASSWORD=admin123 ^
  -p 5432:5432 ^
  -d postgres






When using Optional<T> return type, the errors are handled by the controller.
on the other hand, to handle it in service classes. we will use just <T> and not add Optional

# Spring Boot REST API — Learning Journey

A complete guide to building a production-ready REST API with Spring Boot, covering core concepts from dependency injection to JWT authentication.
 
---

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Tools & Technologies](#tools--technologies)
3. [What We Built](#what-we-built)
4. [Phase 2 — Spring Boot Basics](#phase-2--spring-boot-basics)
5. [Phase 3 — Data & Persistence](#phase-3--data--persistence)
6. [Phase 4 — REST API Best Practices](#phase-4--rest-api-best-practices)
7. [Phase 5 — Security](#phase-5--security)
8. [Annotations Reference](#annotations-reference)
9. [Key Concepts & Keywords](#key-concepts--keywords)
10. [Project Structure](#project-structure)
11. [API Endpoints](#api-endpoints)
12. [How to Run](#how-to-run)
---

## Project Overview

A **Task Manager REST API** built with Spring Boot, allowing users to register, login, and manage their tasks. The API is secured with JWT authentication and follows REST best practices including DTOs, validation, and global exception handling.

### Output
- A fully functional REST API with authentication
- PostgreSQL database with persistent storage
- Swagger UI for interactive API documentation
- Docker-based database setup
- Clean layered architecture following industry standards
---

## Tools & Technologies

| Tool | Version | Purpose |
|---|---|---|
| Java | 17 / 21 | Programming language |
| Spring Boot | 3.x / 4.x | Application framework |
| Spring Data JPA | — | Database abstraction layer |
| Spring Security | — | Authentication & authorization |
| Hibernate | — | ORM (Object Relational Mapping) |
| PostgreSQL | Latest | Production database |
| H2 | — | In-memory database (development) |
| Docker | Latest | Running PostgreSQL locally |
| JJWT | 0.12.3 | JWT token generation & validation |
| Springdoc OpenAPI | 2.8.6 | Swagger UI documentation |
| Maven | — | Build & dependency management |
| IntelliJ IDEA | — | IDE |
| IntelliJ HTTP Client | — | Testing API endpoints |
 
---

## What We Built

### Phase 2 — Library Management API (in-memory)
- Basic CRUD endpoints for books
- In-memory ArrayList storage
- REST controller with proper HTTP methods
### Phase 3 — Task Manager API (with database)
- User and Task entities with a one-to-many relationship
- PostgreSQL database via Docker
- Spring Data JPA repositories
- H2 console for development inspection
### Phase 4 — Production-Quality API
- Global exception handling with custom exceptions
- Input validation with Jakarta Validation
- DTOs (Data Transfer Objects) for clean request/response separation
- Separate create/update request DTOs
### Phase 5 — Secured API
- JWT-based authentication
- BCrypt password hashing
- Role-based access control (`ROLE_USER`, `ROLE_ADMIN`)
- Protected endpoints requiring valid JWT tokens
- Swagger UI with JWT authorization support
---

## Phase 2 — Spring Boot Basics

### Core Concepts Learned
- **IoC (Inversion of Control)** — Spring manages object creation, not you
- **Dependency Injection** — Spring injects dependencies through constructors
- **Layered Architecture** — Controller → Service → Repository → Database
- **REST endpoints** — GET, POST, PUT, PATCH, DELETE
### Key Files Created
- `Book.java` — model
- `BookRepository.java` — data access (in-memory ArrayList)
- `BookService.java` — business logic
- `BookController.java` — HTTP layer
### What PUT vs PATCH Means
- `PUT` — replaces the entire object (send all fields)
- `PATCH` — updates only provided fields (send partial data)
### `ResponseEntity` — HTTP Response Control
```java
ResponseEntity.ok(body)              // 200 OK
ResponseEntity.status(201).body(x)   // 201 Created
ResponseEntity.notFound().build()    // 404 Not Found
ResponseEntity.noContent().build()   // 204 No Content
ResponseEntity.badRequest().build()  // 400 Bad Request
```
 
---

## Phase 3 — Data & Persistence

### Core Concepts Learned
- **JPA (Java Persistence API)** — standard for ORM in Java
- **Hibernate** — JPA implementation, auto-creates tables from entities
- **Spring Data JPA** — repository interfaces with auto-generated queries
- **One-to-Many relationships** — one user has many tasks
- **Enums in database** — stored as strings with `@Enumerated`
- **Infinite recursion** — solved with `@JsonManagedReference` / `@JsonBackReference`
### Database Setup with Docker
```bash
docker run --name task-manager-db -e POSTGRES_DB=taskmanager -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin123 -p 5432:5432 -d postgres
```

### `application.properties` — PostgreSQL Config
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanager
spring.datasource.username=admin
spring.datasource.password=admin123
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### `ddl-auto` Values
| Value | Behavior |
|---|---|
| `create-drop` | Creates tables on start, drops on stop (H2 dev) |
| `update` | Updates schema without losing data (PostgreSQL) |
| `validate` | Validates schema, throws error if mismatch |
| `none` | Does nothing |

### Entity Relationships
```java
// User — one user has many tasks
@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference
private List<Task> tasks = new ArrayList<>();
 
// Task — many tasks belong to one user
@ManyToOne
@JoinColumn(name = "user_id")
@JsonBackReference
private User owner;
```

### Spring Data JPA — Derived Queries
```java
// Spring generates SQL automatically from method names
List<Task> findAllByOwner(User owner);
List<Task> findAllByOwnerAndStatus(User owner, Status status);
List<Task> findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
Optional<User> findByEmail(String email);
boolean existsUserByEmail(String email);
```

### Enums
```java
public enum Status { PENDING, IN_PROGRESS, DONE }
public enum Priority { LOW, MEDIUM, HIGH }
 
// In entity — always use STRING, never ORDINAL
@Enumerated(EnumType.STRING)
private Status status;
```
 
---

## Phase 4 — REST API Best Practices

### Core Concepts Learned
- **Global Exception Handling** — one place handles all errors
- **Custom Exceptions** — meaningful error messages
- **Input Validation** — reject bad data before it hits the database
- **DTOs** — separate classes for request input and response output
### Custom Exceptions
```java
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("User not found with id: " + id);
    }
}
 
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID id) {
        super("Task not found with id: " + id);
    }
}
 
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}
```

### Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
 
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(404).body(
            new ErrorResponse(404, ex.getMessage(), request.getRequestURI())
        );
    }
 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.status(400).body(
            new ErrorResponse(400, errors, request.getRequestURI())
        );
    }
}
```

### Validation Annotations
| Annotation | Use for |
|---|---|
| `@NotNull` | Field can't be null (any type) |
| `@NotBlank` | String can't be null or empty |
| `@NotEmpty` | List/String can't be null or empty |
| `@Email` | Must be valid email format |
| `@Size(min, max)` | String/collection size range |
| `@Min(n)` / `@Max(n)` | Number range |
| `@Pattern(regexp)` | Must match regex |

### DTO Pattern
```
Client → [Request DTO] → Controller → Service → Repository → DB
Client ← [Response DTO] ← Controller ← Service ← Repository ← DB
```

**Why DTOs?**
- Never expose passwords or sensitive fields in responses
- Decouple your DB structure from your API contract
- Different DTOs for create vs update operations
### Separate Create vs Update DTOs
```java
// UserCreateRequest — has email (can be set on creation)
// UserUpdateRequest — no email (cannot be changed after creation)
```
 
---

## Phase 5 — Security

### Core Concepts Learned
- **Spring Security** — controls who can access what
- **JJWT** — creates and validates JWT tokens
- **BCrypt** — password hashing algorithm
- **Stateless authentication** — server stores no session, token carries identity
- **Filter chain** — requests pass through multiple filters before reaching controller
- **`UserDetails`** — Spring Security interface your User entity implements
- **Roles** — define what a user is allowed to do
### How JWT Authentication Works
```
1. User registers   → POST /auth/register → saved to DB, token returned
2. User logs in     → POST /auth/login    → receives JWT token
3. User makes request → Authorization: Bearer <token> → server validates → allows/denies
```

### JWT vs Session Authentication
| | JWT (Stateless) | Session (Stateful) |
|---|---|---|
| Server stores | Nothing | Session in memory |
| Client stores | Token | Session cookie |
| Scales across servers | ✅ Yes | ❌ Hard |
| REST API friendly | ✅ Yes | ❌ No |

### Filter Chain
```
HTTP Request
     ↓
JwtAuthFilter        ← validates JWT token (your custom filter)
     ↓
AuthorizationFilter  ← checks permissions (Spring Security)
     ↓
Controller           ← handles request
     ↓
HTTP Response
```

### Why CSRF is Disabled
CSRF attacks only work with cookie-based auth. Since JWT is sent manually in headers (not cookies), CSRF attacks don't apply — safe to disable for REST APIs.

### `SecurityConfig`
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### Role-Based Access Control
```java
public enum Role { ROLE_USER, ROLE_ADMIN }
 
// Fine-grained control on methods
@GetMapping
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public ResponseEntity<List<UserResponse>> findAll() { ... }
```

### `UserDetails` Methods
| Method | Purpose |
|---|---|
| `getUsername()` | Returns unique identifier (use email) |
| `getPassword()` | Returns hashed password |
| `getAuthorities()` | Returns user roles |
| `isAccountNonExpired()` | Account expiry logic |
| `isAccountNonLocked()` | Ban/lock logic |
| `isCredentialsNonExpired()` | Password expiry logic |
| `isEnabled()` | Email verification logic |
 
---

## Annotations Reference

### Spring Core
| Annotation | Purpose |
|---|---|
| `@Component` | Generic Spring-managed bean |
| `@Service` | Business logic layer bean |
| `@Repository` | Data access layer bean |
| `@RestController` | HTTP layer bean (returns JSON) |
| `@Configuration` | Config class with `@Bean` methods |
| `@Bean` | Registers a method's return value as a bean |
| `@Autowired` | Injects a bean (prefer constructor injection) |
| `@Value("${prop}")` | Injects a value from `application.properties` |

### Spring Web
| Annotation | Purpose |
|---|---|
| `@RequestMapping("/path")` | Base path for a controller |
| `@GetMapping` | HTTP GET endpoint |
| `@PostMapping` | HTTP POST endpoint |
| `@PutMapping` | HTTP PUT endpoint |
| `@PatchMapping` | HTTP PATCH endpoint |
| `@DeleteMapping` | HTTP DELETE endpoint |
| `@RequestBody` | Reads JSON body from request |
| `@PathVariable` | Reads variable from URL path (`/{id}`) |
| `@RequestParam` | Reads query parameter (`?keyword=abc`) |

### JPA / Hibernate
| Annotation | Purpose |
|---|---|
| `@Entity` | Marks class as a DB table |
| `@Table(name="")` | Customizes table name |
| `@Id` | Marks field as primary key |
| `@GeneratedValue` | Auto-generates ID values |
| `@Column` | Customizes column (nullable, unique, name) |
| `@OneToMany` | One-to-many relationship |
| `@ManyToOne` | Many-to-one relationship |
| `@JoinColumn` | Defines the foreign key column |
| `@Enumerated(EnumType.STRING)` | Stores enum as string in DB |

### Validation
| Annotation | Purpose |
|---|---|
| `@Valid` | Triggers validation on a method parameter |
| `@NotNull` | Field cannot be null |
| `@NotBlank` | String cannot be null or empty |
| `@Email` | Must be valid email format |
| `@Size(min, max)` | Size constraints |

### Security
| Annotation | Purpose |
|---|---|
| `@EnableWebSecurity` | Enables Spring Security |
| `@EnableMethodSecurity` | Enables `@PreAuthorize` on methods |
| `@PreAuthorize` | Role/permission check on a method |

### JSON
| Annotation | Purpose |
|---|---|
| `@JsonManagedReference` | Parent side of relationship (serialized) |
| `@JsonBackReference` | Child side of relationship (not serialized) |
| `@JsonIgnore` | Excludes field from JSON output |

### Exception Handling
| Annotation | Purpose |
|---|---|
| `@RestControllerAdvice` | Global exception handler class |
| `@ExceptionHandler` | Handles a specific exception type |
 
---

## Key Concepts & Keywords

| Concept | Meaning |
|---|---|
| **IoC** | Inversion of Control — Spring controls object lifecycle |
| **DI** | Dependency Injection — Spring injects dependencies |
| **Bean** | An object managed by the Spring container |
| **Filter** | Intercepts requests before they reach the controller (like middleware) |
| **Filter Chain** | Multiple filters executed in order |
| **JWT** | JSON Web Token — stateless authentication token |
| **BCrypt** | Password hashing algorithm |
| **DTO** | Data Transfer Object — separates API from DB structure |
| **ORM** | Object Relational Mapping — maps Java classes to DB tables |
| **JPA** | Java Persistence API — standard ORM specification |
| **Hibernate** | JPA implementation used by Spring Boot |
| **Derived Query** | Repository method Spring auto-implements from its name |
| **Cascade** | Operations on parent automatically applied to children |
| **orphanRemoval** | Deletes child records no longer linked to parent |
| **CSRF** | Cross-Site Request Forgery attack (disabled for JWT APIs) |
| **Stateless** | Server stores no session — token carries all identity info |
| **`permitAll()`** | Endpoint accessible without authentication |
| **`authenticated()`** | Endpoint requires valid JWT token |
| **`@PreAuthorize`** | Method-level role/permission check |
 
---

## Project Structure

```
com.yourname.task_manager_api/
├── auth/
│   ├── AuthController.java
│   ├── AuthService.java
│   └── dto/
│       ├── RegisterRequest.java
│       ├── LoginRequest.java
│       └── AuthResponse.java
├── security/
│   ├── JwtUtil.java
│   ├── JwtAuthFilter.java
│   ├── SecurityConfig.java
│   ├── SwaggerConfig.java
│   └── CustomUserDetailsService.java
├── users/
│   ├── UserController.java
│   ├── UserService.java
│   └── UserRepository.java
├── tasks/
│   ├── TaskController.java
│   ├── TaskService.java
│   └── TaskRepository.java
├── model/
│   ├── User.java
│   ├── Task.java
│   ├── Role.java (enum)
│   ├── Status.java (enum)
│   └── Priority.java (enum)
├── dto/
│   ├── user/
│   │   ├── UserCreateRequest.java
│   │   ├── UserUpdateRequest.java
│   │   └── UserResponse.java
│   └── task/
│       ├── TaskRequest.java
│       └── TaskResponse.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ErrorResponse.java
│   ├── UserNotFoundException.java
│   ├── TaskNotFoundException.java
│   └── EmailAlreadyExistsException.java
└── TaskManagerApiApplication.java
```
 
---

## API Endpoints

### Auth (Public)
| Method | URL | Description |
|---|---|---|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Login and receive JWT token |

### Users (Authenticated)
| Method | URL | Role | Description |
|---|---|---|---|
| GET | `/api/users` | ADMIN | Get all users |
| GET | `/api/users/{id}` | USER | Get one user |
| PUT | `/api/users/{id}` | USER | Update username/password |
| DELETE | `/api/users/{id}` | ADMIN | Delete a user |

### Tasks (Authenticated)
| Method | URL | Description |
|---|---|---|
| POST | `/api/users/{userId}/tasks` | Create a task for a user |
| GET | `/api/users/{userId}/tasks` | Get all tasks of a user |
| PATCH | `/api/tasks/{taskId}` | Update a task |
| DELETE | `/api/tasks/{taskId}` | Delete a task |
| GET | `/api/tasks/search?keyword=` | Search tasks by title or description |
 
---

## How to Run

### Prerequisites
- Java 17 or 21
- Maven
- Docker
### 1. Start PostgreSQL with Docker
```bash
docker run --name task-manager-db -e POSTGRES_DB=taskmanager -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin123 -p 5432:5432 -d postgres
```

### 2. Configure `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanager
spring.datasource.username=admin
spring.datasource.password=admin123
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000
springdoc.api-docs.enabled=true
springdoc.swagger-ui.path=/swagger-ui/index.html
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

### 4. Access Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

### 5. Test the API
```http
### Register
POST http://localhost:8080/auth/register
Content-Type: application/json
 
{
  "username": "john",
  "email": "john@email.com",
  "password": "password123"
}
 
### Login
POST http://localhost:8080/auth/login
Content-Type: application/json
 
{
  "email": "john@email.com",
  "password": "password123"
}
 
### Use token on protected endpoints
GET http://localhost:8080/api/users
Authorization: Bearer paste-token-here
```
 
---

## Dependencies (`pom.xml`)

```xml
<!-- Spring Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
 
<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
 
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
 
<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
 
<!-- PostgreSQL -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
 
<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
 
<!-- Swagger UI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.6</version>
</dependency>
 
<!-- DevTools -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```
 
---

## What's Next — Remaining Phases

| Phase | Topics |
|---|---|
| Phase 6 | Unit tests (JUnit 5 + Mockito), Controller tests (MockMvc), Integration tests |
| Phase 7 | Caching (Redis), Async processing, Docker Compose, Microservices intro |
| Capstone | Full deployment to Railway/Render with CI/CD via GitHub Actions |
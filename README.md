# 📦 Box Dispatch Service

A Spring Boot-based backend service for managing drone dispatch boxes.  
It handles box registration, item management, weight limits, and battery capacity checks.

---

## 🚀 Features
- Register and manage boxes.
- Assign items to boxes.
- Validate weight limits and battery levels.
- RESTful API endpoints for CRUD operations.
- PostgreSQL integration.
- Postgres database (for testing).
- Built with Java 17 and Spring Boot 3.

---

## 🛠️ Tech Stack
- **Java 17
- **Spring Boot 3**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Maven**

---

## 🧰 Prerequisites

Ensure you have the following installed:
- [Java 17]
- [Maven 3.8+]
- [PostgreSQL 15+]

---

## 🧩 Database Setup (PostgreSQL)

### 1️⃣ Start PostgreSQL
Open your terminal and access the Postgres shell:

```bash
psql -U postgres
```

### 2️⃣ Create a new database:
```sql
CREATE DATABASE box_dispatch_db;
```

### 3️⃣ Create a user and assign privileges:
```sql
CREATE USER box_user WITH PASSWORD 'box_password';
GRANT ALL PRIVILEGES ON DATABASE box_dispatch_db TO box_user;
```

### 4️⃣ Verify connection:
```bash
psql -U box_user -d box_dispatch_db
```


##  Run and Test the Project

### 1️⃣ Clone the repository:
```bash
git clone https://github.com/stephenprin/box_dispatch_service-.git
cd box_dispatch_service-
```

### 2️⃣ Build the project:
```bash
mvn clean install
```

### 3️⃣ Run the application:
```bash
mvn spring-boot:run
```



### 4️⃣ Verify it’s running:
Open your browser or Postman and visit:
```
http://localhost:8080
```

---

##  Example API Usage

### ✅ Create a Box
**POST** `/api/v1/boxes`

**Request Body:**
```json
{
  "weightLimit": 400.0,
  "batteryCapacity": 80,
  "state": "IDLE"
}
```

**Response:**
```json
{
  "id": " c3be5bd8-e17d-4a8e-860d-8c664e6033c2",
  "txref": "BOX-0001",
  "weightLimit": 400.0,
  "batteryCapacity": 80,
  "state": "IDLE"
}
```

---

##  Running Tests

Run all tests using:
```bash
mvn test
```

---

##  Developer Notes

- The `txref` (transaction reference) is auto-generated in the format `BOX-0001`, `BOX-0002`, etc.
- Each box can hold multiple items, but cannot exceed the defined weight limit.
- `@JsonProperty(access = JsonProperty.Access.READ_ONLY)` ensures certain fields like `txref` are not modified by client input.



## Design Assumptions

Modular and Layered Architecture
I assumed a clean, layered Spring-Boot architecture 
with Controller → DTO → Service → Repository → Entity separation. 
This promotes testability, maintainability, and scalability while
keeping each layer focused on a single responsibility.

Database Design
I assumed PostgreSQL as the primary production database for robust 
relational consistency, ACID compliance, and 
support for advanced features like UUID generation

Validation & Error Handling
I assumed input validation belongs exclusively on DTOs using @Valid, 
@NotBlank, @Pattern, and @Positive. Entity-level annotations are limited to
database constraints only (@Column(nullable=false), unique=true).

Default and Fallback Behaviors
I assumed sensible defaults:
batteryCapacity = 100 if not provided
state = IDLE on creation
ITEM-0001, ITEM-0002... generated server-side using AtomicLong
This ensures predictable behavior, prevents null issues, and protects 
against client-side tampering.

All write operations are @Transactional with a single save() at method end 
to prevent partial state (e.g., box stuck in LOADING)



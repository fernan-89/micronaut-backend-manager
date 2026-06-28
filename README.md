# 🚀 User Management API

## 📖 Overview
The **User Management API** is a high-performance, production-grade microservice developed with **Micronaut** and **MongoDB**. It manages the full lifecycle of user identities, ensuring strict data integrity, operational state control, and comprehensive audit traceability.

## 🏗 Architectural Pillars
* **Centralized Audit Architecture**: Log history is decoupled from the user entity and stored in a dedicated `logs-management` collection. This prevents performance degradation on user read operations and ensures immutable audit trails[cite: 1].
* **High-Fidelity Tracking**: Every modification records a detailed "from/to" (De/Para) structure, enabling precise reconstruction of state changes[cite: 1].
* **Resilient State Management**: Explicit tracking of active/inactive states with full field-level delta calculations.
* **Contract-First Approach**: Input validation using Jakarta Bean Validation ensures that only valid data enters the business logic layer.

## 🛠 Tech Stack
* **Framework**: Micronaut 4.x
* **Language**: Java 21
* **Database**: MongoDB (via Micronaut Data)
* **Serialization**: Micronaut Serde (JSON)
* **Documentation**: OpenAPI/Swagger

---

## 🚀 Getting Started

### Prerequisites
* Java 21 SDK
* MongoDB Instance (running locally or via Docker)
* Gradle 8.x

### Setup
1. **Clone the repository:**
   ```bash
   git clone <your-repository-url>

```

2. **Configure Connection:**
   Update `src/main/resources/application.yml` with your MongoDB URI:
```yaml
mongodb:
  uri: "mongodb://localhost:27017/user-db"

```


3. **Build & Run:**
```bash
./gradlew clean build
./gradlew run

```



---

## 🔑 API Endpoints Reference

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/users` | Creates a new user and initializes the audit trail. |
| `GET` | `/users/{userHash}` | Retrieves current user profile details. |
| `GET` | `/users/{userHash}/logs` | Fetches historical changes (audit trail) for the user. |
| `GET` | `/users/search` | Searches users by email, login, or phone number. |
| `PUT` | `/users/{userHash}` | Performs a partial update and logs the modifications. |
| `DELETE` | `/users/{userHash}` | Permanently removes the user record. |

---

## 📝 Audit Strategy

Our audit strategy relies on the `AuditLogModel`, which captures changes as a map of `ChangeDetail` objects.

### Change Log Structure

```json
"changes": {
    "active": {
        "from": "true",
        "to": "false"
    }
}

```

* **Why?**: This structure allows front-end dashboards to easily render visual "De/Para" diffs without processing complex strings.

---

## 💡 Best Practices for Contributors

1. **Header Requirement**: Always provide the `X-Executor` header on `PUT` requests to identify the entity performing the change.
2. **Partial Updates**: The API handles nulls gracefully. Only send the fields you wish to modify.
3. **Documentation**: Maintain all new public methods and classes with Javadoc comments in English to ensure clarity and consistency across the team.

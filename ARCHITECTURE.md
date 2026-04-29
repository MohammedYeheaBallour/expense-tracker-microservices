# Architecture & Design Documentation

## System Architecture Overview

This document describes the architectural design of the Expense Tracker Microservices System.

## Table of Contents
- [System Architecture](#system-architecture)
- [Microservice Breakdown](#microservice-breakdown)
- [Communication Patterns](#communication-patterns)
- [Data Model](#data-model)
- [Design Patterns](#design-patterns)
- [Scalability & Resilience](#scalability--resilience)

---

## System Architecture

### High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Applications                       │
│                     (Web, Mobile, Desktop)                       │
└──────────────────┬──────────────────┬────────────────────────────┘
                   │                  │
        REST API   │                  │  GraphQL API
                   ▼                  ▼
    ┌──────────────────────┐   ┌─────────────────────┐
    │  API Gateway Layer   │   │  Dashboard Service  │
    │  (Optional - Future) │   │   (Port 8006)       │
    └──────────────────────┘   └─────────────────────┘
                 │                       │
    ┌────────────┼───────────┬───────────┼──────────────┐
    │            │           │           │              │
    ▼            ▼           ▼           ▼              ▼
┌─────────┐ ┌────────┐ ┌──────────┐ ┌────────┐  ┌──────────────┐
│ Income  │ │Expense │ │ Category │ │ Budget │  │ AI Recommend │
│Service  │ │Service │ │ Service  │ │Service │  │ Service      │
│(8001)   │ │(8002)  │ │ (8003)   │ │(8004)  │  │ (gRPC 9005)  │
└────┬────┘ └────┬───┘ └──────────┘ └───┬────┘  └──────┬───────┘
     │           │                       │              │
     └─────┬─────┴───────────────────────┴──────────────┘
           │                 │
    Kafka Events    gRPC Calls
    (Event Bus)     (High-perf)
           │                 │
    ┌──────▼────────┐       │
    │  Kafka Broker │       │
    │ + Zookeeper   │       │
    │  (Docker)     │       │
    └───────────────┘       │
                            │
    H2 Databases (Per Service)
    ┌──────────────────────────────────────┐
    │ Income | Expense | Category | Budget │
    │   DB      DB        DB         DB    │
    └──────────────────────────────────────┘
```

### Deployment Architecture

```
Development/Local:
  ├── Docker Compose
  │   ├── Kafka
  │   ├── Zookeeper
  │
  ├── Spring Boot Services (JVM processes)
  │   ├── Income Service (localhost:8001)
  │   ├── Expense Service (localhost:8002)
  │   ├── Category Service (localhost:8003)
  │   ├── Budget Service (localhost:8004)
  │   ├── AI Recommendation Service (localhost:8005, gRPC:9005)
  │   └── Dashboard Service (localhost:8006)
```

---

## Microservice Breakdown

### 1. Income Service
**Responsibilities:**
- Manage income records
- Publish income events

**Key Components:**
```
IncomeService (Business Logic)
  ├── IncomeRepository (Data Access)
  │   └── Income Entity
  ├── IncomeController (REST Endpoints)
  ├── IncomeEvent (Kafka Event)
  └── KafkaConfig (Producer Configuration)
```

**REST Endpoints:**
- `POST /api/incomes` - Create income
- `GET /api/incomes` - List all
- `GET /api/incomes/{id}` - Get by ID
- `GET /api/incomes/user/{userId}` - Get by user
- `PUT /api/incomes/{id}` - Update
- `DELETE /api/incomes/{id}` - Delete

**Event Publishing:**
- Topic: `income-events`
- Trigger: On successful income creation
- Consumer: Dashboard Service

### 2. Expense Service
**Responsibilities:**
- Manage expense records
- Validate categories (REST → Category Service)
- Publish expense events

**Key Components:**
```
ExpenseService (Business Logic + Category Validation)
  ├── ExpenseRepository (Data Access)
  │   └── Expense Entity
  ├── ExpenseController (REST Endpoints)
  ├── CategoryServiceClient (REST Client)
  ├── ExpenseEvent (Kafka Event)
  └── KafkaConfig (Producer Configuration)
```

**REST Endpoints:**
- `POST /api/expenses` - Create (validates category)
- `GET /api/expenses` - List all
- `GET /api/expenses/{id}` - Get by ID
- `GET /api/expenses/user/{userId}` - Get by user
- `GET /api/expenses/user/{userId}/category/{category}` - Get by category
- `PUT /api/expenses/{id}` - Update
- `DELETE /api/expenses/{id}` - Delete

**Dependencies:**
- Category Service (HTTP/REST) - Synchronous validation

**Event Publishing:**
- Topic: `expense-events`
- Trigger: On successful expense creation (after validation)
- Consumer: Dashboard Service

### 3. Category Service
**Responsibilities:**
- Manage predefined categories
- Provide category validation

**Key Components:**
```
CategoryService (Business Logic)
  ├── CategoryRepository (Data Access)
  │   └── Category Entity
  └── CategoryController (REST Endpoints + Validation)
```

**REST Endpoints:**
- `POST /api/categories` - Create
- `GET /api/categories` - List all
- `GET /api/categories/{id}` - Get by ID
- `GET /api/categories/name/{name}` - Get by name
- `GET /api/categories/validate/{name}` - Validate (used by Expense Service)
- `PUT /api/categories/{id}` - Update
- `DELETE /api/categories/{id}` - Delete

**No External Dependencies**

### 4. Budget Service
**Responsibilities:**
- Manage monthly budgets
- Call AI Recommendation Service (gRPC)

**Key Components:**
```
BudgetService (Business Logic)
  ├── BudgetRepository (Data Access)
  │   └── Budget Entity
  ├── BudgetController (REST Endpoints)
  ├── AIRecommendationClient (gRPC Client)
  └── gRPC Configuration
```

**REST Endpoints:**
- `POST /api/budgets` - Create
- `GET /api/budgets` - List all
- `GET /api/budgets/{id}` - Get by ID
- `GET /api/budgets/user/{userId}` - Get by user
- `GET /api/budgets/user/{userId}/month/{month}` - Get by month
- `PUT /api/budgets/{id}` - Update
- `DELETE /api/budgets/{id}` - Delete

**Dependencies:**
- AI Recommendation Service (gRPC) - Asynchronous recommendation calls

### 5. AI Recommendation Service
**Responsibilities:**
- Generate spending recommendations
- Provide budget analysis

**Key Components:**
```
AIRecommendationService (Business Logic)
  ├── RecommendationService (Algorithms)
  ├── AIRecommendationGrpcService (gRPC Server)
  └── gRPC Configuration
```

**gRPC Service:**
- `GetRecommendation(RecommendationRequest)` → `RecommendationResponse`

**gRPC Proto Definition:**
```protobuf
service AIRecommendation {
  rpc GetRecommendation (RecommendationRequest) 
      returns (RecommendationResponse) {}
}

message RecommendationRequest {
  string user_id = 1;
  double monthly_budget = 2;
  double current_expenses = 3;
}

message RecommendationResponse {
  string user_id = 1;
  string advice = 2;
  double spending_percentage = 3;
  bool is_within_budget = 4;
}
```

**No REST API** (gRPC only)

### 6. Dashboard Service
**Responsibilities:**
- Aggregate financial data
- Provide GraphQL API
- Consume Kafka events

**Key Components:**
```
DashboardService (Business Logic + In-Memory Store)
  ├── IncomeEventListener (Kafka Consumer)
  ├── ExpenseEventListener (Kafka Consumer)
  ├── DashboardResolver (GraphQL Resolver)
  ├── DashboardDTO (Data Transfer Objects)
  └── GraphQL Schema Configuration
```

**GraphQL Schema:**
```graphql
type Query {
  financialOverview(userId: String!, month: String!): FinancialOverview
  summary(userId: String!): Summary
}

type FinancialOverview {
  userId: String!
  month: String!
  totalIncome: Float!
  totalExpenses: Float!
  monthlyBudget: Float!
  remainingBudget: Float!
  expensesByCategory: [CategoryExpense!]!
  incomesBySource: [SourceIncome!]!
}
```

**Kafka Consumers:**
- Topic: `income-events` - Consume income events
- Topic: `expense-events` - Consume expense events
- Group ID: `dashboard-service-group`

---

## Communication Patterns

### 1. REST (Synchronous, HTTP-based)

**Usage:** Expense Service → Category Service

```
Expense Service                 Category Service
    │                                  │
    ├─ POST /expenses ────────────────►│
    │  (Validates category)            │
    │                                  │
    │◄─────── isValid response ────────┤
    │                                  │
    └─ Save expense (if valid)         │
```

**Characteristics:**
- Blocking, synchronous calls
- Strong consistency
- Request-response pattern
- Error handling with HTTP status codes
- Low latency (localhost connection)

### 2. Kafka (Asynchronous, Event-driven)

**Usage:** Income Service → Dashboard Service, Expense Service → Dashboard Service

```
Income Service                 Kafka Broker                Dashboard Service
    │                            │                              │
    ├─ Publish IncomeCreated ───►│                              │
    │  (Income Event)            │                              │
    │                            │ IncomeEventListener          │
    │                            ├─ Consumes event ────────────►│
    │                            │  (Updates cache)             │
    │                            │                              │
    └─ Service continues         └─ Broker stores event        │
       (Non-blocking)
```

**Characteristics:**
- Non-blocking, asynchronous
- Event-driven architecture
- Eventual consistency
- Loose coupling
- Scalable (multiple consumers possible)
- Durability (events persisted in Kafka)

### 3. gRPC (High-performance RPC)

**Usage:** Budget Service → AI Recommendation Service

```
Budget Service                 AI Recommendation Service
    │                                  │
    ├─ gRPC Call ─────────────────────►│
    │  RecommendationRequest           │
    │  (HTTP/2, Protocol Buffers)      │
    │                                  │
    │◄─ RecommendationResponse ────────┤
    │  (Binary encoded)                │
    │                                  │
    └─ Return to client                │
```

**Characteristics:**
- High performance (HTTP/2, binary protocol)
- Type-safe (Protocol Buffers)
- Strongly typed contracts
- Streaming support (not used here)
- Blocking or async operations

### 4. GraphQL (Flexible querying)

**Usage:** Client Applications → Dashboard Service

```
Client Application             Dashboard Service
    │                                  │
    ├─ GraphQL Query ──────────────────►│
    │  (Exactly what's needed)         │
    │                                  │
    │◄─ GraphQL Response ───────────────┤
    │  (JSON, requested fields only)    │
    │                                  │
    └─ Use data directly               │
```

**Characteristics:**
- Client specifies exact data needed
- Single endpoint
- No over-fetching
- No under-fetching
- Strong typing
- Playground for exploration

---

## Data Model

### Entity Relationships

```
Income Entity
├── id: Long (PK)
├── userId: String
├── amount: BigDecimal
├── source: String
├── date: LocalDate
└── createdAt: LocalDate

Expense Entity
├── id: Long (PK)
├── userId: String
├── amount: BigDecimal
├── category: String (FK → Category)
├── date: LocalDate
├── note: String
└── createdAt: LocalDate

Category Entity
├── id: Long (PK)
├── name: String (Unique)
└── description: String

Budget Entity
├── id: Long (PK)
├── userId: String
├── month: String (format: YYYY-MM)
├── totalLimit: BigDecimal
└── Composite index (userId, month)

Dashboard Cache (In-Memory)
├── userId:month → {
│   ├── totalIncome
│   ├── totalExpenses
│   ├── monthlyBudget
│   ├── expensesByCategory: Map
│   └── incomesBySource: Map
│ }
```

### Database Schema (H2 Default)

**Incomes Table:**
```sql
CREATE TABLE incomes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(255) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    source VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    created_at DATE NOT NULL
);

CREATE INDEX idx_incomes_user_id ON incomes(user_id);
```

**Expenses Table:**
```sql
CREATE TABLE expenses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(255) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    category VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    note VARCHAR(500),
    created_at DATE NOT NULL
);

CREATE INDEX idx_expenses_user_id ON expenses(user_id);
CREATE INDEX idx_expenses_user_category ON expenses(user_id, category);
```

**Categories Table:**
```sql
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500)
);
```

**Budgets Table:**
```sql
CREATE TABLE budgets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(255) NOT NULL,
    month VARCHAR(7) NOT NULL,
    total_limit DECIMAL(19,2) NOT NULL
);

CREATE UNIQUE INDEX idx_budgets_user_month ON budgets(user_id, month);
```

---

## Design Patterns

### 1. Microservices Pattern
- Each service owns its database
- No shared data stores
- Independent deployment
- Clear boundaries and responsibilities

### 2. Event Sourcing (Partial)
- Income and Expense events published to Kafka
- Dashboard reconstructs state from events
- Potential for event replay

### 3. Service Locator Pattern
- Category Service: Located by URL (hardcoded)
- AI Recommendation Service: Located by gRPC config

### 4. Data Transfer Object (DTO)
- DashboardDTO, CategoryExpenseDTO, SourceIncomeDTO
- Separation of API contracts from domain model
- Flexibility for API evolution

### 5. Repository Pattern
- JpaRepository for data access abstraction
- Clean separation of persistence concerns
- Testability improvement

### 6. Dependency Injection
- Spring Framework handles all DI
- Loose coupling between components
- Easy mocking for tests

---

## Scalability & Resilience

### Current State (Development/Demo)

**Scaling Limitations:**
1. Hardcoded service URLs (no service discovery)
2. Single Kafka broker (single point of failure)
3. In-memory state in Dashboard Service
4. No circuit breakers or retry logic
5. No load balancing

### Production Improvements

**Recommended Changes:**

1. **Service Discovery**
   - Eureka or Consul
   - Automatic service registration
   - Dynamic URL resolution

2. **API Gateway**
   - Spring Cloud Gateway
   - Centralized routing
   - Rate limiting
   - Authentication

3. **Distributed Caching**
   - Redis instead of in-memory
   - Shared cache across Dashboard instances
   - Cache invalidation strategies

4. **Fault Tolerance**
   - Resilience4j for circuit breakers
   - Retry logic with exponential backoff
   - Timeouts on external calls

5. **Kafka Setup**
   - Multiple brokers (3-5 recommended)
   - Replication factor: 3
   - Multiple consumer group instances

6. **Database**
   - Switch from H2 to PostgreSQL/MySQL
   - Connection pooling
   - Read replicas
   - Backup strategies

7. **Monitoring**
   - Prometheus metrics
   - Grafana dashboards
   - ELK Stack for logging
   - Distributed tracing (Jaeger)

### Load Distribution

```
Production Setup:
┌─────────────────────────────────────┐
│     API Gateway / Load Balancer     │
└──────────────┬──────────────────────┘
               │
    ┌──────────┴──────────┬─────────────┬──────────┐
    ▼                     ▼             ▼          ▼
┌─────────┐          ┌─────────┐  ┌─────────┐ ┌────────┐
│ Income  │ x N      │Expense  │  │Category │ │Budget  │
│Service  │ replicas │Service  │  │Service  │ │Service │
└─────────┘          └─────────┘  └─────────┘ └────────┘
    │                    │             │          │
    └────────────────────┼─────────────┴──────────┘
                         │
                    ┌────▼────┐
                    │  Kafka  │ (Cluster)
                    │ (Multi  │
                    │  Broker)│
                    └─────────┘
```

### Resilience Strategies

**REST Calls (Expense → Category):**
```java
// Circuit Breaker Pattern
@CircuitBreaker(name = "categoryService")
@Retry(name = "categoryService")
public boolean validateCategory(String categoryName) {
    return categoryServiceClient.validateCategory(categoryName);
}
```

**Kafka Consumers:**
```yaml
spring:
  kafka:
    consumer:
      max-poll-records: 100
      session-timeout-ms: 30000
      max-poll-interval-ms: 300000
```

**gRPC Calls (Budget → AI Recommendation):**
```yaml
grpc:
  client:
    aiRecommendationService:
      address: localhost:9005
      negotiation-type: plaintext
      deadline-ms: 5000
```

---

## Security Considerations

### Current State
- No authentication/authorization
- No SSL/TLS
- No API key validation
- CORS enabled for all origins (development only)

### Recommended Production Security

1. **Authentication**
   - JWT tokens
   - OAuth 2.0
   - API keys for service-to-service

2. **Authorization**
   - Role-based access control (RBAC)
   - Resource-level permissions

3. **Encryption**
   - TLS for all inter-service communication
   - Encryption at rest for sensitive data
   - Secure key management

4. **API Security**
   - Rate limiting
   - Request validation
   - SQL injection prevention
   - CORS restrictions

5. **Network Security**
   - VPC isolation
   - Firewall rules
   - DDoS protection

---

## Deployment Topology

### Docker Deployment
```
Docker Compose:
├── Zookeeper Container
├── Kafka Container
├── Service Network (bridge)
│   ├── Income Service Container
│   ├── Expense Service Container
│   ├── Category Service Container
│   ├── Budget Service Container
│   ├── AI Recommendation Service Container
│   └── Dashboard Service Container
```

### Kubernetes Deployment (Future)
```
Kubernetes Cluster:
├── Namespace: expense-tracker
│   ├── Deployment: income-service (replicas: 3)
│   ├── Deployment: expense-service (replicas: 3)
│   ├── Deployment: category-service (replicas: 2)
│   ├── Deployment: budget-service (replicas: 2)
│   ├── Deployment: ai-recommendation-service (replicas: 2)
│   ├── Deployment: dashboard-service (replicas: 3)
│   ├── StatefulSet: Kafka (replicas: 3)
│   ├── Service: Kafka ClusterIP
│   └── Ingress: API Gateway
```

---

## Cost Optimization

**Development/Testing:**
- In-memory databases (H2)
- Single container Kafka
- Minimal resource allocation

**Production:**
- Cloud-managed Kafka (AWS MSK, Azure Event Hubs)
- Managed database (RDS, Azure SQL)
- Auto-scaling service groups
- Reserved instances for baseline capacity

---

## References

- [Microservices Patterns](https://microservices.io/)
- [Spring Microservices Guide](https://spring.io/projects/spring-cloud)
- [Kafka Architecture](https://kafka.apache.org/documentation/#design)
- [gRPC Performance Best Practices](https://grpc.io/docs/guides/performance/)
- [GraphQL Best Practices](https://graphql.org/learn/best-practices/)


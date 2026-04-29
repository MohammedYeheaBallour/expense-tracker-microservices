# Expense Tracker Microservices System

## Overview

This is an academic project demonstrating a microservices architecture for an Expense Tracker system. It implements multiple communication patterns including REST APIs, Kafka event streaming, gRPC, and GraphQL.

**Course:** Advanced Software Engineering (Chapter 5: Implementing Microservice Communication)

## Architecture

### Microservices

1. **Income Service** (Port 8001)
   - Manages income records
   - REST API: CRUD operations for income
   - Publishes `IncomeCreated` events to Kafka

2. **Expense Service** (Port 8002)
   - Manages expense records
   - REST API: CRUD operations for expenses
   - Calls Category Service (REST) for validation
   - Publishes `ExpenseAdded` events to Kafka

3. **Category Service** (Port 8003)
   - Manages predefined categories
   - REST API: List and validate categories
   - No dependencies on other services

4. **Budget Service** (Port 8004)
   - Manages monthly budgets per user
   - REST API: CRUD for budgets
   - Calls AI Recommendation Service via gRPC

5. **AI Recommendation Service** (Port 8005)
   - Provides spending recommendations
   - gRPC service (port 9005)
   - Receives budget data and returns advice

6. **Dashboard Service** (Port 8006)
   - Provides combined financial overview
   - GraphQL API for flexible querying
   - Subscribes to Income and Expense events from Kafka
   - Aggregates data in-memory for demo purposes

### Communication Patterns

```
REST (Synchronous):
  Expense Service → Category Service
  Dashboard Service → Other Services (for initial data)

Kafka (Asynchronous Event Streaming):
  Income Service → IncomeCreated events
  Expense Service → ExpenseAdded events
  Dashboard Service ← Subscribe to events

gRPC (High-Performance RPC):
  Budget Service → AI Recommendation Service (port 9005)

GraphQL (Flexible Querying):
  Dashboard Service → GraphQL endpoint for clients
```

## Project Structure

```
expense-tracker-system/
├── pom.xml                          # Parent POM
├── README.md                        # This file
├── docker-compose.yml               # Kafka/Zookeeper setup
│
├── income-service/
│   ├── pom.xml
│   ├── src/main/java/com/expensetracker/income/
│   │   ├── IncomeServiceApplication.java
│   │   ├── entity/Income.java
│   │   ├── repository/IncomeRepository.java
│   │   ├── service/IncomeService.java
│   │   ├── controller/IncomeController.java
│   │   └── event/IncomeEvent.java
│   └── src/main/resources/application.yml
│
├── expense-service/
│   ├── pom.xml
│   ├── src/main/java/com/expensetracker/expense/
│   │   ├── ExpenseServiceApplication.java
│   │   ├── entity/Expense.java
│   │   ├── repository/ExpenseRepository.java
│   │   ├── service/ExpenseService.java
│   │   ├── controller/ExpenseController.java
│   │   ├── client/CategoryServiceClient.java
│   │   ├── event/ExpenseEvent.java
│   │   └── config/KafkaConfig.java
│   └── src/main/resources/application.yml
│
├── category-service/
│   ├── pom.xml
│   ├── src/main/java/com/expensetracker/category/
│   │   ├── CategoryServiceApplication.java
│   │   ├── entity/Category.java
│   │   ├── repository/CategoryRepository.java
│   │   ├── service/CategoryService.java
│   │   └── controller/CategoryController.java
│   └── src/main/resources/application.yml
│
├── budget-service/
│   ├── pom.xml
│   ├── src/main/java/com/expensetracker/budget/
│   │   ├── BudgetServiceApplication.java
│   │   ├── entity/Budget.java
│   │   ├── repository/BudgetRepository.java
│   │   ├── service/BudgetService.java
│   │   ├── controller/BudgetController.java
│   │   └── grpc/AIRecommendationClient.java
│   ├── src/main/proto/
│   │   └── ai_recommendation.proto
│   └── src/main/resources/application.yml
│
├── ai-recommendation-service/
│   ├── pom.xml
│   ├── src/main/java/com/expensetracker/airecommendation/
│   │   ├── AIRecommendationServiceApplication.java
│   │   ├── grpc/AIRecommendationGrpcService.java
│   │   └── service/RecommendationService.java
│   ├── src/main/proto/
│   │   └── ai_recommendation.proto
│   └── src/main/resources/application.yml
│
└── dashboard-service/
    ├── pom.xml
    ├── src/main/java/com/expensetracker/dashboard/
    │   ├── DashboardServiceApplication.java
    │   ├── graphql/
    │   │   ├── DashboardResolver.java
    │   │   └── DashboardDTO.java
    │   ├── service/DashboardService.java
    │   ├── listener/
    │   │   ├── IncomeEventListener.java
    │   │   └── ExpenseEventListener.java
    │   └── config/KafkaConfig.java
    ├── src/main/resources/
    │   ├── graphql/
    │   │   └── schema.graphqls
    │   └── application.yml
    └── README_GraphQL.md
```

## Prerequisites

- **Java 17** or higher
- **Maven 3.8.0** or higher
- **Docker** and **Docker Compose** (for running Kafka)
- **Git**

## Quick Start

### 1. Start Kafka (Prerequisites for Event Streaming)

```bash
# Navigate to project root
cd expense-tracker-system

# Start Kafka and Zookeeper using Docker Compose
docker-compose up -d
```

This starts:
- Kafka broker on `localhost:9092`
- Zookeeper on `localhost:2181`

### 2. Build All Services

```bash
# From project root
mvn clean install -DskipTests
```

### 3. Run Services (in separate terminal windows/tabs)

**Terminal 1 - Income Service:**
```bash
cd income-service
mvn spring-boot:run
# Service running on http://localhost:8001
```

**Terminal 2 - Category Service:**
```bash
cd category-service
mvn spring-boot:run
# Service running on http://localhost:8003
```

**Terminal 3 - Expense Service:**
```bash
cd expense-service
mvn spring-boot:run
# Service running on http://localhost:8002
# Depends on Category Service for validation
```

**Terminal 4 - Budget Service:**
```bash
cd budget-service
mvn spring-boot:run
# Service running on http://localhost:8004
# gRPC port: 9004
```

**Terminal 5 - AI Recommendation Service:**
```bash
cd ai-recommendation-service
mvn spring-boot:run
# Service running on http://localhost:8005
# gRPC port: 9005
```

**Terminal 6 - Dashboard Service:**
```bash
cd dashboard-service
mvn spring-boot:run
# Service running on http://localhost:8006
# GraphQL endpoint: http://localhost:8006/graphql
```

## Testing Endpoints

### 1. REST APIs

#### Category Service

```bash
# Create a category
curl -X POST http://localhost:8003/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Food","description":"Food and dining"}'

# List categories
curl http://localhost:8003/api/categories

# Validate category
curl http://localhost:8003/api/categories/validate/Food
```

#### Income Service

```bash
# Create income
curl -X POST http://localhost:8001/api/incomes \
  -H "Content-Type: application/json" \
  -d '{"userId":"user123","amount":5000,"source":"Salary","date":"2024-04-29"}'

# Get all incomes
curl http://localhost:8001/api/incomes

# Get income by user
curl http://localhost:8001/api/incomes/user/user123
```

#### Expense Service

```bash
# Create expense
curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{"userId":"user123","amount":50,"category":"Food","date":"2024-04-29","note":"Lunch"}'

# Get all expenses
curl http://localhost:8002/api/expenses

# Get expenses by user
curl http://localhost:8002/api/expenses/user/user123
```

#### Budget Service

```bash
# Create budget
curl -X POST http://localhost:8004/api/budgets \
  -H "Content-Type: application/json" \
  -d '{"userId":"user123","month":"2024-04","totalLimit":2000}'

# Get all budgets
curl http://localhost:8004/api/budgets

# Get budget by user and month
curl http://localhost:8004/api/budgets/user/user123/month/2024-04
```

### 2. GraphQL Queries

Access GraphQL playground: http://localhost:8006/graphql

```graphql
# Get financial overview for a user
query {
  financialOverview(userId: "user123", month: "2024-04") {
    userId
    month
    totalIncome
    totalExpenses
    monthlyBudget
    remainingBudget
    expensesByCategory {
      category
      amount
    }
    incomesBySource {
      source
      amount
    }
  }
}

# Get summary
query {
  summary(userId: "user123") {
    totalIncome
    totalExpenses
    netBalance
  }
}
```

### 3. gRPC Testing

Use `grpcurl` to test gRPC endpoints:

```bash
# Install grpcurl if not already installed
# brew install grpcurl (macOS)
# or download from https://github.com/fullstorydev/grpcurl/releases

# Get recommendation (Budget Service → AI Recommendation Service)
grpcurl -plaintext \
  -d '{"userId":"user123","monthlyBudget":2000,"currentExpenses":1500}' \
  localhost:9005 \
  com.expensetracker.AIRecommendation/GetRecommendation
```

## Key Features Demonstrated

 **Microservice Communication Patterns:**
- Synchronous REST calls (Expense → Category)
- Asynchronous Kafka events (Income/Expense → Dashboard)
- gRPC for high-performance calls (Budget → AI Recommendation)
- GraphQL for flexible querying

**Event-Driven Architecture:**
- Kafka topics for `income-events` and `expense-events`
- Event publishing and consuming
- Real-time data aggregation

**Database Layer:**
- Spring Data JPA
- H2 in-memory database (easily switchable to MySQL)
- Automatic schema generation

**API Documentation:**
- RESTful endpoints with request/response examples
- GraphQL schema with resolvers
- gRPC proto definitions

## Implementation Notes

1. **In-Memory Event Processing:** Dashboard Service stores data in memory for simplicity. In production, use a distributed cache or database.

2. **Security:** This project does not include security (authentication/authorization) for simplicity. Add Spring Security in production.

3. **Circuit Breakers:** Consider adding Resilience4j for fault tolerance in production.

4. **Service Discovery:** Uses hard-coded URLs. In production, use Eureka or Kubernetes service discovery.

5. **Logging & Monitoring:** Add Spring Boot Actuator, Prometheus, and Grafana for monitoring.

6. **Configuration:** Uses application.yml. Extend with Spring Cloud Config Server for centralized configuration.

## Code Quality

- Clean architecture with separation of concerns
- Entity, Repository, Service, Controller layers
- Dependency injection via Spring
- RESTful API design
- Event-driven patterns
- Type-safe gRPC communication
- Schema-driven GraphQL

## Files

- **6 microservices** as separate Maven modules
- **gRPC proto definitions** for type-safe communication
- **GraphQL schema** for flexible querying
- **Kafka configuration** for event streaming
- **Docker Compose** for infrastructure
- **Example REST, gRPC, and GraphQL requests**

## Troubleshooting

**Kafka Connection Issues:**
- Ensure Docker containers are running: `docker ps`
- Check Kafka connectivity: `docker logs <kafka-container-id>`

**Service Port Conflicts:**
- Change ports in `application.yml` of each service

**Build Failures:**
- Ensure Java 17+ is installed: `java -version`
- Clean Maven cache: `mvn clean`

**gRPC Issues:**
- Verify gRPC port (9005) is accessible
- Check protocol: gRPC uses HTTP/2

## References

- [Spring Boot 3.x Documentation](https://spring.io/projects/spring-boot)
- [Apache Kafka Documentation](https://kafka.apache.org/)
- [gRPC Java Guide](https://grpc.io/docs/languages/java/)
- [Spring GraphQL Documentation](https://spring.io/projects/spring-graphql)
- [Microservices Patterns](https://microservices.io/)

## Author

Created by Mohammed Yehea Ballour



# API Examples & Testing Guide

This document provides comprehensive examples for testing all microservices APIs.

## Table of Contents
- [REST API Examples](#rest-api-examples)
- [gRPC Examples](#grpc-examples)
- [GraphQL Examples](#graphql-examples)
- [Event-Driven Workflow](#event-driven-workflow)
- [Testing Scenarios](#testing-scenarios)

---

## REST API Examples

All REST APIs return JSON responses with appropriate HTTP status codes.

### 1. Category Service (Port 8003)

#### Create Category
```bash
curl -X POST http://localhost:8003/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Food",
    "description":"Food and dining expenses"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Food",
  "description": "Food and dining expenses"
}
```

#### Create More Categories
```bash
curl -X POST http://localhost:8003/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Transport","description":"Transportation costs"}'

curl -X POST http://localhost:8003/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Utilities","description":"Bills and utilities"}'

curl -X POST http://localhost:8003/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Entertainment","description":"Entertainment and hobbies"}'
```

#### List All Categories
```bash
curl http://localhost:8003/api/categories
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Food",
    "description": "Food and dining expenses"
  },
  {
    "id": 2,
    "name": "Transport",
    "description": "Transportation costs"
  }
]
```

#### Validate Category
```bash
curl http://localhost:8003/api/categories/validate/Food
```

**Response (Valid):**
```json
{
  "category": "Food",
  "isValid": true
}
```

**Response (Invalid):**
```json
{
  "category": "InvalidCategory",
  "isValid": false
}
```

---

### 2. Income Service (Port 8001)

#### Create Income
```bash
curl -X POST http://localhost:8001/api/incomes \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":5000.00,
    "source":"Salary",
    "date":"2024-04-29"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "userId": "user123",
  "amount": 5000.00,
  "source": "Salary",
  "date": "2024-04-29",
  "createdAt": "2024-04-29"
}
```

#### Create Additional Income
```bash
curl -X POST http://localhost:8001/api/incomes \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":500.00,
    "source":"Freelance",
    "date":"2024-04-25"
  }'
```

#### List All Incomes
```bash
curl http://localhost:8001/api/incomes
```

**Response:**
```json
[
  {
    "id": 1,
    "userId": "user123",
    "amount": 5000.00,
    "source": "Salary",
    "date": "2024-04-29",
    "createdAt": "2024-04-29"
  }
]
```

#### Get Incomes by User
```bash
curl http://localhost:8001/api/incomes/user/user123
```

#### Update Income
```bash
curl -X PUT http://localhost:8001/api/incomes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":5200.00,
    "source":"Salary",
    "date":"2024-04-29"
  }'
```

#### Delete Income
```bash
curl -X DELETE http://localhost:8001/api/incomes/1
```

---

### 3. Expense Service (Port 8002)

**Important:** Expense Service validates categories with Category Service. Create categories first!

#### Create Expense (Valid Category)
```bash
curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":50.00,
    "category":"Food",
    "date":"2024-04-29",
    "note":"Lunch at cafe"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "userId": "user123",
  "amount": 50.00,
  "category": "Food",
  "date": "2024-04-29",
  "note": "Lunch at cafe",
  "createdAt": "2024-04-29"
}
```

#### Create Expense (Invalid Category)
```bash
curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":100.00,
    "category":"InvalidCategory",
    "date":"2024-04-29",
    "note":"Test"
  }'
```

**Response (400 Bad Request):**
```
Invalid category: InvalidCategory
```

#### Create More Expenses
```bash
curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":30.00,
    "category":"Transport",
    "date":"2024-04-28",
    "note":"Bus ticket"
  }'

curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":100.00,
    "category":"Utilities",
    "date":"2024-04-27",
    "note":"Electricity bill"
  }'

curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user456",
    "amount":25.00,
    "category":"Food",
    "date":"2024-04-29",
    "note":"Dinner"
  }'
```

#### List All Expenses
```bash
curl http://localhost:8002/api/expenses
```

#### Get Expenses by User
```bash
curl http://localhost:8002/api/expenses/user/user123
```

#### Get Expenses by User and Category
```bash
curl http://localhost:8002/api/expenses/user/user123/category/Food
```

#### Update Expense
```bash
curl -X PUT http://localhost:8002/api/expenses/1 \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":60.00,
    "category":"Food",
    "date":"2024-04-29",
    "note":"Lunch and coffee"
  }'
```

#### Delete Expense
```bash
curl -X DELETE http://localhost:8002/api/expenses/1
```

---

### 4. Budget Service (Port 8004)

#### Create Budget
```bash
curl -X POST http://localhost:8004/api/budgets \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "month":"2024-04",
    "totalLimit":2000.00
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "userId": "user123",
  "month": "2024-04",
  "totalLimit": 2000.00
}
```

#### Create Budget for Another User
```bash
curl -X POST http://localhost:8004/api/budgets \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user456",
    "month":"2024-04",
    "totalLimit":3000.00
  }'
```

#### List All Budgets
```bash
curl http://localhost:8004/api/budgets
```

#### Get Budgets by User
```bash
curl http://localhost:8004/api/budgets/user/user123
```

#### Get Budget by User and Month
```bash
curl http://localhost:8004/api/budgets/user/user123/month/2024-04
```

**Response:**
```json
{
  "id": 1,
  "userId": "user123",
  "month": "2024-04",
  "totalLimit": 2000.00
}
```

#### Update Budget
```bash
curl -X PUT http://localhost:8004/api/budgets/1 \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "month":"2024-04",
    "totalLimit":2500.00
  }'
```

#### Delete Budget
```bash
curl -X DELETE http://localhost:8004/api/budgets/1
```

---

## gRPC Examples

### Using grpcurl

Install grpcurl first:
```bash
# macOS
brew install grpcurl

# Linux
# Download from https://github.com/fullstorydev/grpcurl/releases
```

### Get AI Recommendation

Test the gRPC call from Budget Service to AI Recommendation Service:

```bash
# Request when within budget
grpcurl -plaintext \
  -d '{"userId":"user123","monthly_budget":2000,"current_expenses":1500}' \
  localhost:9005 \
  com.expensetracker.AIRecommendation/GetRecommendation
```

**Response:**
```json
{
  "userId": "user123",
  "advice": "GOOD: You are spending well within your budget (75.00%). Good financial discipline!",
  "spendingPercentage": 75,
  "isWithinBudget": true
}
```

#### Test: Warning (High Spending)
```bash
grpcurl -plaintext \
  -d '{"userId":"user123","monthly_budget":2000,"current_expenses":1800}' \
  localhost:9005 \
  com.expensetracker.AIRecommendation/GetRecommendation
```

**Response:**
```json
{
  "userId": "user123",
  "advice": "WARNING: You are close to your budget limit (90.00%). Consider reducing expenses.",
  "spendingPercentage": 90,
  "isWithinBudget": true
}
```

#### Test: Over Budget
```bash
grpcurl -plaintext \
  -d '{"userId":"user123","monthly_budget":2000,"current_expenses":2500}' \
  localhost:9005 \
  com.expensetracker.AIRecommendation/GetRecommendation
```

**Response:**
```json
{
  "userId": "user123",
  "advice": "ALERT: You have exceeded your budget! Current spending is 125.00% of your budget.",
  "spendingPercentage": 125,
  "isWithinBudget": false
}
```

---

## GraphQL Examples

### Access GraphQL Playground
Open browser: http://localhost:8006/graphql

### Query: Financial Overview

```graphql
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
```

**Response:**
```json
{
  "data": {
    "financialOverview": {
      "userId": "user123",
      "month": "2024-04",
      "totalIncome": 5500,
      "totalExpenses": 180,
      "monthlyBudget": 2000,
      "remainingBudget": 1820,
      "expensesByCategory": [
        {
          "category": "Food",
          "amount": 50
        },
        {
          "category": "Transport",
          "amount": 30
        },
        {
          "category": "Utilities",
          "amount": 100
        }
      ],
      "incomesBySource": [
        {
          "source": "Salary",
          "amount": 5000
        },
        {
          "source": "Freelance",
          "amount": 500
        }
      ]
    }
  }
}
```

### Query: Summary

```graphql
query {
  summary(userId: "user123") {
    totalIncome
    totalExpenses
    netBalance
  }
}
```

**Response:**
```json
{
  "data": {
    "summary": {
      "totalIncome": 5500,
      "totalExpenses": 180,
      "netBalance": 5320
    }
  }
}
```

---

## Event-Driven Workflow

### Complete Workflow Demonstrating Event Publishing and Consumption

#### 1. Create Categories
```bash
for cat in "Food" "Transport" "Utilities" "Entertainment" "Healthcare"; do
  curl -X POST http://localhost:8003/api/categories \
    -H "Content-Type: application/json" \
    -d "{\"name\":\"$cat\",\"description\":\"$cat expenses\"}"
done
```

#### 2. Create Income (Published to Kafka)
```bash
curl -X POST http://localhost:8001/api/incomes \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":5000.00,
    "source":"Salary",
    "date":"2024-04-29"
  }'
```

**Dashboard Service** subscribes to `income-events` and receives this event automatically.

#### 3. Create Expenses (Published to Kafka)
```bash
curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":50.00,
    "category":"Food",
    "date":"2024-04-29",
    "note":"Lunch"
  }'

curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":30.00,
    "category":"Transport",
    "date":"2024-04-29",
    "note":"Bus"
  }'
```

**Dashboard Service** subscribes to `expense-events` and receives these events.

#### 4. Create Budget
```bash
curl -X POST http://localhost:8004/api/budgets \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "month":"2024-04",
    "totalLimit":2000.00
  }'
```

#### 5. Query Dashboard via GraphQL
```graphql
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
```

The dashboard has aggregated data from:
- Income events (via Kafka)
- Expense events (via Kafka)  
- Budget (stored in service)

---

## Testing Scenarios

### Scenario 1: Typical User Month

**Setup:**
1. Create categories
2. Create income for user
3. Create multiple expenses
4. Create budget

**Test:**
```bash
# See financial overview via GraphQL
curl -X POST http://localhost:8006/graphql \
  -H "Content-Type: application/json" \
  -d '{
    "query": "{ financialOverview(userId: \"user123\", month: \"2024-04\") { userId month totalIncome totalExpenses } }"
  }'
```

### Scenario 2: Category Validation

**Test invalid category rejection:**
```bash
curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":50.00,
    "category":"NonExistentCategory",
    "date":"2024-04-29"
  }'
# Should return 400 Bad Request
```

### Scenario 3: gRPC Budget Recommendations

**Budget within limits:**
```bash
grpcurl -plaintext \
  -d '{"userId":"user123","monthly_budget":2000,"current_expenses":500}' \
  localhost:9005 \
  com.expensetracker.AIRecommendation/GetRecommendation
```

**Budget over limit:**
```bash
grpcurl -plaintext \
  -d '{"userId":"user123","monthly_budget":2000,"current_expenses":2500}' \
  localhost:9005 \
  com.expensetracker.AIRecommendation/GetRecommendation
```

### Scenario 4: Multi-User Isolation

```bash
# Create separate records for user456
curl -X POST http://localhost:8001/api/incomes \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user456",
    "amount":3000.00,
    "source":"Salary",
    "date":"2024-04-29"
  }'

curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user456",
    "amount":100.00,
    "category":"Food",
    "date":"2024-04-29"
  }'

# Get separate summaries
curl http://localhost:8001/api/incomes/user/user456
curl http://localhost:8002/api/expenses/user/user456
```

---

## Error Handling Examples

### 400 Bad Request - Invalid Category
```
POST /api/expenses with category="InvalidCategory"
Response: "Invalid category: InvalidCategory"
```

### 404 Not Found
```
GET /api/incomes/999
Response: 404 Not Found
```

### 409 Conflict (if implemented)
```
POST /api/categories with duplicate name
Response: 409 Conflict
```

---

## Performance Testing Tips

1. **Load Test REST API:**
```bash
# Using Apache Bench
ab -n 1000 -c 10 http://localhost:8003/api/categories
```

2. **Monitor Kafka:**
```bash
docker exec <kafka-container> kafka-console-consumer \
  --topic income-events \
  --from-beginning \
  --bootstrap-server localhost:9092
```

3. **Check gRPC Performance:**
```bash
# Multiple rapid gRPC calls
for i in {1..100}; do
  grpcurl -plaintext \
    -d '{"userId":"user123","monthly_budget":2000,"current_expenses":1500}' \
    localhost:9005 \
    com.expensetracker.AIRecommendation/GetRecommendation
done
```

---

## Troubleshooting

**Connection Issues:**
- Verify all services are running on correct ports
- Check: `netstat -tuln | grep LISTEN` (Linux/Mac) or `netstat -ano` (Windows)
- Verify Kafka is running: `docker ps | grep kafka`

**Kafka Event Not Appearing in Dashboard:**
- Check Dashboard Service logs for Kafka connection errors
- Verify topic names match: `income-events` and `expense-events`
- Ensure Kafka consumer group is correct: `dashboard-service-group`

**gRPC Connection Error:**
- Verify AI Recommendation Service is running on port 9005
- Check Budget Service grpc client configuration
- Test with: `grpcurl -plaintext localhost:9005 list`

**GraphQL Query Error:**
- Verify Dashboard Service has consumed events from Kafka
- Wait a few seconds after creating income/expenses before querying
- Check GraphQL schema in `dashboard-service/src/main/resources/graphql/schema.graphqls`


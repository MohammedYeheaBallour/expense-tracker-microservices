# GraphQL API Documentation

## Overview

The Dashboard Service provides a GraphQL API for querying financial overview and summary data. GraphQL enables flexible, efficient data fetching with the exact fields needed.

## Accessing GraphQL

### GraphQL Playground
Browse to: http://localhost:8006/graphql

The GraphQL Playground provides an interactive IDE with:
- Query editor
- Real-time documentation
- Query history
- Variables panel

## Schema Definition

### Root Query Type

```graphql
type Query {
    financialOverview(userId: String!, month: String!): FinancialOverview
    summary(userId: String!): Summary
}
```

### Object Types

```graphql
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

type CategoryExpense {
    category: String!
    amount: Float!
}

type SourceIncome {
    source: String!
    amount: Float!
}

type Summary {
    totalIncome: Float!
    totalExpenses: Float!
    netBalance: Float!
}
```

## Query Examples

### Query 1: Full Financial Overview

**Use Case:** Get complete financial picture for a user in a specific month

```graphql
query GetFinancialOverview {
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

**Expected Response:**
```json
{
  "data": {
    "financialOverview": {
      "userId": "user123",
      "month": "2024-04",
      "totalIncome": 5500.0,
      "totalExpenses": 180.0,
      "monthlyBudget": 2000.0,
      "remainingBudget": 1820.0,
      "expensesByCategory": [
        {
          "category": "Food",
          "amount": 50.0
        },
        {
          "category": "Transport",
          "amount": 30.0
        },
        {
          "category": "Utilities",
          "amount": 100.0
        }
      ],
      "incomesBySource": [
        {
          "source": "Salary",
          "amount": 5000.0
        },
        {
          "source": "Freelance",
          "amount": 500.0
        }
      ]
    }
  }
}
```

### Query 2: Expenses Only

**Use Case:** Get only expense breakdown by category

```graphql
query GetExpenses {
  financialOverview(userId: "user123", month: "2024-04") {
    expensesByCategory {
      category
      amount
    }
  }
}
```

### Query 3: Budget Status

**Use Case:** Check if user is within budget

```graphql
query CheckBudgetStatus {
  financialOverview(userId: "user123", month: "2024-04") {
    month
    monthlyBudget
    totalExpenses
    remainingBudget
  }
}
```

### Query 4: Income Sources

**Use Case:** Analyze income breakdown

```graphql
query GetIncomeSources {
  financialOverview(userId: "user123", month: "2024-04") {
    totalIncome
    incomesBySource {
      source
      amount
    }
  }
}
```

### Query 5: User Summary

**Use Case:** Get overall financial health

```graphql
query GetSummary {
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
      "totalIncome": 5500.0,
      "totalExpenses": 180.0,
      "netBalance": 5320.0
    }
  }
}
```

## Using Variables

GraphQL supports variables for better query reusability.

### Query with Variables

```graphql
query GetFinancialData($userId: String!, $month: String!) {
  financialOverview(userId: $userId, month: $month) {
    userId
    month
    totalIncome
    totalExpenses
    monthlyBudget
    remainingBudget
  }
}
```

### Variables JSON

```json
{
  "userId": "user123",
  "month": "2024-04"
}
```

## cURL Examples

### Query via cURL

```bash
curl -X POST http://localhost:8006/graphql \
  -H "Content-Type: application/json" \
  -d '{
    "query": "{ financialOverview(userId: \"user123\", month: \"2024-04\") { totalIncome totalExpenses monthlyBudget remainingBudget } }"
  }'
```

### Query with Variables via cURL

```bash
curl -X POST http://localhost:8006/graphql \
  -H "Content-Type: application/json" \
  -d '{
    "query": "query GetFinancialData($userId: String!, $month: String!) { financialOverview(userId: $userId, month: $month) { totalIncome totalExpenses } }",
    "variables": {
      "userId": "user123",
      "month": "2024-04"
    }
  }'
```

## Real-World Use Cases

### Use Case 1: Dashboard Widget - Budget Alert

```graphql
query BudgetAlert {
  financialOverview(userId: "user123", month: "2024-04") {
    monthlyBudget
    totalExpenses
    remainingBudget
  }
}
```

Display warning if `remainingBudget < 0.2 * monthlyBudget` (20% left)

### Use Case 2: Category Analytics

```graphql
query CategorySpending {
  financialOverview(userId: "user123", month: "2024-04") {
    expensesByCategory {
      category
      amount
    }
  }
}
```

Sort by amount to show top spending categories

### Use Case 3: Multi-Month Comparison

Query multiple months in sequence:

```graphql
query MultiMonthComparison {
  march: financialOverview(userId: "user123", month: "2024-03") {
    month
    totalExpenses
  }
  april: financialOverview(userId: "user123", month: "2024-04") {
    month
    totalExpenses
  }
}
```

### Use Case 4: Income vs Expense Ratio

```graphql
query IncomeExpenseRatio {
  financialOverview(userId: "user123", month: "2024-04") {
    totalIncome
    totalExpenses
  }
}
```

Calculate: `expenseRatio = (totalExpenses / totalIncome) * 100`

## Error Handling

### Invalid User
```graphql
query {
  financialOverview(userId: "nonexistent", month: "2024-04") {
    totalIncome
  }
}
```

**Response:** Returns object with zero values (no error, graceful degradation)

### Missing Required Arguments
```graphql
query {
  financialOverview(userId: "user123") {
    totalIncome
  }
}
```

**Response:**
```json
{
  "errors": [
    {
      "message": "Field argument 'month' of type 'String!' is required and was not provided",
      "extensions": {
        "classification": "ValidationError"
      }
    }
  ]
}
```

### Type Mismatch
```graphql
query {
  financialOverview(userId: 123, month: "2024-04") {
    totalIncome
  }
}
```

**Response:**
```json
{
  "errors": [
    {
      "message": "Argument 'userId' has coerced Null value for NonNull type 'String!'",
      "extensions": {
        "classification": "ValidationError"
      }
    }
  ]
}
```

## Best Practices

1. **Request Only Needed Fields**
   - Bad: Request all fields even if not needed
   - Good: Request specific fields matching your use case

2. **Use Aliases for Multiple Queries**
   ```graphql
   query {
     currentMonth: financialOverview(userId: "user123", month: "2024-04") {
       totalExpenses
     }
     previousMonth: financialOverview(userId: "user123", month: "2024-03") {
       totalExpenses
     }
   }
   ```

3. **Fragment Reuse**
   ```graphql
   fragment OverviewData on FinancialOverview {
     totalIncome
     totalExpenses
     monthlyBudget
     remainingBudget
   }
   
   query {
     financialOverview(userId: "user123", month: "2024-04") {
       ...OverviewData
     }
   }
   ```

4. **Cache Responses**
   - GraphQL responses are deterministic for same inputs
   - Cache based on userId + month combination

## Integration with Frontend

### React Example

```javascript
const query = `
  query GetFinancialOverview($userId: String!, $month: String!) {
    financialOverview(userId: $userId, month: $month) {
      totalIncome
      totalExpenses
      monthlyBudget
      remainingBudget
    }
  }
`;

fetch('http://localhost:8006/graphql', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    query,
    variables: { userId: 'user123', month: '2024-04' }
  })
})
  .then(res => res.json())
  .then(data => console.log(data.data.financialOverview))
  .catch(err => console.error(err));
```

### JavaScript Apollo Client

```javascript
import { ApolloClient, gql } from '@apollo/client';

const client = new ApolloClient({
  uri: 'http://localhost:8006/graphql'
});

const GET_OVERVIEW = gql`
  query GetOverview($userId: String!, $month: String!) {
    financialOverview(userId: $userId, month: $month) {
      totalIncome
      totalExpenses
      monthlyBudget
    }
  }
`;

client.query({
  query: GET_OVERVIEW,
  variables: { userId: 'user123', month: '2024-04' }
});
```

## Performance Considerations

1. **Batch Similar Queries**
   - Use aliases to get data for multiple months in one request
   
2. **Pagination** (Not currently implemented but recommended for production)
   - Implement pagination for large result sets
   
3. **Caching Strategy**
   - Cache financial data for the current month
   - Invalidate on new transactions
   - Archive old months for historical queries

4. **Query Complexity**
   - Current schema is simple; no depth limitations
   - Production: Implement query complexity analysis

## Monitoring & Debugging

### Enable GraphQL Logging

Check Spring Boot logs for:
```
GraphQL query execution time
Number of fields requested
Query execution statistics
```

### GraphQL Playground Tools

- **Documentation Explorer:** Click docs icon to see schema
- **Query History:** Previous queries are saved
- **Network Tab:** Monitor request/response in browser dev tools

## API Versioning

Current version: `1.0.0`

Future versions may include:
- Pagination
- Sorting
- Filtering
- Subscriptions for real-time updates


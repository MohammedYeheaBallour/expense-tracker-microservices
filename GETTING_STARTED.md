# Getting Started Guide

Quick reference for setting up and running the Expense Tracker Microservices System.

## Prerequisites

- **Java 17+** - [Download](https://www.oracle.com/java/technologies/downloads/#java17)
- **Maven 3.8.0+** - [Download](https://maven.apache.org/download.cgi)
- **Docker & Docker Compose** - [Download](https://www.docker.com/products/docker-desktop)
- **Git** - [Download](https://git-scm.com/)
- **cURL** (for testing) - Usually pre-installed

**Verify installations:**
```bash
java -version          # Should show Java 17+
mvn -version           # Should show Maven 3.8+
docker --version       # Should show Docker version
docker-compose --version
```

---

## Project Structure

```
expense-tracker-system/
├── pom.xml                          # Parent POM
├── README.md                        # Main documentation
├── ARCHITECTURE.md                  # Architecture guide
├── API_EXAMPLES.md                  # API examples & testing
├── docker-compose.yml               # Kafka setup
├── income-service/                  # Service modules
├── expense-service/
├── category-service/
├── budget-service/
├── ai-recommendation-service/
└── dashboard-service/
```

---

## Step 1: Clone/Setup Project

```bash
# Option A: If already in project directory
cd expense-tracker-system

# Option B: Clone from GitHub (when available)
git clone <repository-url>
cd expense-tracker-system
```

---

## Step 2: Start Infrastructure (Kafka)

**Terminal 1 - Start Kafka:**

```bash
# Start Kafka and Zookeeper
docker-compose up -d

# Verify containers are running
docker ps
# Should see: confluentinc/cp-kafka and confluentinc/cp-zookeeper containers

# View logs (optional)
docker-compose logs -f
```

**Expected output:**
```
Creating expense-tracker-system_zookeeper_1 ... done
Creating expense-tracker-system_kafka_1 ... done
```

**Troubleshooting:**
```bash
# Stop and restart
docker-compose down
docker-compose up -d

# Check Kafka health
docker exec <kafka-container> kafka-broker-api-versions --bootstrap-server localhost:9092
```

---

## Step 3: Build All Services

**Terminal 2 - Build:**

```bash
# From project root
mvn clean install -DskipTests

# This will:
# - Compile all 6 services
# - Generate gRPC code
# - Create JAR files
# - Take 2-5 minutes on first build
```

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: X m XX s
```

**Troubleshooting:**
```bash
# If build fails, clean cache
mvn clean

# Check Java version
java -version  # Should be 17+

# Check Maven settings
mvn -version

# Build single service for faster testing
mvn clean install -DskipTests -f category-service/pom.xml
```

---

## Step 4: Start Microservices

Open 6 new terminal windows (Terminal 3-8) and run services in this order:

**Terminal 3 - Category Service (Port 8003):**
```bash
cd category-service
mvn spring-boot:run
# Wait for: "Started CategoryServiceApplication"
```

**Terminal 4 - Income Service (Port 8001):**
```bash
cd income-service
mvn spring-boot:run
# Wait for: "Started IncomeServiceApplication"
```

**Terminal 5 - Expense Service (Port 8002):**
```bash
cd expense-service
mvn spring-boot:run
# Wait for: "Started ExpenseServiceApplication"
```

**Terminal 6 - AI Recommendation Service (Port 8005, gRPC 9005):**
```bash
cd ai-recommendation-service
mvn spring-boot:run
# Wait for: "Started AIRecommendationServiceApplication"
```

**Terminal 7 - Budget Service (Port 8004):**
```bash
cd budget-service
mvn spring-boot:run
# Wait for: "Started BudgetServiceApplication"
```

**Terminal 8 - Dashboard Service (Port 8006):**
```bash
cd dashboard-service
mvn spring-boot:run
# Wait for: "Started DashboardServiceApplication"
```

**All services running? You should see:**
```
- Income Service:           http://localhost:8001
- Expense Service:          http://localhost:8002
- Category Service:         http://localhost:8003
- Budget Service:           http://localhost:8004
- AI Recommendation (gRPC): localhost:9005
- Dashboard Service:        http://localhost:8006 (GraphQL)
```

---

## Step 5: Verify Services Health

**Terminal 9 - Test endpoints:**

```bash
# Check Category Service
curl http://localhost:8003/actuator/health
# Response: {"status":"UP"}

# Check all services
for port in 8001 8002 8003 8004 8005 8006; do
  echo "Port $port:"
  curl -s http://localhost:$port/actuator/health | jq .status
done
```

---

## Step 6: Quick Test Workflow

**Run this complete workflow to verify everything works:**

### 6A. Create Categories
```bash
# Create Food category
curl -X POST http://localhost:8003/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Food","description":"Food expenses"}'

# Create Transport category  
curl -X POST http://localhost:8003/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Transport","description":"Transport costs"}'

# Create Utilities category
curl -X POST http://localhost:8003/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Utilities","description":"Bills"}'

# Verify categories
curl http://localhost:8003/api/categories | jq .
```

### 6B. Create Income
```bash
curl -X POST http://localhost:8001/api/incomes \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":5000,
    "source":"Salary",
    "date":"2024-04-29"
  }' | jq .
```

### 6C. Create Expenses
```bash
# Expense 1
curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":50,
    "category":"Food",
    "date":"2024-04-29",
    "note":"Lunch"
  }' | jq .

# Expense 2
curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "amount":30,
    "category":"Transport",
    "date":"2024-04-29",
    "note":"Bus"
  }' | jq .
```

### 6D. Create Budget
```bash
curl -X POST http://localhost:8004/api/budgets \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"user123",
    "month":"2024-04",
    "totalLimit":2000
  }' | jq .
```

### 6E. Query Dashboard (GraphQL)
```bash
# Wait 2-3 seconds for events to be consumed by Dashboard Service

# Query via cURL
curl -X POST http://localhost:8006/graphql \
  -H "Content-Type: application/json" \
  -d '{
    "query": "{ financialOverview(userId: \"user123\", month: \"2024-04\") { totalIncome totalExpenses monthlyBudget remainingBudget } }"
  }' | jq .
```

**Expected response:**
```json
{
  "data": {
    "financialOverview": {
      "totalIncome": 5000,
      "totalExpenses": 80,
      "monthlyBudget": 2000,
      "remainingBudget": 1920
    }
  }
}
```

### 6F. Test gRPC (AI Recommendations)
```bash
# Install grpcurl if needed (macOS)
brew install grpcurl

# Test recommendation
grpcurl -plaintext \
  -d '{"userId":"user123","monthly_budget":2000,"current_expenses":100}' \
  localhost:9005 \
  com.expensetracker.AIRecommendation/GetRecommendation
```

**Expected response:**
```json
{
  "userId": "user123",
  "advice": "GOOD: You are spending well within your budget (5.00%). Good financial discipline!",
  "spendingPercentage": 5,
  "isWithinBudget": true
}
```

---

## Access Web Interfaces

Open these in your browser:

1. **GraphQL Playground** (Dashboard Service)
   - URL: http://localhost:8006/graphql
   - Interactive GraphQL IDE

2. **H2 Database Consoles** (Per service)
   - Income: http://localhost:8001/h2-console
   - Expense: http://localhost:8002/h2-console
   - Category: http://localhost:8003/h2-console
   - Budget: http://localhost:8004/h2-console
   - Dashboard: N/A (no database)

3. **Health Endpoints**
   - Income: http://localhost:8001/actuator/health
   - Expense: http://localhost:8002/actuator/health
   - Category: http://localhost:8003/actuator/health
   - Budget: http://localhost:8004/actuator/health
   - Dashboard: http://localhost:8006/actuator/health

---

## Testing REST APIs

Use the comprehensive guide: [API_EXAMPLES.md](API_EXAMPLES.md)

Quick test script:
```bash
#!/bin/bash

echo "=== Category Service ==="
curl -s http://localhost:8003/api/categories | jq '.[] | {id, name}'

echo -e "\n=== Income Service ==="
curl -s http://localhost:8001/api/incomes | jq '.[] | {id, userId, amount, source}'

echo -e "\n=== Expense Service ==="
curl -s http://localhost:8002/api/expenses | jq '.[] | {id, userId, amount, category}'

echo -e "\n=== Budget Service ==="
curl -s http://localhost:8004/api/budgets | jq '.[] | {id, userId, month, totalLimit}'
```

Save as `test.sh` and run:
```bash
chmod +x test.sh
./test.sh
```

---

## Stopping Services

### Stop Individual Services
Press `Ctrl+C` in each terminal window

### Stop All Services
```bash
# In project root directory
docker-compose down

# Or manually stop Docker containers
docker stop $(docker ps -q)
```

---

## Common Issues & Troubleshooting

### Issue: "Connection refused" on localhost:9092 (Kafka)

**Solution:**
```bash
# Verify Kafka is running
docker ps | grep kafka

# If not running, start it
docker-compose up -d

# Check Kafka logs
docker-compose logs kafka
```

### Issue: Service won't start on port XXXX

**Solution:**
```bash
# Check what's using the port (macOS/Linux)
lsof -i :8001

# Kill the process
kill -9 <PID>

# Or change the port in service's application.yml
# server:
#   port: 8011  (changed from 8001)
```

### Issue: "Cannot find symbol" during Maven build

**Solution:**
```bash
# Clean Maven cache
mvn clean

# Rebuild
mvn install -DskipTests
```

### Issue: gRPC connection refused

**Solution:**
```bash
# Verify AI Recommendation Service is running
curl http://localhost:8005/actuator/health

# Check gRPC port is listening
netstat -tuln | grep 9005 (Linux/Mac)
netstat -ano | findstr 9005 (Windows)

# Restart AI Recommendation Service
# (Ctrl+C in its terminal, then restart)
```

### Issue: GraphQL query returns empty data

**Solution:**
```bash
# Wait 2-3 seconds after creating data
# Dashboard Service needs time to consume Kafka events

# Check if events are in Kafka
docker exec <kafka-container> \
  kafka-console-consumer --topic income-events \
  --from-beginning --bootstrap-server localhost:9092

# Verify Dashboard Service is consuming
# Check logs: Should see "Income event processed" messages
```

### Issue: Categories not validating in Expense Service

**Solution:**
```bash
# Verify Category Service is running
curl http://localhost:8003/api/categories

# Create a category first
curl -X POST http://localhost:8003/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","description":"Test"}'

# Then create expense with that category
curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{"userId":"user1","amount":50,"category":"Test","date":"2024-04-29"}'
```

---

## Performance Tips

### Faster Development Builds
```bash
# Skip tests during build
mvn clean install -DskipTests

# Skip test compilation
mvn clean install -DskipTests -Dmaven.test.skip=true

# Build single service
mvn clean install -DskipTests -f expense-service/pom.xml
```

### Faster Service Startup
```bash
# Services start in this order (no dependencies):
# 1. Category Service (no deps)
# 2. Income Service (only Kafka, optional)
# 3. AI Recommendation Service (standalone)

# Slowest starters:
# 4. Expense Service (depends on Category)
# 5. Budget Service (depends on AI gRPC)
# 6. Dashboard Service (waits for Kafka)
```

### Monitor Kafka Topics
```bash
# Watch income events in real-time
docker exec <kafka-container> kafka-console-consumer \
  --topic income-events \
  --bootstrap-server localhost:9092 \
  --from-beginning

# In another terminal, watch expense events
docker exec <kafka-container> kafka-console-consumer \
  --topic expense-events \
  --bootstrap-server localhost:9092 \
  --from-beginning
```

---

## Next Steps

1. **Read Full Documentation:**
   - [README.md](README.md) - Overview and usage
   - [ARCHITECTURE.md](ARCHITECTURE.md) - System design
   - [API_EXAMPLES.md](API_EXAMPLES.md) - Complete API reference

2. **Explore the Code:**
   - Each service has clean separation of concerns
   - Study the communication patterns (REST, Kafka, gRPC, GraphQL)
   - Review entity and service layer design

3. **Extend the System:**
   - Add more microservices
   - Implement authentication/authorization
   - Add caching with Redis
   - Deploy to Docker/Kubernetes

4. **Production Deployment:**
   - Add circuit breakers (Resilience4j)
   - Implement service discovery (Eureka)
   - Setup distributed logging (ELK)
   - Configure monitoring (Prometheus/Grafana)

---

## Quick Reference Commands

```bash
# Build everything
mvn clean install -DskipTests

# Start Kafka
docker-compose up -d

# Check all services are healthy
for port in 8001 8002 8003 8004 8005 8006; do
  curl -s http://localhost:$port/actuator/health | jq .status
done

# View Kafka topics
docker exec <kafka> kafka-topics --list --bootstrap-server localhost:9092

# Stop everything
docker-compose down

# View Docker logs
docker-compose logs -f <service-name>
```

---

## Support & Resources

- **Spring Boot:** https://spring.io/projects/spring-boot
- **Kafka:** https://kafka.apache.org/documentation/
- **gRPC:** https://grpc.io/docs/
- **GraphQL:** https://graphql.org/learn/
- **Maven:** https://maven.apache.org/

---

**Created for Advanced Software Engineering Course - Chapter 5: Microservice Communication**

Version: 1.0.0 | Last Updated: April 2024


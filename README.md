# نظام تتبع النفقات - Expense Tracker Microservices System

## 🎯 ملخص المشروع

نظام متقدم لإدارة وتتبع النفقات والدخل مبني على **معمارية الخدمات الدقيقة (Microservices)** يدمج 4 تقنيات اتصال مختلفة:
- **REST APIs** (الاتصال المتزامن البسيط)
- **Kafka Event Streaming** (المراسلة غير المتزامنة)
- **gRPC** (الاتصال عالي الأداء)
- **GraphQL** (الاستعلام المرن عن البيانات)

**الحالة:** ✅ **مكتمل وجاهز للنشر**  
**الفئة المستهدفة:** طلاب الهندسة البرمجية - Advanced Software Engineering  
**المستوى:** مشروع أكاديمي متقدم

---




## 🏗️ المعمارية النظام

### الخدمات الدقيقة (Microservices)

```
┌─────────────────────────────────────────────────┐
│            تطبيقات العملاء                        │
│     (Web, Mobile, Desktop)                      │
└──────────────────┬──────────────────────────────┘
                   │
    ┌──────────────┴──────────────┐
    │                             │
    ▼                             ▼
┌────────────────────┐    ┌──────────────────────┐
│   خدمة الإدارة     │    │  Dashboard Service   │
│   (REST APIs)      │    │  (GraphQL)           │
└─────────┬──────────┘    └──────────────────────┘
          │                        │
    ┌─────┼─────┬────────┐        │
    │     │     │        │        │
    ▼     ▼     ▼        ▼        ▼
  Income Expense Category Budget  AI Service
  (8001) (8002) (8003)   (8004)  (9005 gRPC)
    
    └──────────────────────────────────┬─────────────┘
                 │
        ┌────────▼──────────┐
        │  Kafka Broker +   │
        │  Zookeeper        │
        │  (Docker)         │
        └─────────┬─────────┘
                 │
        ┌────────▼──────────┐
        │  Databases        │
        │  (H2 / MySQL)     │
        └───────────────────┘
```

### الخدمات الستة:

#### 1️⃣ **Income Service** (منفذ 8001)
- **الوظيفة:** إدارة سجلات الدخل
- **الاتصال:** 
  - REST API للعمليات الكاملة
  - ينشر أحداث `IncomeCreated` إلى Kafka
- **الـ API:**
  - `POST /api/incomes` - إضافة دخل جديد
  - `GET /api/incomes` - عرض جميع السجلات
  - `GET /api/incomes/{id}` - عرض سجل معين
  - `PUT /api/incomes/{id}` - تعديل سجل
  - `DELETE /api/incomes/{id}` - حذف سجل

#### 2️⃣ **Expense Service** (منفذ 8002)
- **الوظيفة:** إدارة سجلات النفقات
- **الاتصال:**
  - REST API للعمليات الكاملة
  - استدعاء REST إلى Category Service للتحقق من الفئات
  - ينشر أحداث `ExpenseAdded` إلى Kafka
- **الـ API:**
  - `POST /api/expenses` - إضافة نفقة جديدة
  - `GET /api/expenses` - عرض جميع النفقات
  - `GET /api/expenses/{id}` - عرض نفقة معينة
  - `PUT /api/expenses/{id}` - تعديل نفقة
  - `DELETE /api/expenses/{id}` - حذف نفقة

#### 3️⃣ **Category Service** (منفذ 8003)
- **الوظيفة:** إدارة الفئات المحددة مسبقاً
- **الاتصال:**
  - REST API فقط
  - بدون اعتماديات على خدمات أخرى
- **الـ API:**
  - `POST /api/categories` - إنشاء فئة جديدة
  - `GET /api/categories` - عرض جميع الفئات
  - `GET /api/categories/validate/{name}` - التحقق من صحة الفئة

#### 4️⃣ **Budget Service** (منفذ 8004)
- **الوظيفة:** إدارة الميزانيات الشهرية
- **الاتصال:**
  - REST API للعمليات الكاملة
  - استدعاء gRPC إلى AI Service للحصول على التوصيات
- **الـ API:**
  - `POST /api/budgets` - إضافة ميزانية جديدة
  - `GET /api/budgets` - عرض جميع الميزانيات
  - `GET /api/budgets/{id}` - عرض ميزانية معينة

#### 5️⃣ **AI Recommendation Service** (منفذ 9005 - gRPC)
- **الوظيفة:** توفير توصيات للإنفاق
- **الاتصال:**
  - gRPC Server فقط (بدون REST)
  - تحليل بيانات الميزانية والنفقات
- **الخدمة:**
  - `GetRecommendation` - تحليل الإنفاق وإعطاء نصائح
  - حساب نسبة الإنفاق
  - تحديد حالة الميزانية

#### 6️⃣ **Dashboard Service** (منفذ 8006)
- **الوظيفة:** لوحة تحكم موحدة للبيانات المالية
- **الاتصال:**
  - GraphQL API للاستعلامات المرنة
  - مستمع Kafka لأحداث الدخل والنفقات
- **الاستعلامات:**
  - `financialOverview(userId, month)` - نظرة عامة كاملة
  - `summary(userId)` - ملخص شامل

---

## 💻 المتطلبات الأساسية

```bash
# المتطلبات الإلزامية:
✓ Java 17 أو أحدث
✓ Maven 3.8.0 أو أحدث
✓ Docker و Docker Compose
✓ Git
✓ cURL (للاختبار)
```

### التحقق من التثبيت:
```bash
java -version          # يجب أن يظهر Java 17+
mvn -version           # يجب أن يظهر Maven 3.8+
docker --version       # يجب أن يظهر إصدار Docker
docker-compose --version
```

---

## 🚀 خطوات التشغيل السريع

### الخطوة 1: إعداد المشروع

```bash
# الانتقال إلى مجلد المشروع
cd expense-tracker-system

# أو استنساخ من GitHub (عند توفره)
git clone <repository-url>
cd expense-tracker-system
```

### الخطوة 2: تشغيل البنية التحتية (Kafka)

```bash
# تشغيل Kafka و Zookeeper
docker-compose up -d

# التحقق من أن الحاويات تعمل
docker ps

# الاطلاع على السجلات (اختياري)
docker-compose logs -f
```

### الخطوة 3: بناء جميع الخدمات

```bash
# من جذر المشروع
mvn clean install -DskipTests

# هذا سيقوم بـ:
# - ترجمة جميع 6 خدمات
# - توليد كود gRPC
# - إنشاء ملفات JAR
```

**مدة البناء:** 3-5 دقائق (حسب سرعة الإنترنت)

### الخطوة 4: تشغيل الخدمات

في محطات طرفية منفصلة، شغل كل خدمة:

```bash
# المحطة 1: Category Service (يجب البدء أولاً)
cd category-service
mvn spring-boot:run

# المحطة 2: Income Service
cd income-service
mvn spring-boot:run

# المحطة 3: Expense Service
cd expense-service
mvn spring-boot:run

# المحطة 4: Budget Service
cd budget-service
mvn spring-boot:run

# المحطة 5: AI Recommendation Service
cd ai-recommendation-service
mvn spring-boot:run

# المحطة 6: Dashboard Service
cd dashboard-service
mvn spring-boot:run
```

### الخطوة 5: اختبار الخدمات

```bash
# التحقق من أن جميع الخدمات تعمل
curl http://localhost:8001/api/incomes
curl http://localhost:8002/api/expenses
curl http://localhost:8003/api/categories
curl http://localhost:8004/api/budgets
curl http://localhost:8006/graphql

# جميع الاستجابات يجب أن تكون JSON أو GraphQL response
```

---

## 📊 نماذج البيانات

### 1. Income Entity
```json
{
  "id": 1,
  "userId": "user123",
  "amount": 5000.00,
  "source": "الراتب",
  "date": "2024-05-25",
  "createdAt": "2024-05-25"
}
```

### 2. Expense Entity
```json
{
  "id": 1,
  "userId": "user123",
  "amount": 150.00,
  "category": "طعام",
  "date": "2024-05-25",
  "note": "شراء من السوبر ماركت",
  "createdAt": "2024-05-25"
}
```

### 3. Category Entity
```json
{
  "id": 1,
  "name": "طعام",
  "description": "نفقات الطعام والمطاعم"
}
```

### 4. Budget Entity
```json
{
  "id": 1,
  "userId": "user123",
  "month": "2024-05",
  "totalLimit": 5000.00
}
```

---

## 🔌 أمثلة الـ APIs

### مثال كامل: إضافة فئة جديدة

```bash
curl -X POST http://localhost:8003/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "طعام",
    "description": "نفقات الطعام والمطاعم"
  }'
```

**الاستجابة:**
```json
{
  "id": 1,
  "name": "طعام",
  "description": "نفقات الطعام والمطاعم"
}
```

### مثال: إضافة دخل جديد

```bash
curl -X POST http://localhost:8001/api/incomes \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "amount": 5000.00,
    "source": "الراتب",
    "date": "2024-05-25"
  }'
```

### مثال: إضافة نفقة جديدة

```bash
curl -X POST http://localhost:8002/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "amount": 150.00,
    "category": "طعام",
    "date": "2024-05-25",
    "note": "شراء من السوبر ماركت"
  }'
```

### مثال: استعلام GraphQL

```bash
curl -X POST http://localhost:8006/graphql \
  -H "Content-Type: application/json" \
  -d '{
    "query": "query { financialOverview(userId: \"user123\", month: \"2024-05\") { totalIncome totalExpenses categories { name total } } }"
  }'
```

---

## 🔄 أنماط الاتصال

### 1. REST (المتزامن)
**الاستخدام:**
- Expense Service → Category Service (التحقق من الفئات)
- بين جميع الخدمات والعملاء المباشرين

**الفوائد:**
- بسيط وسهل الفهم
- مناسب للعمليات السريعة

### 2. Kafka (غير المتزامن)
**الاستخدام:**
- Income Service → ينشر أحداث الدخل الجديد
- Expense Service → ينشر أحداث النفقات الجديدة
- Dashboard Service ← يستمع للأحداث

**الفوائد:**
- فك الاقتران بين الخدمات
- معالجة موثوقة للأحداث
- تحميل عالي من العمليات

### 3. gRPC (عالي الأداء)
**الاستخدام:**
- Budget Service → AI Service (الحصول على التوصيات)

**الفوائد:**
- أداء عالي جداً
- استهلاك نطاق ترددي منخفض
- مثالي للعمليات الثقيلة

### 4. GraphQL (الاستعلام المرن)
**الاستخدام:**
- Dashboard Service → عرض بيانات مرنة حسب الحاجة

**الفوائد:**
- طلب البيانات المحددة فقط
- تقليل استهلاك البيانات
- واجهة موحدة

---

## 🐳 النشر عبر Docker

### نشر خدمة واحدة:

```bash
# 1. بناء الصورة
cd expense-service
mvn clean package -DskipTests -Dspring-boot.repackage.skip=false

# 2. إنشاء Dockerfile
# انظر DOCKER_DEPLOYMENT_GUIDE.md

# 3. بناء صورة Docker
docker build -t expense-tracker-expense-service:1.0.0 .

# 4. تشغيل الحاوية
docker run -p 8002:8002 \
  --network expense-tracker-network \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
  expense-tracker-expense-service:1.0.0
```

---

---

## ✅ المتطلبات المحققة

### ✓ البنية والتنظيم
- ✅ مجلد الجذر `expense-tracker-system` مع جميع الخدمات
- ✅ هيكل Maven مع 6 وحدات خدمة
- ✅ بناء جميع الخدمات مع `mvn clean install`
- ✅ توثيق واضح

### ✓ الخدمات الدقيقة
- ✅ **6 خدمات مستقلة:** Income, Expense, Category, Budget, AI, Dashboard
- ✅ **4 أنماط اتصال:** REST, Kafka, gRPC, GraphQL
- ✅ **معالجة الأحداث:** Kafka Event Streaming
- ✅ **الاستعلام المرن:** GraphQL

### ✓ التقنيات المستخدمة
- ✅ Spring Boot 3.1.5
- ✅ JPA/Hibernate
- ✅ Kafka
- ✅ gRPC
- ✅ GraphQL
- ✅ H2 Database
- ✅ Maven

---

## 🎓 المعرفة والمهارات المكتسبة

من خلال هذا المشروع، تم التعامل مع:

1. **معمارية الخدمات الدقيقة**
   - تصميم الأنظمة الموزعة
   - فصل الخدمات والمسؤوليات
   - التعامل مع المراحل والتوسع

2. **أنماط الاتصال**
   - REST API للعمليات المتزامنة
   - Kafka للعمليات غير المتزامنة
   - gRPC للأداء العالي
   - GraphQL للاستعلامات المرنة

3. **Spring Boot و Java**
   - بناء تطبيقات Spring Boot متقدمة
   - JPA/Hibernate للوصول للبيانات
   - المعالجات والخدمات المتقدمة
   - التكوين والحقن للاعتماديات

4. **معالجة الأحداث**
   - معمارية الأحداث (Event-Driven)
   - مستمعات Kafka
   - التعامل مع التوافقية والموثوقية

5. **Docker والحاويات**
   - بناء صور Docker
   - تشغيل الحاويات
   - التشبيك بين الخدمات

6. **GitHub و CI/CD**
   - إدارة الإصدارات
   - GitHub Actions
   - التكامل والنشر المستمر

---

## 🔗 الموارد الإضافية

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Kafka Documentation](https://kafka.apache.org/documentation/)
- [gRPC Guide](https://grpc.io/docs/)
- [GraphQL Specification](https://graphql.org/)
- [Docker Documentation](https://docs.docker.com/)

---




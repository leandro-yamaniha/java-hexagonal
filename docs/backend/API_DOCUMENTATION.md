# 📚 API Documentation

## 🌐 OpenAPI/Swagger Documentation

All three frameworks provide OpenAPI/Swagger documentation for easy API exploration and testing.

| Framework | Swagger UI | OpenAPI Spec |
|-----------|------------|--------------|
| **Spring Boot** | http://localhost:8082/swagger-ui.html | http://localhost:8082/v3/api-docs |
| **Quarkus** | http://localhost:8081/q/swagger-ui | http://localhost:8081/q/openapi |
| **Micronaut** | Static files served | http://localhost:8083/swagger/restaurant-management-api---micronaut-1.0.0.yml |

---

## 🔗 Application Endpoints

### Spring Boot Application (Port 8082)

- **Base URL**: http://localhost:8082
- **Health Check**: http://localhost:8082/actuator/health
- **Customer API**: http://localhost:8082/api/v1/customers
- **Menu API**: http://localhost:8082/api/v1/menu-items
- **Swagger UI**: http://localhost:8082/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8082/v3/api-docs
- **Virtual Threads Benchmark**: http://localhost:8082/api/v1/benchmark/virtual-threads
- **Startup Metrics**: http://localhost:8082/api/v1/startup/metrics

### Quarkus Application (Port 8081)

- **Base URL**: http://localhost:8081
- **Health Check**: http://localhost:8081/q/health
- **Customer API**: http://localhost:8081/api/v1/customers
- **Menu API**: http://localhost:8081/api/v1/menu-items
- **Swagger UI**: http://localhost:8081/q/swagger-ui
- **OpenAPI Spec**: http://localhost:8081/q/openapi
- **Dev UI**: http://localhost:8081/q/dev
- **Virtual Threads Benchmark**: http://localhost:8081/api/v1/benchmark/virtual-threads
- **Startup Metrics**: http://localhost:8081/api/v1/startup/metrics

### Micronaut Application (Port 8083)

- **Base URL**: http://localhost:8083
- **Health Check**: http://localhost:8083/health
- **Customer API**: http://localhost:8083/api/v1/customers
- **Menu API**: http://localhost:8083/api/v1/menu-items
- **OpenAPI Spec**: http://localhost:8083/swagger/restaurant-management-api---micronaut-1.0.0.yml
- **Virtual Threads Benchmark**: http://localhost:8083/api/v1/benchmark/virtual-threads
- **Startup Metrics**: http://localhost:8083/api/v1/startup/metrics

---

## 📋 Available Endpoints (All Three Frameworks)

### Customer Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/customers` | List all customers |
| `POST` | `/api/v1/customers` | Create a new customer |
| `GET` | `/api/v1/customers/{id}` | Get customer by ID |
| `PUT` | `/api/v1/customers/{id}` | Update customer |
| `DELETE` | `/api/v1/customers/{id}` | Delete customer |
| `PATCH` | `/api/v1/customers/{id}/activate` | Activate customer |
| `PATCH` | `/api/v1/customers/{id}/deactivate` | Deactivate customer |

### Menu Item Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/menu-items` | List all menu items |
| `POST` | `/api/v1/menu-items` | Create a new menu item |
| `GET` | `/api/v1/menu-items/{id}` | Get menu item by ID |
| `PUT` | `/api/v1/menu-items/{id}` | Update menu item |
| `DELETE` | `/api/v1/menu-items/{id}` | Delete menu item |
| `PATCH` | `/api/v1/menu-items/{id}/availability` | Update availability |
| `GET` | `/api/v1/menu-items/available` | Get available items only |
| `GET` | `/api/v1/menu-items/category/{category}` | Get items by category |

---

## 📝 Request/Response Examples

### Create Customer

**Request**:
```bash
curl -X POST http://localhost:8082/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890"
  }'
```

**Response** (201 Created):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "active": true,
  "createdAt": "2025-10-02T23:00:00Z",
  "updatedAt": "2025-10-02T23:00:00Z"
}
```

### Get All Customers

**Request**:
```bash
curl http://localhost:8082/api/v1/customers
```

**Response** (200 OK):
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "active": true
  },
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "phone": "+0987654321",
    "active": true
  }
]
```

### Create Menu Item

**Request**:
```bash
curl -X POST http://localhost:8082/api/v1/menu-items \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Margherita Pizza",
    "description": "Classic pizza with tomato and mozzarella",
    "price": 12.99,
    "category": "MAIN_COURSE",
    "available": true
  }'
```

**Response** (201 Created):
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "name": "Margherita Pizza",
  "description": "Classic pizza with tomato and mozzarella",
  "price": 12.99,
  "category": "MAIN_COURSE",
  "available": true,
  "createdAt": "2025-10-02T23:00:00Z",
  "updatedAt": "2025-10-02T23:00:00Z"
}
```

### Get Available Menu Items

**Request**:
```bash
curl http://localhost:8082/api/v1/menu-items/available
```

**Response** (200 OK):
```json
[
  {
    "id": "770e8400-e29b-41d4-a716-446655440002",
    "name": "Margherita Pizza",
    "description": "Classic pizza with tomato and mozzarella",
    "price": 12.99,
    "category": "MAIN_COURSE",
    "available": true
  }
]
```

---

## 🧪 Testing Endpoints

### Using cURL

```bash
# List all customers
curl http://localhost:8082/api/v1/customers

# Create a customer
curl -X POST http://localhost:8082/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","phone":"+1234567890"}'

# Get customer by ID
curl http://localhost:8082/api/v1/customers/{id}

# Update customer
curl -X PUT http://localhost:8082/api/v1/customers/{id} \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Name","email":"updated@example.com","phone":"+1234567890"}'

# Delete customer
curl -X DELETE http://localhost:8082/api/v1/customers/{id}

# Activate customer
curl -X PATCH http://localhost:8082/api/v1/customers/{id}/activate

# Deactivate customer
curl -X PATCH http://localhost:8082/api/v1/customers/{id}/deactivate
```

### Using HTTPie

```bash
# List all customers
http GET http://localhost:8082/api/v1/customers

# Create a customer
http POST http://localhost:8082/api/v1/customers \
  name="Test User" \
  email="test@example.com" \
  phone="+1234567890"

# Get customer by ID
http GET http://localhost:8082/api/v1/customers/{id}
```

---

## 🔍 Health Checks

All three frameworks provide health check endpoints:

```bash
# Spring Boot
curl http://localhost:8082/actuator/health

# Quarkus
curl http://localhost:8081/q/health

# Micronaut
curl http://localhost:8083/health
```

**Expected Response**:
```json
{
  "status": "UP"
}
```

---

## ⚡ Performance Benchmarks

### Virtual Threads Endpoint

Test Virtual Threads performance with I/O-intensive operations:

```bash
# Spring Boot
curl "http://localhost:8082/api/v1/benchmark/virtual-threads?delayMs=1000"

# Quarkus
curl "http://localhost:8081/api/v1/benchmark/virtual-threads?delayMs=1000"

# Micronaut
curl "http://localhost:8083/api/v1/benchmark/virtual-threads?delayMs=1000"
```

### Startup Metrics

Get application startup time and metrics:

```bash
# Spring Boot
curl http://localhost:8082/api/v1/startup/metrics

# Quarkus
curl http://localhost:8081/api/v1/startup/metrics

# Micronaut
curl http://localhost:8083/api/v1/startup/metrics
```

---

## 📖 Related Documentation

- **[README.md](../../README.md)** - Project overview
- **[ARCHITECTURE.md](./ARCHITECTURE.md)** - Architecture details
- **[TROUBLESHOOTING.md](./TROUBLESHOOTING.md)** - Common issues
- **[DTO_PATTERN_GUIDE.md](./DTO_PATTERN_GUIDE.md)** - DTO pattern guide

---

**Last Updated**: 2025-10-02  
**API Version**: v1  
**Status**: Active ✅

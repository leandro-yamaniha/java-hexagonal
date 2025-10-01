# Restaurant Management System

A comprehensive restaurant management system built with **hexagonal architecture** (ports and adapters pattern), supporting **three frameworks** (Spring Boot, Quarkus, and Micronaut) with complete framework independence in core modules.

> 📋 **Quick Start**: See [EXECUTIVE_SUMMARY.md](./EXECUTIVE_SUMMARY.md) for a complete overview of the project.

## 🏗️ Architecture

This project implements a **pure hexagonal architecture** with strict separation of concerns:

```
restaurant-management/
├── domain/                     # 🔵 Core business logic (pure Java, no frameworks)
├── application/               # 🔵 Use cases and application services (pure Java)
├── infrastructure/            # 🟡 External adapters (JPA, Redis, pure implementations)
├── spring-boot-app/          # 🟢 Spring Boot REST API (port 8082) ✅
├── quarkus-app/              # 🟢 Quarkus REST API (port 8081) ✅
├── micronaut-app/            # 🟢 Micronaut REST API (port 8083) ✅
├── architecture-tests/        # 🧪 ArchUnit tests (75 tests validating architecture)
└── docker/                   # 🐳 Docker configurations (MySQL + Redis)
```

### 🎯 Three Frameworks, One Architecture

This project demonstrates the **true power of hexagonal architecture** by supporting three different frameworks with the **same domain and business logic**:

| Framework | Port | Version | DTOs | Mappers | Controllers | OpenAPI | Status |
|-----------|------|---------|------|---------|-------------|---------|--------|
| **Spring Boot** | 8082 | 3.2.1 | 4 | 4 | 2 | ✅ Swagger UI | ✅ Active |
| **Quarkus** | 8081 | 3.6.4 | 4 | 2 | 1 | ✅ Swagger UI | ✅ Active |
| **Micronaut** | 8083 | 4.2.3 | 4 | 2 | 1 | ✅ YAML Spec | ✅ Active |

### 📊 Visual Diagrams

For detailed architecture diagrams and flow charts, see:
- **[ARCHITECTURE_DIAGRAMS.md](./ARCHITECTURE_DIAGRAMS.md)** - Complete visual documentation with Mermaid diagrams
- **[ARCHITECTURE.md](./ARCHITECTURE.md)** - Detailed architecture documentation
- **[ARCHITECTURE_TESTS_SUMMARY.md](./ARCHITECTURE_TESTS_SUMMARY.md)** - Architecture tests summary

### 🎨 DTO Pattern

This project uses the **DTO (Data Transfer Object) pattern** to keep the domain layer pure and free from serialization concerns:

- **[DTO_PATTERN_GUIDE.md](./DTO_PATTERN_GUIDE.md)** - Complete guide with examples and best practices
- **Domain Layer**: Pure Java entities without framework annotations
- **Presentation Layer**: DTOs with Jackson annotations for JSON serialization
- **Mappers**: Convert between Domain entities and DTOs

**Benefits:**
- ✅ Domain 100% pure (no Jackson, no framework dependencies)
- ✅ Easy to change API format without affecting business logic
- ✅ Better testability and maintainability
- ✅ Validated by architecture tests (44/44 passing)

## 🛠️ Troubleshooting

### Quarkus – EntityManager não injeta (Unsatisfied dependency)
Se você vir erros como "Unsatisfied dependency for type jakarta.persistence.EntityManager" ao subir o Quarkus:

1. __Dependência do ORM__ (arquivo `quarkus-app/pom.xml`):
   - Garanta que a dependência abaixo esteja presente:
     ```xml
     <dependency>
       <groupId>io.quarkus</groupId>
       <artifactId>quarkus-hibernate-orm</artifactId>
     </dependency>
     ```

2. __Pacotes de entidades JPA__ (arquivo `quarkus-app/src/main/resources/application.properties`):
   - Informe o pacote onde estão as entidades JPA para o Quarkus montar a Persistence Unit e disponibilizar o `EntityManager`:
     ```properties
     quarkus.hibernate-orm.packages=com.restaurant.infrastructure.persistence.entity
     ```

3. __Injeção do EntityManager__ (arquivo `quarkus-app/src/main/java/com/restaurant/quarkus/config/ApplicationConfig.java`):
   - Utilize injeção nativa do Quarkus e consuma o `EntityManager` nos producers dos repositórios:
     ```java
     @ApplicationScoped
     public class ApplicationConfig {
       @Inject
       EntityManager entityManager;

       @Produces @Singleton
       public CustomerRepository customerRepository() {
         return new JpaCustomerRepository(entityManager);
       }

       @Produces @Singleton
       public MenuItemRepository menuItemRepository() {
         return new JpaMenuItemRepository(entityManager);
       }
     }
     ```

4. __Reiniciar o dev mode do Quarkus__ (caso veja "Error restarting Quarkus"):
   - Pare processos ativos e suba novamente:
     ```bash
     # Em outro terminal:
     pkill -f "quarkus:dev" || true
     
     # No diretório quarkus-app
     mvn clean compile
     mvn quarkus:dev -Dquarkus.http.port=8081
     ```

5. __Verificar MySQL__:
   - Confirme que as credenciais e a URL batem com o `application.properties`:
     ```properties
     quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/restaurant_db
     quarkus.datasource.username=restaurant_user
     quarkus.datasource.password=restaurant_password
     ```

6. __Validar endpoints__:
   - Teste:
     ```bash
     curl -i http://localhost:8081/api/v1/customers
     ```

### Live Reload do Quarkus quebrou (página de erro 500 de reinício)
- Às vezes o hot reload entra em estado inconsistente. Faça um restart limpo conforme o passo 4 acima.

## 🧹 Git & Hygiene

- __Ignore arquivos de build__ (adicione um `.gitignore` na raiz):
  ```gitignore
  **/target/
  node_modules/
  .venv/
  .idea/
  .DS_Store
  ```

- __Se artefatos de build já estiverem versionados__, remova do índice mantendo no disco:
  ```bash
  git rm -r --cached **/target
  git commit -m "chore: stop tracking build artifacts"
  ```

 

### Architecture Principles
- **Framework Independence**: Core modules (`domain`, `application`, `infrastructure`) use only standard Java conventions
- **Dependency Inversion**: Business logic doesn't depend on frameworks or external libraries
- **Multiple Implementations**: Same business logic can be exposed via different frameworks
- **Clean Boundaries**: Each layer has well-defined interfaces and responsibilities

## Technology Stack

### Backend
- **Frameworks**: Quarkus 3.x, Spring Boot 3.x
- **Language**: Java 21
- **Database**: MySQL 8.0
- **Cache**: Redis
- **Build Tool**: Maven
- **Testing**: JUnit 5, Testcontainers

### Frontend
- **Framework**: Angular 17
- **UI Library**: Angular Material
- **State Management**: NgRx
- **HTTP Client**: Angular HttpClient

## Features

- **Menu Management**: Create, update, and manage restaurant menu items
- **Order Management**: Handle customer orders and order tracking
- **Customer Management**: Manage customer information and preferences
- **Table Management**: Restaurant table allocation and management
- **Inventory Management**: Track ingredients and stock levels
- **Staff Management**: Manage restaurant staff and roles
- **Reporting**: Sales reports and analytics

## 🚀 Getting Started

### Prerequisites
- **Java 21+** (Required)
- **Maven 3.9+** (Required)
- **MySQL 8.0** (Required - running on localhost:3306)
- **Redis** (Optional - for caching)
- **Docker & Docker Compose** (Optional - for infrastructure)

### Database Setup

1. **Create MySQL Database**:
   ```sql
   CREATE DATABASE restaurant_db;
   CREATE USER 'restaurant_user'@'localhost' IDENTIFIED BY 'restaurant_password';
   GRANT ALL PRIVILEGES ON restaurant_db.* TO 'restaurant_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

2. **Or use Docker**:
   ```bash
   docker-compose up -d mysql redis
   ```

### Building the Project

1. **Build Core Modules** (Required first):
   ```bash
   mvn clean install -DskipTests -pl domain,application,infrastructure
   ```

### Running Applications

You can run all three frameworks simultaneously on different ports!

#### 🟢 Spring Boot Application (Port 8082)
```bash
# From project root
mvn spring-boot:run -pl spring-boot-app

# Or using JAR
cd spring-boot-app
mvn clean package
java -jar target/spring-boot-app-1.0.0.jar
```
- **URL**: http://localhost:8082
- **API**: http://localhost:8082/api/v1/customers
- **Health**: http://localhost:8082/actuator/health
- **Swagger**: http://localhost:8082/swagger-ui.html
- **Status**: ✅ Fully functional

#### 🟢 Quarkus Application (Port 8081)
```bash
# From project root
mvn quarkus:dev -pl quarkus-app

# Or using JAR
cd quarkus-app
mvn clean package
java -jar target/quarkus-app-runner.jar
```
- **URL**: http://localhost:8081
- **API**: http://localhost:8081/api/v1/customers
- **Health**: http://localhost:8081/q/health
- **Dev UI**: http://localhost:8081/q/dev
- **Status**: ✅ Fully functional

#### 🟢 Micronaut Application (Port 8083)
```bash
# From project root
mvn clean package -pl micronaut-app -DskipTests
java -Dmicronaut.server.port=8083 -jar micronaut-app/target/micronaut-app-1.0.0.jar

# Or specify port in command line
java -jar micronaut-app/target/micronaut-app-1.0.0.jar
```
- **URL**: http://localhost:8083
- **API**: http://localhost:8083/api/v1/customers
- **Health**: http://localhost:8083/health
- **OpenAPI**: http://localhost:8083/swagger/restaurant-management-api---micronaut-1.0.0.yml
- **Status**: ✅ Fully functional

### 🎯 Quick Start - All Three Frameworks

```bash
# 1. Start infrastructure (MySQL + Redis)
cd docker
docker-compose up -d

# 2. Build all modules
cd ..
mvn clean install -DskipTests

# 3. Start Spring Boot (Terminal 1)
mvn spring-boot:run -pl spring-boot-app

# 4. Start Quarkus (Terminal 2)
mvn quarkus:dev -pl quarkus-app

# 5. Start Micronaut (Terminal 3)
java -Dmicronaut.server.port=8083 -jar micronaut-app/target/micronaut-app-1.0.0.jar
```

### ✅ Verify All Services

```bash
# Test all endpoints
curl http://localhost:8082/api/v1/customers  # Spring Boot
curl http://localhost:8081/api/v1/customers  # Quarkus
curl http://localhost:8083/api/v1/customers  # Micronaut

# Check health
curl http://localhost:8082/actuator/health   # Spring Boot
curl http://localhost:8081/q/health          # Quarkus
curl http://localhost:8083/health            # Micronaut
```

## 📚 API Documentation

### OpenAPI/Swagger Documentation

| Framework | Swagger UI | OpenAPI Spec |
|-----------|------------|--------------|
| **Spring Boot** | http://localhost:8082/swagger-ui.html | http://localhost:8082/v3/api-docs |
| **Quarkus** | http://localhost:8081/q/swagger-ui | http://localhost:8081/q/openapi |
| **Micronaut** | Static files served | http://localhost:8083/swagger/restaurant-management-api---micronaut-1.0.0.yml |

### Application Endpoints (All Frameworks)

#### Spring Boot Application (Port 8082)
- **Base URL**: http://localhost:8082
- **Health Check**: http://localhost:8082/actuator/health
- **Customer API**: http://localhost:8082/api/v1/customers
- **Menu API**: http://localhost:8082/api/v1/menu-items

#### Quarkus Application (Port 8081)
- **Base URL**: http://localhost:8081
- **Health Check**: http://localhost:8081/q/health
- **Customer API**: http://localhost:8081/api/v1/customers
- **Menu API**: http://localhost:8081/api/v1/menu-items

#### Micronaut Application (Port 8083)
- **Base URL**: http://localhost:8083
- **Health Check**: http://localhost:8083/health
- **Customer API**: http://localhost:8083/api/v1/customers
- **OpenAPI Spec**: http://localhost:8083/swagger/restaurant-management-api---micronaut-1.0.0.yml

### Available Endpoints (All Three Frameworks)
```
# Customer Management
GET    /api/v1/customers           # List all customers
POST   /api/v1/customers           # Create customer
GET    /api/v1/customers/{id}      # Get customer by ID
PUT    /api/v1/customers/{id}      # Update customer
DELETE /api/v1/customers/{id}      # Delete customer
PATCH  /api/v1/customers/{id}/activate    # Activate customer
PATCH  /api/v1/customers/{id}/deactivate  # Deactivate customer

# Menu Item Management
GET    /api/v1/menu-items          # List menu items
POST   /api/v1/menu-items          # Create menu item
GET    /api/v1/menu-items/{id}     # Get menu item by ID
PUT    /api/v1/menu-items/{id}     # Update menu item
DELETE /api/v1/menu-items/{id}     # Delete menu item
PATCH  /api/v1/menu-items/{id}/availability  # Update availability
GET    /api/v1/menu-items/available           # Get available items
GET    /api/v1/menu-items/category/{category} # Get by category
```

## 🚀 Current Status

### ✅ Both Applications Fully Operational

| Application | Port | Status | Database | Cache | API Endpoints |
|-------------|------|--------|----------|-------|---------------|
| **Spring Boot** | 8082 | ✅ Running | ✅ MySQL Connected | ✅ Redis Connected | ✅ All Working |
| **Quarkus** | 8081 | ✅ Running | ✅ MySQL Connected | ✅ Redis Connected | ✅ All Working |

### 🏗️ Architecture Achievements

- ✅ **Hexagonal Architecture**: Clean separation of domain, application, and infrastructure layers
- ✅ **Framework Independence**: Core business logic shared between Spring Boot and Quarkus
- ✅ **Database Integration**: Both applications connected to MySQL with sample data
- ✅ **Caching Layer**: Redis integration for performance optimization
- ✅ **REST APIs**: Complete CRUD operations for customers and menu items
- ✅ **Dependency Injection**: Proper CDI configuration in both frameworks

### 🔧 Technical Implementation

- **Domain Layer**: Pure Java entities and value objects
- **Application Layer**: Use cases and service implementations
- **Infrastructure Layer**: JPA repositories, Redis cache, database entities
- **Spring Boot Module**: REST controllers with Spring Boot configuration
- **Quarkus Module**: JAX-RS resources with CDI configuration

### 📊 Verification Results

Both applications successfully serve the same data from the shared MySQL database:
- **Customers**: 3 records available via both APIs
- **Menu Items**: 5 records available via both APIs
- **Health Checks**: Spring Boot actuator health endpoint operational
- **Database**: MySQL connection validated on both applications
- **Cache**: Redis connectivity confirmed

## License

MIT License

# Hexagonal Architecture Implementation

## Overview

This project implements a **pure hexagonal architecture** (also known as ports and adapters pattern) that ensures complete framework independence in the core business logic while allowing multiple framework implementations.

## Architecture Layers

### 🔵 Core Layers (Framework Independent)

#### 1. Domain Layer (`domain/`)
- **Purpose**: Contains the core business entities and domain logic
- **Dependencies**: None (pure Java)
- **Key Components**:
  - `Customer`: Core customer entity with business rules
  - `MenuItem`: Menu item entity with pricing and availability logic
  - `Order`: Order aggregate with business validation
  - Value objects and domain services

#### 2. Application Layer (`application/`)
- **Purpose**: Orchestrates business use cases and application services
- **Dependencies**: Only domain layer
- **Key Components**:
  - **Ports In** (`port/in/`): Use case interfaces (CustomerUseCase, MenuUseCase)
  - **Ports Out** (`port/out/`): Repository and service interfaces
  - **Services** (`service/`): Use case implementations (CustomerService, MenuService)

### 🟡 Infrastructure Layer (`infrastructure/`)
- **Purpose**: Implements external adapters and technical concerns
- **Dependencies**: Application and domain layers (but no specific frameworks)
- **Key Components**:
  - **Persistence**: JPA entities and repository implementations
  - **Cache**: Redis cache service implementation
  - **Mappers**: Entity-to-domain object mappers

### 🟢 Application Frameworks (Framework Specific)

#### Spring Boot Application (`spring-boot-app/`)
- **Purpose**: REST API implementation using Spring Boot
- **Status**: ✅ Fully functional
- **Key Features**:
  - REST controllers for Customer and Menu management
  - Spring dependency injection configuration
  - JPA/Hibernate integration
  - Redis caching
  - Health checks and monitoring

#### Quarkus Application (`quarkus-app/`)
- **Purpose**: Alternative REST API implementation using Quarkus
- **Status**: ⚠️ Requires EntityManager configuration
- **Key Features**:
  - JAX-RS controllers
  - CDI dependency injection
  - Native compilation support

## Visual Architecture

For detailed visual diagrams with Mermaid, see **[ARCHITECTURE_DIAGRAMS.md](./ARCHITECTURE_DIAGRAMS.md)**

### DTO Pattern Implementation

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant DTO
    participant Mapper
    participant UseCase
    participant Domain
    
    Client->>Controller: HTTP Request (JSON)
    Controller->>DTO: Deserialize to DTO
    DTO->>Mapper: toCommand()
    Mapper->>UseCase: execute(command)
    UseCase->>Domain: Business Logic
    Domain-->>UseCase: Domain Entity
    UseCase-->>Mapper: Domain Entity
    Mapper->>DTO: toDTO(entity)
    DTO-->>Controller: CustomerDTO
    Controller-->>Client: HTTP Response (JSON)
    
    Note over DTO: Jackson Annotations<br/>Validation<br/>Serialization
    Note over Domain: Pure Java<br/>No Frameworks<br/>Business Rules
```

## Dependency Flow

```
┌─────────────────┐    ┌─────────────────┐
│   Spring Boot   │    │     Quarkus     │
│   Application   │    │   Application   │
└─────────┬───────┘    └─────────┬───────┘
          │                      │
          └──────────┬───────────┘
                     │
          ┌──────────▼───────────┐
          │   Infrastructure     │
          │     (Adapters)       │
          └──────────┬───────────┘
                     │
          ┌──────────▼───────────┐
          │    Application       │
          │   (Use Cases)        │
          └──────────┬───────────┘
                     │
          ┌──────────▼───────────┐
          │      Domain          │
          │  (Business Logic)    │
          └──────────────────────┘
```

## Key Architectural Decisions

### 1. Framework Independence
- Core modules (`domain`, `application`, `infrastructure`) use only standard Java
- No Spring or Quarkus annotations in core business logic
- Repository implementations use plain JPA without framework-specific annotations

### 2. Dependency Inversion
- Business logic defines interfaces (ports)
- Infrastructure implements these interfaces (adapters)
- Application frameworks wire dependencies together

### 3. Multiple Framework Support
- Same business logic exposed through different frameworks
- Each framework has its own configuration and dependency injection
- Shared infrastructure implementations

### 4. Clean Module Boundaries

```xml
<!-- Root POM: Framework neutral -->
<parent>
    <!-- No parent - neutral -->
</parent>

<!-- Spring Boot App: Framework specific -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
</parent>

<!-- Core modules: Pure Java -->
<parent>
    <groupId>com.restaurant</groupId>
    <artifactId>restaurant-management</artifactId>
</parent>
```


## Configuration Patterns

### Spring Boot Configuration

```java
@Configuration
public class ApplicationConfig {
    
    @Bean
    public CustomerRepository customerRepository(EntityManager entityManager) {
        return new JpaCustomerRepository(entityManager);
    }
    
    @Bean
    public CustomerUseCase customerUseCase(CustomerRepository repository, CacheService cache) {
        return new CustomerService(repository, cache);
    }
}
```

### Repository Implementation (Framework Independent)

```java
public class JpaCustomerRepository implements CustomerRepository {
    
    private final EntityManager entityManager;
    
    public JpaCustomerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Override
    @Transactional  // Using Jakarta EE, not Spring
    public Customer save(Customer customer) {
        // Implementation using pure JPA
    }
}
```

## Benefits of This Architecture

1. **Testability**: Easy to unit test business logic without frameworks
2. **Flexibility**: Can switch frameworks without changing business logic
3. **Maintainability**: Clear separation of concerns
4. **Evolution**: Can add new frameworks or change existing ones
5. **Performance**: Can optimize different implementations for different needs

## Testing Strategy

### 🧪 Architecture Tests (`architecture-tests/`)

Automated verification of architectural rules using **ArchUnit**:

- **Hexagonal Architecture Compliance**: Verifies layer dependencies and isolation
- **Naming Conventions**: Ensures consistent naming patterns across the codebase
- **Ports and Adapters**: Validates correct implementation of the pattern
- **Layer Purity**: Ensures each layer only uses appropriate dependencies

#### Key Architecture Rules Enforced

```java
// Domain layer must be pure (no framework dependencies)
@Test
void domainLayerShouldNotDependOnAnyOtherLayer() {
    noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("..application..", "..infrastructure..");
}

// Application layer should only depend on domain
@Test
void applicationLayerShouldOnlyDependOnDomainLayer() {
    noClasses()
        .that().resideInAPackage("..application..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("..infrastructure..");
}
```

#### Running Architecture Tests

```bash
# Run all architecture tests
mvn test -pl architecture-tests

# Run specific test class
mvn test -pl architecture-tests -Dtest=HexagonalArchitectureTest
```


### 📋 Other Testing Levels

- **Unit Tests**: Test domain logic in isolation
- **Integration Tests**: Test use cases with in-memory implementations
- **Framework Tests**: Test REST endpoints and framework integration
- **Contract Tests**: Ensure all implementations satisfy the same contracts

## Migration Path

To add a new framework (e.g., Micronaut):
1. Create new module `micronaut-app/`
2. Add framework-specific dependencies
3. Implement REST controllers
4. Configure dependency injection
5. Wire existing infrastructure implementations

The core business logic remains unchanged.

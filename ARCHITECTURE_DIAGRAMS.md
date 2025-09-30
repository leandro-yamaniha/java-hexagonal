# 📊 Diagramas de Arquitetura - Restaurant Management System

## 🏗️ Arquitetura Hexagonal

```mermaid
graph TB
    subgraph "Presentation Layer"
        A[Spring Boot<br/>Controllers] 
        B[Quarkus<br/>Resources]
        C[DTOs]
    end
    
    subgraph "Application Layer"
        D[Use Cases<br/>Services]
        E[Input Ports<br/>Interfaces]
        F[Output Ports<br/>Interfaces]
    end
    
    subgraph "Domain Layer"
        G[Entities]
        H[Value Objects]
        I[Domain Logic]
    end
    
    subgraph "Infrastructure Layer"
        J[JPA Repositories]
        K[Redis Cache]
        L[Mappers]
    end
    
    subgraph "External"
        M[(MySQL<br/>Database)]
        N[(Redis<br/>Cache)]
    end
    
    A --> C
    B --> C
    C --> E
    E --> D
    D --> F
    D --> G
    D --> H
    F --> J
    F --> K
    J --> L
    L --> G
    J --> M
    K --> N
    
    style A fill:#e1f5ff
    style B fill:#e1f5ff
    style D fill:#fff4e1
    style G fill:#e8f5e9
    style J fill:#f3e5f5
```

## 🔄 Fluxo de Criação de Customer

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant DTO
    participant Mapper
    participant UseCase
    participant Domain
    participant Repository
    participant Database
    
    Client->>Controller: POST /api/v1/customers
    Controller->>Controller: Validate Request
    Controller->>UseCase: createCustomer(command)
    UseCase->>Domain: new Customer(...)
    Domain->>Domain: Validate Business Rules
    UseCase->>Repository: save(customer)
    Repository->>Mapper: toEntity(customer)
    Mapper->>Database: INSERT INTO customers
    Database-->>Mapper: Success
    Mapper-->>Repository: CustomerEntity
    Repository->>Mapper: toDomain(entity)
    Mapper-->>Repository: Customer
    Repository-->>UseCase: Customer
    UseCase-->>Controller: Customer
    Controller->>Mapper: toDTO(customer)
    Mapper-->>Controller: CustomerDTO
    Controller-->>Client: 201 Created + CustomerDTO
```

## 📦 Estrutura de Pacotes

```mermaid
graph LR
    subgraph "com.restaurant"
        subgraph "domain"
            D1[entity]
            D2[valueobject]
        end
        
        subgraph "application"
            A1[service]
            A2[port.in]
            A3[port.out]
        end
        
        subgraph "infrastructure"
            I1[persistence.entity]
            I2[persistence.repository]
            I3[persistence.mapper]
            I4[cache]
        end
        
        subgraph "springboot"
            S1[controller]
            S2[dto]
            S3[mapper]
            S4[config]
        end
        
        subgraph "quarkus"
            Q1[resource]
            Q2[config]
        end
    end
    
    S1 --> S2
    S2 --> S3
    S3 --> A2
    A2 --> A1
    A1 --> A3
    A3 --> I2
    I2 --> I3
    I3 --> D1
    A1 --> D1
    
    style D1 fill:#e8f5e9
    style D2 fill:#e8f5e9
    style A1 fill:#fff4e1
    style I2 fill:#f3e5f5
    style S1 fill:#e1f5ff
```

## 🎯 Padrão Ports & Adapters

```mermaid
graph TB
    subgraph "Driving Side (Primary)"
        REST[REST API<br/>Controllers]
        CLI[CLI Interface]
    end
    
    subgraph "Application Core"
        subgraph "Input Ports"
            IP1[CustomerUseCase]
            IP2[MenuUseCase]
            IP3[OrderUseCase]
        end
        
        subgraph "Business Logic"
            BL[Services]
        end
        
        subgraph "Output Ports"
            OP1[CustomerRepository]
            OP2[MenuItemRepository]
            OP3[CacheService]
        end
    end
    
    subgraph "Driven Side (Secondary)"
        DB[(Database<br/>MySQL)]
        CACHE[(Cache<br/>Redis)]
        EXT[External APIs]
    end
    
    REST --> IP1
    REST --> IP2
    REST --> IP3
    CLI --> IP1
    
    IP1 --> BL
    IP2 --> BL
    IP3 --> BL
    
    BL --> OP1
    BL --> OP2
    BL --> OP3
    
    OP1 --> DB
    OP2 --> DB
    OP3 --> CACHE
    OP3 --> EXT
    
    style BL fill:#fff4e1
    style REST fill:#e1f5ff
    style DB fill:#f3e5f5
```

## 🔀 Fluxo de Dados com Cache

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant Service
    participant Cache
    participant Repository
    participant Database
    
    Client->>Controller: GET /api/v1/customers/{id}
    Controller->>Service: findCustomerById(id)
    
    Service->>Cache: get(key, Customer.class)
    
    alt Cache Hit
        Cache-->>Service: Optional<Customer>
        Service-->>Controller: Customer
    else Cache Miss
        Cache-->>Service: Optional.empty()
        Service->>Repository: findById(id)
        Repository->>Database: SELECT * FROM customers
        Database-->>Repository: CustomerEntity
        Repository-->>Service: Optional<Customer>
        Service->>Cache: put(key, customer)
        Service-->>Controller: Customer
    end
    
    Controller->>Controller: toDTO(customer)
    Controller-->>Client: 200 OK + CustomerDTO
```

## 🏛️ Camadas e Dependências

```mermaid
graph TD
    subgraph "Layer Dependencies"
        L1[Presentation Layer<br/>Spring Boot / Quarkus]
        L2[Application Layer<br/>Use Cases & Ports]
        L3[Domain Layer<br/>Entities & Business Logic]
        L4[Infrastructure Layer<br/>JPA, Redis, Mappers]
    end
    
    L1 -->|depends on| L2
    L2 -->|depends on| L3
    L4 -->|depends on| L3
    L4 -->|implements| L2
    
    L3 -.->|NO dependencies| X[External Frameworks]
    
    style L3 fill:#e8f5e9,stroke:#4caf50,stroke-width:3px
    style X fill:#ffebee,stroke:#f44336,stroke-width:2px,stroke-dasharray: 5 5
```

## 🔐 Fluxo de Validação

```mermaid
flowchart TD
    A[Request] --> B{DTO Validation}
    B -->|Invalid| C[400 Bad Request]
    B -->|Valid| D[Create Command]
    D --> E{Business Rules}
    E -->|Violation| F[IllegalArgumentException]
    E -->|Valid| G[Create Domain Entity]
    G --> H{Domain Validation}
    H -->|Invalid| I[ValidationException]
    H -->|Valid| J[Save to Repository]
    J --> K[Return Entity]
    K --> L[Convert to DTO]
    L --> M[201 Created]
    
    style G fill:#e8f5e9
    style C fill:#ffebee
    style F fill:#ffebee
    style I fill:#ffebee
    style M fill:#e1f5ff
```

## 🔄 Ciclo de Vida do Pedido

```mermaid
stateDiagram-v2
    [*] --> PENDING: Create Order
    PENDING --> CONFIRMED: Confirm Payment
    PENDING --> CANCELLED: Cancel
    CONFIRMED --> PREPARING: Start Preparation
    PREPARING --> READY: Complete Preparation
    READY --> DELIVERING: Start Delivery
    DELIVERING --> DELIVERED: Complete Delivery
    DELIVERED --> COMPLETED: Customer Confirmation
    CONFIRMED --> CANCELLED: Cancel
    PREPARING --> CANCELLED: Cancel
    CANCELLED --> [*]
    COMPLETED --> [*]
    
    note right of PENDING
        Initial state
        Awaiting payment
    end note
    
    note right of PREPARING
        Kitchen is preparing
        Estimated time tracking
    end note
    
    note right of COMPLETED
        Final state
        Order archived
    end note
```

## 🏢 Arquitetura Multi-Framework

```mermaid
graph TB
    subgraph "Client Applications"
        WEB[Web Browser<br/>Angular]
        MOBILE[Mobile App]
        API[External API]
    end
    
    subgraph "API Gateway / Load Balancer"
        LB[Load Balancer]
    end
    
    subgraph "Spring Boot Instance"
        SB1[Spring Boot App<br/>Port 8082]
    end
    
    subgraph "Quarkus Instance"
        QK1[Quarkus App<br/>Port 8081]
    end
    
    subgraph "Shared Layers"
        DOM[Domain Layer]
        APP[Application Layer]
        INF[Infrastructure Layer]
    end
    
    subgraph "Data Layer"
        MYSQL[(MySQL<br/>Database)]
        REDIS[(Redis<br/>Cache)]
    end
    
    WEB --> LB
    MOBILE --> LB
    API --> LB
    
    LB --> SB1
    LB --> QK1
    
    SB1 --> DOM
    SB1 --> APP
    SB1 --> INF
    
    QK1 --> DOM
    QK1 --> APP
    QK1 --> INF
    
    INF --> MYSQL
    INF --> REDIS
    
    style DOM fill:#e8f5e9
    style APP fill:#fff4e1
    style INF fill:#f3e5f5
    style SB1 fill:#e1f5ff
    style QK1 fill:#e1f5ff
```

## 📊 Fluxo de Testes de Arquitetura

```mermaid
flowchart LR
    A[Code Change] --> B[Maven Build]
    B --> C[Architecture Tests]
    
    subgraph "Architecture Validation"
        C --> D{Hexagonal Rules}
        C --> E{Naming Conventions}
        C --> F{Layer Purity}
        C --> G{Ports & Adapters}
    end
    
    D -->|Pass| H[✅]
    D -->|Fail| I[❌ Violation]
    E -->|Pass| H
    E -->|Fail| I
    F -->|Pass| H
    F -->|Fail| I
    G -->|Pass| H
    G -->|Fail| I
    
    H --> J[Build Success]
    I --> K[Build Failure]
    
    J --> L[Deploy]
    K --> M[Fix Code]
    M --> A
    
    style H fill:#e8f5e9
    style I fill:#ffebee
    style J fill:#e1f5ff
    style K fill:#ffebee
```

## 🎨 Padrão DTO vs Domain

```mermaid
graph LR
    subgraph "Presentation Layer"
        DTO[CustomerDTO<br/>+ Jackson Annotations<br/>+ Validation<br/>+ Serialization]
    end
    
    subgraph "Mapper"
        MAP[CustomerDTOMapper<br/>toDTO()<br/>toDomain()]
    end
    
    subgraph "Domain Layer"
        DOM[Customer Entity<br/>+ Business Logic<br/>+ Pure Java<br/>- No Frameworks]
    end
    
    DTO <-->|Convert| MAP
    MAP <-->|Convert| DOM
    
    style DTO fill:#e1f5ff
    style DOM fill:#e8f5e9
    style MAP fill:#fff4e1
```

---

## 📚 Legenda de Cores

- 🟢 **Verde**: Domain Layer (Puro, sem frameworks)
- 🟡 **Amarelo**: Application Layer (Use Cases)
- 🟣 **Roxo**: Infrastructure Layer (Persistência)
- 🔵 **Azul**: Presentation Layer (Controllers/APIs)
- 🔴 **Vermelho**: Erros ou Violações

---

## 🔗 Links Relacionados

- [ARCHITECTURE.md](./ARCHITECTURE.md) - Documentação detalhada da arquitetura
- [ARCHITECTURE_TESTS_SUMMARY.md](./ARCHITECTURE_TESTS_SUMMARY.md) - Resumo dos testes
- [README.md](./README.md) - Guia principal do projeto

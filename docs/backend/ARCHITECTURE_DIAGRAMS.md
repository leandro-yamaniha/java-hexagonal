# 📊 Diagramas de Arquitetura - Restaurant Management System

## 🏗️ Arquitetura Hexagonal

```mermaid
flowchart TB
    subgraph Driving["🔌 Driving Adapters"]
        Spring[Spring Boot<br/>REST API :8082]
        Quarkus[Quarkus<br/>REST API :8081]
        Micronaut[Micronaut<br/>REST API :8083]
    end
    
    subgraph Application["📍 Application Layer"]
        PortIn[Input Ports<br/>Use Case Interfaces]
        Services[Use Case Services<br/>Business Orchestration]
        PortOut[Output Ports<br/>Repository Interfaces]
    end
    
    subgraph Domain["⬡ Domain Layer"]
        Entities[Domain Entities<br/>Customer, MenuItem, Order]
        ValueObjects[Value Objects<br/>Money, Status]
        Logic[Business Logic<br/>Pure Java]
    end
    
    subgraph Infrastructure["🔧 Infrastructure Layer"]
        JPA[JPA Repositories<br/>MySQL Persistence]
        Redis[Redis Cache<br/>Caching Service]
        Mappers[Entity Mappers<br/>JPA ↔ Domain]
    end
    
    subgraph External["💾 External Systems"]
        MySQL[(MySQL<br/>Database)]
        RedisDB[(Redis<br/>Cache)]
    end
    
    Spring --> PortIn
    Quarkus --> PortIn
    Micronaut --> PortIn
    
    PortIn --> Services
    Services --> Entities
    Services --> Logic
    Services --> PortOut
    
    PortOut --> JPA
    PortOut --> Redis
    
    JPA --> Mappers
    Mappers --> Entities
    
    JPA --> MySQL
    Redis --> RedisDB
    
    style Driving fill:none,stroke:#1976D2,stroke-width:2px,stroke-dasharray: 5 5
    style Application fill:none,stroke:#7B1FA2,stroke-width:2px,stroke-dasharray: 5 5
    style Domain fill:none,stroke:#4caf50,stroke-width:4px
    style Infrastructure fill:none,stroke:#F57C00,stroke-width:2px,stroke-dasharray: 5 5
    style External fill:none,stroke:#666,stroke-width:2px,stroke-dasharray: 5 5
    style Entities fill:none,stroke:#4caf50,stroke-width:3px
    style Logic fill:none,stroke:#4caf50,stroke-width:2px
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
flowchart TB
    subgraph Backend["backend/"]
        subgraph Domain["domain/"]
            D1[entity/<br/>Customer, MenuItem, Order]
            D2[valueobject/<br/>Money, Status]
        end
        
        subgraph Application["application/"]
            A1[service/<br/>Use Case Implementations]
            A2[port.in/<br/>Input Interfaces]
            A3[port.out/<br/>Output Interfaces]
        end
        
        subgraph Infrastructure["infrastructure/"]
            I1[persistence.entity/<br/>JPA Entities]
            I2[persistence.repository/<br/>JPA Repos]
            I3[persistence.mapper/<br/>Entity Mappers]
            I4[cache/<br/>Redis Service]
        end
        
        subgraph SpringBoot["spring-boot-app/"]
            S1[controller/<br/>REST Controllers]
            S2[dto/<br/>DTOs + Jackson]
            S3[mapper/<br/>DTO Mappers]
            S4[config/<br/>Spring Config]
        end
        
        subgraph Quarkus["quarkus-app/"]
            Q1[resource/<br/>JAX-RS Resources]
            Q2[dto/<br/>DTOs + Jackson]
            Q3[mapper/<br/>DTO Mappers]
        end
        
        subgraph Micronaut["micronaut-app/"]
            M1[controller/<br/>HTTP Controllers]
            M2[dto/<br/>DTOs + Jackson]
            M3[mapper/<br/>DTO Mappers]
        end
    end
    
    S1 --> S2 --> S3 --> A2
    Q1 --> Q2 --> Q3 --> A2
    M1 --> M2 --> M3 --> A2
    
    A2 --> A1
    A1 --> D1
    A1 --> A3
    A3 --> I2
    I2 --> I3
    I3 --> D1
    
    style Domain fill:none,stroke:#4caf50,stroke-width:3px,stroke-dasharray: 5 5
    style Application fill:none,stroke:#7B1FA2,stroke-width:2px,stroke-dasharray: 5 5
    style Infrastructure fill:none,stroke:#F57C00,stroke-width:2px,stroke-dasharray: 5 5
    style SpringBoot fill:none,stroke:#1976D2,stroke-width:2px,stroke-dasharray: 5 5
    style Quarkus fill:none,stroke:#C2185B,stroke-width:2px,stroke-dasharray: 5 5
    style Micronaut fill:none,stroke:#7B1FA2,stroke-width:2px,stroke-dasharray: 5 5
    style Backend fill:none,stroke:#666,stroke-width:1px
```

## 🎯 Padrão Ports & Adapters

```mermaid
flowchart TB
    subgraph Driving["🔌 Driving Adapters (Primary)"]
        REST[REST API<br/>Spring/Quarkus/Micronaut]
        CLI[CLI Interface<br/>Future]
    end
    
    subgraph Core["⬡ Application Core"]
        subgraph InputPorts["Input Ports"]
            IP1[CustomerUseCase]
            IP2[MenuUseCase]
            IP3[OrderUseCase]
        end
        
        subgraph Business["Business Logic"]
            BL[Use Case Services<br/>Domain Operations]
        end
        
        subgraph OutputPorts["Output Ports"]
            OP1[CustomerRepository]
            OP2[MenuItemRepository]
            OP3[CacheService]
        end
    end
    
    subgraph Driven["🔧 Driven Adapters (Secondary)"]
        DB[JPA Repository<br/>MySQL]
        CACHE[Redis Cache<br/>Caching]
        EXT[External APIs<br/>Future]
    end
    
    REST --> IP1 & IP2 & IP3
    CLI -.-> IP1
    
    IP1 & IP2 & IP3 --> BL
    
    BL --> OP1 & OP2 & OP3
    
    OP1 & OP2 --> DB
    OP3 --> CACHE
    OP3 -.-> EXT
    
    style Driving fill:none,stroke:#1976D2,stroke-width:2px,stroke-dasharray: 5 5
    style Core fill:none,stroke:#4caf50,stroke-width:4px
    style Driven fill:none,stroke:#F57C00,stroke-width:2px,stroke-dasharray: 5 5
    style InputPorts fill:none,stroke:#7B1FA2,stroke-width:2px,stroke-dasharray: 3 3
    style Business fill:none,stroke:#4caf50,stroke-width:2px,stroke-dasharray: 3 3
    style OutputPorts fill:none,stroke:#7B1FA2,stroke-width:2px,stroke-dasharray: 3 3
    style BL fill:none,stroke:#4caf50,stroke-width:2px
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
flowchart TD
    L1[🔌 Presentation Layer<br/>Spring Boot / Quarkus / Micronaut]
    L2[📍 Application Layer<br/>Use Cases & Ports]
    L3[⬡ Domain Layer<br/>Entities & Business Logic<br/>Pure Java]
    L4[🔧 Infrastructure Layer<br/>JPA, Redis, Mappers]
    X[❌ External Frameworks<br/>Spring, Quarkus, etc.]
    
    L1 -->|depends on| L2
    L2 -->|depends on| L3
    L4 -->|depends on| L3
    L4 -->|implements| L2
    
    L3 -.->|NO dependencies| X
    
    style L1 fill:none,stroke:#1976D2,stroke-width:2px
    style L2 fill:none,stroke:#7B1FA2,stroke-width:2px
    style L3 fill:none,stroke:#4caf50,stroke-width:4px
    style L4 fill:none,stroke:#F57C00,stroke-width:2px
    style X fill:none,stroke:#f44336,stroke-width:2px,stroke-dasharray: 5 5
```

## 🔐 Fluxo de Validação

```mermaid
flowchart TD
    A[📥 Request] --> B{DTO Validation}
    B -->|Invalid| C[❌ 400 Bad Request]
    B -->|Valid| D[Create Command]
    D --> E{Business Rules<br/>Validation}
    E -->|Violation| F[❌ IllegalArgumentException]
    E -->|Valid| G[⬡ Create Domain Entity]
    G --> H{Domain Validation}
    H -->|Invalid| I[❌ ValidationException]
    H -->|Valid| J[💾 Save to Repository]
    J --> K[Return Entity]
    K --> L[Convert to DTO]
    L --> M[✅ 201 Created]
    
    style A fill:none,stroke:#1976D2,stroke-width:2px
    style G fill:none,stroke:#4caf50,stroke-width:3px
    style C fill:none,stroke:#f44336,stroke-width:2px
    style F fill:none,stroke:#f44336,stroke-width:2px
    style I fill:none,stroke:#f44336,stroke-width:2px
    style M fill:none,stroke:#4caf50,stroke-width:2px
    style J fill:none,stroke:#F57C00,stroke-width:2px
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
flowchart TB
    subgraph Clients["👥 Client Applications"]
        WEB[🌐 Web Browser<br/>Angular]
        MOBILE[📱 Mobile App<br/>Future]
        API[🔗 External API<br/>Future]
    end
    
    subgraph Gateway["🔀 API Gateway"]
        LB[Nginx Load Balancer<br/>Round Robin]
    end
    
    subgraph Backends["☕ Backend Instances"]
        SB1[Spring Boot<br/>:8082]
        QK1[Quarkus<br/>:8081]
        MN1[Micronaut<br/>:8083]
    end
    
    subgraph Shared["⬡ Shared Core Layers"]
        DOM[Domain Layer<br/>Pure Java]
        APP[Application Layer<br/>Use Cases]
        INF[Infrastructure Layer<br/>JPA + Redis]
    end
    
    subgraph Data["💾 Data Layer"]
        MYSQL[(MySQL<br/>Database)]
        REDIS[(Redis<br/>Cache)]
    end
    
    WEB --> LB
    MOBILE -.-> LB
    API -.-> LB
    
    LB --> SB1 & QK1 & MN1
    
    SB1 & QK1 & MN1 --> DOM
    SB1 & QK1 & MN1 --> APP
    SB1 & QK1 & MN1 --> INF
    
    INF --> MYSQL & REDIS
    
    style Clients fill:none,stroke:#1976D2,stroke-width:2px,stroke-dasharray: 5 5
    style Gateway fill:none,stroke:#388E3C,stroke-width:2px,stroke-dasharray: 5 5
    style Backends fill:none,stroke:#1976D2,stroke-width:2px,stroke-dasharray: 5 5
    style Shared fill:none,stroke:#4caf50,stroke-width:4px
    style Data fill:none,stroke:#666,stroke-width:2px,stroke-dasharray: 5 5
    style DOM fill:none,stroke:#4caf50,stroke-width:3px
    style APP fill:none,stroke:#7B1FA2,stroke-width:2px
    style INF fill:none,stroke:#F57C00,stroke-width:2px
```

## 📊 Fluxo de Testes de Arquitetura

```mermaid
flowchart LR
    A[💻 Code Change] --> B[🔨 Maven Build]
    B --> C[🧪 Architecture Tests]
    
    subgraph Validation["Architecture Validation"]
        D{Hexagonal Rules<br/>44 tests}
        E{Naming Conventions}
        F{Layer Purity}
        G{Ports & Adapters}
    end
    
    C --> D & E & F & G
    
    D & E & F & G -->|All Pass| H[✅ Success]
    D & E & F & G -->|Any Fail| I[❌ Violation]
    
    H --> J[🚀 Build Success]
    I --> K[🛑 Build Failure]
    
    J --> L[📦 Deploy]
    K --> M[🔧 Fix Code]
    M --> A
    
    style A fill:none,stroke:#1976D2,stroke-width:2px
    style Validation fill:none,stroke:#7B1FA2,stroke-width:2px,stroke-dasharray: 5 5
    style H fill:none,stroke:#4caf50,stroke-width:3px
    style I fill:none,stroke:#f44336,stroke-width:3px
    style J fill:none,stroke:#4caf50,stroke-width:2px
    style K fill:none,stroke:#f44336,stroke-width:2px
    style L fill:none,stroke:#388E3C,stroke-width:2px
```

## 🎨 Padrão DTO vs Domain

```mermaid
flowchart LR
    subgraph Presentation["Presentation Layer"]
        DTO["CustomerDTO<br/>+ Jackson Annotations<br/>+ Validation<br/>+ Serialization"]
    end
    
    subgraph Mapper["Mapper Layer"]
        MAP["CustomerDTOMapper<br/>toDTO()<br/>toEntity()<br/>toCommand()"]
    end
    
    subgraph Domain["Domain Layer"]
        DOM["Customer Entity<br/>+ Business Logic<br/>+ Pure Java<br/>- No Frameworks<br/>- No Annotations"]
    end
    
    DTO -->|toEntity| MAP
    MAP -->|toDTO| DTO
    MAP -->|Pure Object| DOM
    DOM -->|Entity| MAP
    
    style Presentation fill:none,stroke:#1976D2,stroke-width:2px,stroke-dasharray: 5 5
    style Mapper fill:none,stroke:#7B1FA2,stroke-width:2px,stroke-dasharray: 5 5
    style Domain fill:none,stroke:#4caf50,stroke-width:4px
    style DTO fill:none,stroke:#1976D2,stroke-width:2px
    style MAP fill:none,stroke:#7B1FA2,stroke-width:2px
    style DOM fill:none,stroke:#4caf50,stroke-width:3px
```

---

## 📚 Legenda de Estilos

### Cores das Bordas

- 🟢 **Verde** (#4caf50): Domain Layer (Puro, sem frameworks)
- 🟣 **Roxo** (#7B1FA2): Application Layer / Ports (Interfaces)
- 🟠 **Laranja** (#F57C00): Infrastructure Layer (Driven Adapters)
- 🔵 **Azul** (#1976D2): Presentation Layer (Driving Adapters)
- 🔴 **Vermelho** (#f44336): Erros ou Violações

### Estilos de Linha

- **Sólida**: Componentes individuais
- **Pontilhada (5 5)**: Agrupamentos de camadas
- **Pontilhada (3 3)**: Sub-agrupamentos
- **Espessura 4px**: Hexagon Core (destaque)
- **Espessura 3px**: Entidades Domain
- **Espessura 2px**: Demais componentes

---

## 🔗 Links Relacionados

- [ARCHITECTURE.md](./ARCHITECTURE.md) - Documentação detalhada da arquitetura
- [ARCHITECTURE_TESTS_SUMMARY.md](./ARCHITECTURE_TESTS_SUMMARY.md) - Resumo dos testes
- [README.md](./README.md) - Guia principal do projeto

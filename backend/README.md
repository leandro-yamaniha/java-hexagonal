# Backend - Restaurant Management System

Backend Java do sistema de gerenciamento de restaurante, implementado com arquitetura hexagonal e suportando três frameworks diferentes.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Estrutura de Módulos](#estrutura-de-módulos)
- [Frameworks Suportados](#frameworks-suportados)
- [Documentação Detalhada](#documentação-detalhada)
- [Como Começar](#como-começar)

## 🎯 Visão Geral

Este é o backend do sistema, construído com **arquitetura hexagonal pura** (ports and adapters), permitindo total independência de frameworks nas camadas de domínio e aplicação.

### Características Principais

- ✅ **Arquitetura Hexagonal** - Separação completa de preocupações
- ✅ **3 Frameworks** - Spring Boot, Quarkus e Micronaut
- ✅ **Domain-Driven Design** - Domínio puro, sem dependências externas
- ✅ **DTOs Pattern** - Separação entre domínio e apresentação
- ✅ **Architecture Tests** - 75+ testes validando a arquitetura
- ✅ **Hot Reload** - Desenvolvimento rápido com live reload

## 📦 Estrutura de Módulos

```
backend/
├── domain/                 # 🔵 Camada de Domínio
│   └── entities, value objects, domain events
├── application/            # 🟣 Camada de Aplicação
│   └── use cases, ports, DTOs
├── infrastructure/         # 🟡 Camada de Infraestrutura
│   └── JPA, Redis, implementações
├── spring-boot-app/       # 🟢 Spring Boot (port 8082)
├── quarkus-app/           # 🟢 Quarkus (port 8081)
├── micronaut-app/         # 🟢 Micronaut (port 8083)
└── architecture-tests/     # 🧪 Testes de Arquitetura
```

## 🚀 Frameworks Suportados

| Framework | Port | Startup | Servidor | Virtual Threads |
|-----------|------|---------|----------|-----------------|
| **Quarkus** | 8081 | ~1.1s ⚡ | Vert.x | ⚠️ Experimental |
| **Spring Boot** | 8082 | ~2.5-4s 🚀 | Undertow | ✅ Nativo |
| **Micronaut** | 8083 | ~3-5s | Netty | 🔴 Básico |

## 📚 Documentação Detalhada

Toda a documentação técnica do backend está centralizada em **[../docs/](../docs/)**:

### Arquitetura

- **[ARCHITECTURE.md](../docs/ARCHITECTURE.md)** - Documentação completa da arquitetura hexagonal
- **[ARCHITECTURE_DIAGRAMS.md](../docs/ARCHITECTURE_DIAGRAMS.md)** - Diagramas visuais da arquitetura
- **[ARCHITECTURE_STATUS_REPORT.md](../docs/ARCHITECTURE_STATUS_REPORT.md)** - Relatório de status da arquitetura
- **[ARCHITECTURE_TESTS_SUMMARY.md](../docs/ARCHITECTURE_TESTS_SUMMARY.md)** - Resumo dos testes arquiteturais

### Padrões e Implementação

- **[DTO_PATTERN_GUIDE.md](../docs/DTO_PATTERN_GUIDE.md)** - Guia completo do padrão DTO
- **[DTO_IMPLEMENTATION_COMPLETE.md](../docs/DTO_IMPLEMENTATION_COMPLETE.md)** - Status de implementação dos DTOs
- **[IMPLEMENTATION_SUMMARY.md](../docs/IMPLEMENTATION_SUMMARY.md)** - Resumo geral da implementação

### Frameworks

- **[FRAMEWORK_ARCHITECTURE_TESTS.md](../docs/FRAMEWORK_ARCHITECTURE_TESTS.md)** - Testes de arquitetura dos frameworks
- **[FRAMEWORK_STRUCTURE_TESTS_SUMMARY.md](../docs/FRAMEWORK_STRUCTURE_TESTS_SUMMARY.md)** - Resumo dos testes de estrutura
- **[QUARKUS_VALIDATION_REPORT.md](../docs/QUARKUS_VALIDATION_REPORT.md)** - Relatório de validação do Quarkus

### Performance

- **[STARTUP_TIMES.md](../docs/STARTUP_TIMES.md)** - Análise comparativa de tempos de startup

## 🏃 Como Começar

### ⚡ Backend Auto-Contido

O backend é **completamente independente** e não requer nenhum arquivo Maven externo:

- ✅ **Sem parent POM externo** - Todas as configurações estão no `pom.xml` do backend
- ✅ **Auto-suficiente** - Contém todas as versões e dependências necessárias
- ✅ **Pode ser movido** - Funciona em qualquer lugar como módulo standalone

### Compilar Todos os Módulos

```bash
cd backend
mvn clean install
```

### Iniciar Frameworks Individualmente

```bash
# Na raiz do projeto
./scripts/start-quarkus.sh      # Port 8081
./scripts/start-spring-boot.sh  # Port 8082
./scripts/start-micronaut.sh    # Port 8083
```

### Modo Desenvolvimento (Hot Reload)

```bash
./scripts/start-dev-mode.sh
```

## 🧪 Executar Testes

### Todos os Testes

```bash
mvn test
```

### Testes de Arquitetura

```bash
cd architecture-tests
mvn test
```

### Testes por Módulo

```bash
cd domain && mvn test
cd application && mvn test
cd infrastructure && mvn test
```

## 🔌 APIs e Endpoints

Todos os frameworks expõem os mesmos endpoints:

### Swagger UI

- **Quarkus**: http://localhost:8081/q/swagger-ui
- **Spring Boot**: http://localhost:8082/swagger-ui.html
- **Micronaut**: http://localhost:8083/swagger-ui

### Endpoints Principais

```
GET    /api/customers           # Listar clientes
POST   /api/customers           # Criar cliente
GET    /api/customers/{id}      # Buscar cliente
PUT    /api/customers/{id}      # Atualizar cliente
DELETE /api/customers/{id}      # Deletar cliente

GET    /api/menu-items          # Listar itens do menu
POST   /api/menu-items          # Criar item
GET    /api/menu-items/{id}     # Buscar item
PUT    /api/menu-items/{id}     # Atualizar item
DELETE /api/menu-items/{id}     # Deletar item
```

## 🏗️ Camadas da Arquitetura

### Domain Layer (Pura)
- Entidades de negócio
- Value Objects
- Domain Events
- **Zero dependências externas**

### Application Layer
- Use Cases
- Interfaces de Portas (Ports)
- DTOs
- Orquestração de lógica

### Infrastructure Layer
- Implementações JPA
- Repositórios
- Cache (Redis)
- Adaptadores externos

### Framework Layer
- Controllers REST
- Configurações específicas
- Mapeadores DTO
- Exception Handlers

## 🎓 Conceitos Implementados

- **Hexagonal Architecture** (Ports & Adapters)
- **Domain-Driven Design** (DDD)
- **SOLID Principles**
- **Clean Architecture**
- **DTO Pattern**
- **Repository Pattern**
- **Dependency Inversion**

## 📖 Referências Externas

Para informações gerais do projeto, consulte:
- [README principal](../README.md)
- [Estrutura do projeto](../PROJECT_STRUCTURE.md)
- [Setup e configuração](../SETUP.md)
- [Sumário executivo](../EXECUTIVE_SUMMARY.md)

---

**Desenvolvido com Arquitetura Hexagonal** 🏛️

# 📚 Reference Documentation

Complete reference guide for all technologies, frameworks, and tools used in this project.

---

## ☕ Java & Core Technologies

| Technology | Version | Documentation | Description |
|------------|---------|---------------|-------------|
| **Java** | 21 | [Oracle Java 21 Docs](https://docs.oracle.com/en/java/javase/21/) | Core programming language with Virtual Threads support |
| **Maven** | 3.9+ | [Maven Documentation](https://maven.apache.org/guides/) | Build automation and dependency management |
| **JPA/Hibernate** | 6.x | [Hibernate ORM Documentation](https://hibernate.org/orm/documentation/6.5/) | Object-relational mapping framework |
| **MySQL** | 8.0 | [MySQL 8.0 Reference Manual](https://dev.mysql.com/doc/refman/8.0/en/) | Relational database management system |
| **Redis** | Latest | [Redis Documentation](https://redis.io/docs/) | In-memory data structure store for caching |

### Java 21 Features Used

- **Virtual Threads (Project Loom)**: Lightweight threads for better concurrency
- **Pattern Matching**: Enhanced switch expressions
- **Records**: Immutable data carriers
- **Sealed Classes**: Restricted class hierarchies

**Resources**:
- [Java 21 Release Notes](https://www.oracle.com/java/technologies/javase/21-relnote-issues.html)
- [Virtual Threads Guide](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html)

---

## 🚀 Backend Frameworks

### Spring Boot

| Resource | Link | Description |
|----------|------|-------------|
| **Official Documentation** | [Spring Boot Docs](https://docs.spring.io/spring-boot/index.html) | Complete framework documentation |
| **Reference Guide** | [Spring Boot Reference](https://docs.spring.io/spring-boot/reference/) | Detailed reference manual |
| **Getting Started** | [Spring Guides](https://spring.io/guides) | Step-by-step tutorials |
| **API Documentation** | [Spring Boot API](https://docs.spring.io/spring-boot/api/java/) | JavaDoc API reference |

**Version**: 3.5.6  
**Web Server**: Undertow (high-performance NIO)  
**Port**: 8082

**Key Features**:
- Native Virtual Threads support
- Actuator for monitoring
- Auto-configuration
- Extensive ecosystem

### Quarkus

| Resource | Link | Description |
|----------|------|-------------|
| **Official Documentation** | [Quarkus Guides](https://quarkus.io/guides/) | Comprehensive guides |
| **Getting Started** | [Getting Started Guide](https://quarkus.io/get-started/) | Quick start tutorial |
| **Extensions** | [Quarkus Extensions](https://quarkus.io/extensions/) | Available extensions |
| **Dev UI** | [Dev UI Guide](https://quarkus.io/guides/dev-ui) | Development tools |

**Version**: 3.15.1  
**Web Server**: Eclipse Vert.x (reactive/event-driven)  
**Port**: 8081

**Key Features**:
- Supersonic startup time (~2-4s)
- Reactive programming support
- Native compilation (GraalVM)
- Dev UI for development

### Micronaut

| Resource | Link | Description |
|----------|------|-------------|
| **Official Documentation** | [Micronaut Documentation](https://docs.micronaut.io/latest/guide/) | Complete framework guide |
| **Quick Start** | [Quick Start Guide](https://guides.micronaut.io/latest/micronaut-creating-first-graal-app.html) | Getting started tutorial |
| **Guides** | [Micronaut Guides](https://guides.micronaut.io/) | Practical guides |
| **API Docs** | [Micronaut API](https://docs.micronaut.io/latest/api/) | JavaDoc reference |

**Version**: 4.6.3  
**Web Server**: Netty (asynchronous NIO)  
**Port**: 8083

**Key Features**:
- Low memory footprint
- Fast startup time
- Compile-time DI
- Cloud-native features

---

## 🎨 Frontend Technologies

### Angular

| Resource | Link | Description |
|----------|------|-------------|
| **Official Documentation** | [Angular Documentation](https://angular.dev/overview) | Complete Angular guide |
| **Getting Started** | [Angular Tutorial](https://angular.dev/tutorials) | Step-by-step tutorials |
| **API Reference** | [Angular API](https://angular.dev/api) | Complete API documentation |
| **CLI** | [Angular CLI](https://angular.dev/cli) | Command-line interface guide |

**Version**: 18+  
**Language**: TypeScript  
**UI Framework**: Angular Material

**Key Features**:
- Component-based architecture
- TypeScript support
- Reactive forms
- HTTP client
- Routing

### TypeScript

| Resource | Link | Description |
|----------|------|-------------|
| **Official Documentation** | [TypeScript Docs](https://www.typescriptlang.org/docs/) | Complete TypeScript guide |
| **Handbook** | [TypeScript Handbook](https://www.typescriptlang.org/docs/handbook/intro.html) | Comprehensive handbook |
| **Playground** | [TypeScript Playground](https://www.typescriptlang.org/play) | Online code editor |

### npm

| Resource | Link | Description |
|----------|------|-------------|
| **Official Documentation** | [npm Documentation](https://docs.npmjs.com/) | Package manager docs |
| **CLI Commands** | [npm CLI](https://docs.npmjs.com/cli/) | Command-line reference |

---

## 🏗️ Architecture & Patterns

### Hexagonal Architecture

| Resource | Link | Description |
|----------|------|-------------|
| **Original Article** | [Alistair Cockburn's Article](https://alistair.cockburn.us/hexagonal-architecture/) | Original hexagonal architecture concept |
| **Ports and Adapters** | [Herberto Graca's Blog](https://herbertograca.com/2017/09/14/ports-adapters-architecture/) | Detailed explanation of ports and adapters |
| **Clean Architecture** | [Robert C. Martin's Blog](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) | Clean architecture principles |

**Key Concepts**:
- **Ports**: Interfaces that define how the application interacts with the outside world
- **Adapters**: Implementations that connect ports to external systems
- **Domain**: Pure business logic without framework dependencies
- **Dependency Inversion**: Dependencies point inward toward the domain

### Testing & Validation

| Resource | Link | Description |
|----------|------|-------------|
| **ArchUnit** | [ArchUnit User Guide](https://www.archunit.org/userguide/html/000_Index.html) | Architecture testing framework |
| **JUnit 5** | [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/) | Unit testing framework |
| **Mockito** | [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html) | Mocking framework |

---

## 🐳 DevOps & Tools

### Docker

| Resource | Link | Description |
|----------|------|-------------|
| **Official Documentation** | [Docker Documentation](https://docs.docker.com/) | Complete Docker guide |
| **Docker Compose** | [Compose Specification](https://docs.docker.com/compose/) | Multi-container applications |
| **Best Practices** | [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/) | Development best practices |
| **Dockerfile Reference** | [Dockerfile Reference](https://docs.docker.com/reference/dockerfile/) | Dockerfile syntax |

**Used For**:
- MySQL database container
- Redis cache container
- Nginx reverse proxy
- Complete infrastructure setup

### Git

| Resource | Link | Description |
|----------|------|-------------|
| **Official Documentation** | [Git Documentation](https://git-scm.com/doc) | Complete Git guide |
| **Pro Git Book** | [Pro Git](https://git-scm.com/book/en/v2) | Free comprehensive book |
| **Git Reference** | [Git Reference](https://git-scm.com/docs) | Command reference |

---

## 📊 Database & Caching

### MySQL

| Resource | Link | Description |
|----------|------|-------------|
| **Reference Manual** | [MySQL 8.0 Reference](https://dev.mysql.com/doc/refman/8.0/en/) | Complete MySQL documentation |
| **Tutorial** | [MySQL Tutorial](https://dev.mysql.com/doc/refman/8.0/en/tutorial.html) | Getting started tutorial |
| **SQL Statements** | [SQL Statement Syntax](https://dev.mysql.com/doc/refman/8.0/en/sql-statements.html) | SQL syntax reference |

**Configuration**:
- **Database**: `restaurant_db`
- **User**: `restaurant_user`
- **Port**: 3306
- **Charset**: UTF-8

### Redis

| Resource | Link | Description |
|----------|------|-------------|
| **Official Documentation** | [Redis Documentation](https://redis.io/docs/) | Complete Redis guide |
| **Commands** | [Redis Commands](https://redis.io/commands/) | Command reference |
| **Data Types** | [Redis Data Types](https://redis.io/docs/data-types/) | Available data structures |

**Used For**:
- Customer data caching
- Menu item caching
- Session storage (future)

---

## 🔧 Build & Development Tools

### Maven

| Resource | Link | Description |
|----------|------|-------------|
| **Official Documentation** | [Maven Documentation](https://maven.apache.org/guides/) | Complete Maven guide |
| **POM Reference** | [POM Reference](https://maven.apache.org/pom.html) | POM file syntax |
| **Plugins** | [Maven Plugins](https://maven.apache.org/plugins/) | Available plugins |

**Key Plugins Used**:
- `maven-compiler-plugin`: Java compilation
- `maven-surefire-plugin`: Unit tests
- `spring-boot-maven-plugin`: Spring Boot packaging
- `quarkus-maven-plugin`: Quarkus development
- `maven-shade-plugin`: Micronaut packaging

---

## 📖 Project-Specific Documentation

### Internal Documentation

| Document | Location | Description |
|----------|----------|-------------|
| **Executive Summary** | [docs/EXECUTIVE_SUMMARY.md](./EXECUTIVE_SUMMARY.md) | Complete project overview |
| **Setup Guide** | [docs/SETUP.md](./SETUP.md) | Installation and configuration |
| **Architecture** | [docs/backend/ARCHITECTURE.md](./backend/ARCHITECTURE.md) | Detailed architecture |
| **Architecture Diagrams** | [docs/backend/ARCHITECTURE_DIAGRAMS.md](./backend/ARCHITECTURE_DIAGRAMS.md) | Visual diagrams |
| **API Documentation** | [docs/backend/API_DOCUMENTATION.md](./backend/API_DOCUMENTATION.md) | Complete API reference |
| **DTO Pattern Guide** | [docs/backend/DTO_PATTERN_GUIDE.md](./backend/DTO_PATTERN_GUIDE.md) | DTO pattern implementation |
| **Troubleshooting** | [docs/backend/TROUBLESHOOTING.md](./backend/TROUBLESHOOTING.md) | Common issues and solutions |
| **Startup Times** | [docs/backend/STARTUP_TIMES.md](./backend/STARTUP_TIMES.md) | Performance comparison |

---

## 🌐 Additional Resources

### Learning Resources

- **Java Virtual Threads**: [JEP 444](https://openjdk.org/jeps/444)
- **Reactive Programming**: [Reactive Manifesto](https://www.reactivemanifesto.org/)
- **Domain-Driven Design**: [DDD Reference](https://www.domainlanguage.com/ddd/reference/)
- **Microservices**: [Martin Fowler's Guide](https://martinfowler.com/microservices/)

### Community

- **Spring Community**: [Spring Community](https://spring.io/community)
- **Quarkus Community**: [Quarkus Community](https://quarkus.io/community/)
- **Micronaut Community**: [Micronaut Community](https://micronaut.io/community/)

---

**Last Updated**: 2025-10-02  
**Maintained By**: Project Team  
**Status**: Active ✅

# 📁 Estrutura do Projeto

## 📋 Visão Geral

Este projeto utiliza uma **estrutura polyglot moderna**, onde cada tecnologia (Java, TypeScript, Docker) é auto-contida e independente.

```
restaurant-management/
├── backend/                      # 🔧 Backend Java (auto-contido)
│   ├── domain/                   # Domain layer (pure Java)
│   ├── application/              # Application layer (use cases)
│   ├── infrastructure/           # Infrastructure layer (JPA, Redis)
│   ├── architecture-tests/       # Architecture tests
│   ├── quarkus-app/             # Quarkus implementation (port 8081)
│   ├── micronaut-app/           # Micronaut implementation (port 8083)
│   ├── spring-boot-app/         # Spring Boot implementation (port 8082)
│   ├── pom.xml                  # Backend ROOT POM (auto-contido)
│   ├── README.md                # Backend documentation
│   └── *.md                     # Backend technical docs
├── frontend-angular/             # 🎨 Angular Frontend (auto-contido)
│   └── package.json             # NPM dependencies
├── docker/                       # 🐳 Docker configurations (MySQL, Redis)
├── scripts/                      # 📜 Automation scripts
└── *.md                          # 📚 General documentation
```
## 📦 Módulos Backend

### Camada de Domínio (`domain/`)
- Entidades de negócio puras
- Value objects
- Sem dependências externas

### Camada de Aplicação (`application/`)
- Use cases
- Interfaces de portas (ports)
- DTOs de entrada/saída
- Lógica de orquestração

### Camada de Infraestrutura (`infrastructure/`)
- Implementações de persistência (JPA)
- Cache (Redis)
- Adaptadores externos
- Mapeadores de entidades

### Implementações de Framework

#### Spring Boot (`spring-boot-app/`)
- **Porta**: 8082
- **Servidor**: Undertow
- **Startup**: ~2.5-4s
- **Características**: Virtual Threads habilitadas, ecosystem maduro

#### Quarkus (`quarkus-app/`)
- **Porta**: 8081
- **Servidor**: Vert.x
- **Startup**: ~1.1s ⚡
- **Características**: Cloud-native, supersônico

#### Micronaut (`micronaut-app/`)
- **Porta**: 8083
- **Servidor**: Netty
- **Startup**: ~3-5s
- **Características**: AOT compilation, baixo uso de memória

### Testes de Arquitetura (`architecture-tests/`)
- Validação de hexagonal architecture
- Testes de dependências entre camadas
- Verificação de convenções de código

## 🚀 Como Usar

### Compilar Todo o Backend

O backend é **totalmente auto-contido** e não depende de nenhum POM externo:

```bash
cd backend
mvn clean install
```

### Iniciar Aplicações
```bash
# Quarkus
./scripts/start-quarkus.sh

# Spring Boot
./scripts/start-spring-boot.sh

# Micronaut
./scripts/start-micronaut.sh
```

### Modo Desenvolvimento
```bash
./scripts/start-dev-mode.sh
```

## 🔄 Migração

### Antes
```
restaurant-management/
├── domain/
├── application/
├── infrastructure/
├── quarkus-app/
├── spring-boot-app/
└── micronaut-app/
```

### Depois
```
restaurant-management/
├── backend/
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   ├── quarkus-app/
│   ├── spring-boot-app/
│   └── micronaut-app/
└── frontend-angular/
```

## ✅ Características Auto-Contidas

### Backend (Java/Maven)
- **Sem parent POM externo** - Todas configurações em `backend/pom.xml`
- **Auto-suficiente** - Contém todas versões e dependências
- **Portável** - Funciona em qualquer lugar
- **Build**: `cd backend && mvn clean install`

### Frontend (TypeScript/NPM)
- **NPM puro** - Usa `package.json`
- **Angular CLI** - Ferramentas próprias
- **Build**: `cd frontend-angular && npm install && npm start`

## 📝 Benefícios da Estrutura Polyglot

1. **Separação Clara**: Cada tecnologia em sua pasta própria
2. **Auto-Contido**: Backend e frontend completamente independentes
3. **CI/CD Friendly**: Builds independentes e paralelos
4. **Escalável**: Fácil adicionar Python, Go, mobile apps, etc
5. **Manutenibilidade**: Código organizado e fácil navegação
6. **Sem Confusão**: Estrutura clara para projeto multi-tecnologia

## 🔗 Referências

### Documentação Geral
- [README.md](README.md) - Documento principal do projeto
- [EXECUTIVE_SUMMARY.md](EXECUTIVE_SUMMARY.md) - Resumo executivo
- [SETUP.md](SETUP.md) - Guia de configuração
- [scripts/README.md](scripts/README.md) - Documentação dos scripts

### Documentação Backend
Toda documentação específica do backend está em **[backend/](backend/)**:

- [backend/README.md](backend/README.md) - Índice completo da documentação backend
- [backend/ARCHITECTURE.md](backend/ARCHITECTURE.md) - Arquitetura hexagonal detalhada
- [backend/ARCHITECTURE_DIAGRAMS.md](backend/ARCHITECTURE_DIAGRAMS.md) - Diagramas visuais
- [backend/DTO_PATTERN_GUIDE.md](backend/DTO_PATTERN_GUIDE.md) - Guia de padrão DTO
- [backend/STARTUP_TIMES.md](backend/STARTUP_TIMES.md) - Comparação de performance
- E mais 6 documentos técnicos...

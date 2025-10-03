# 🐳 Docker Infrastructure

Infraestrutura completa com Docker para o Restaurant Management System.

## 📋 Visão Geral

Esta infraestrutura fornece:

- **Nginx**: Reverse proxy e load balancer na porta 80
- **Backend Escalável**: Cada framework pode ter até 2 instâncias
- **MySQL**: Banco de dados compartilhado
- **Redis**: Cache compartilhado
- **Frontend**: Servido estaticamente pelo Nginx

## 🏗️ Arquitetura

### C4 Model - Container Diagram

```mermaid
C4Context
    title Container Diagram - Restaurant Management System

    Person(user, "Cliente", "Usuário do sistema via browser")
    
    System_Boundary(frontend, "Frontend Layer") {
        Container(nginx_frontend, "Nginx Frontend", "Nginx:alpine", "Serve aplicação Angular e faz proxy para APIs")
    }
    
    System_Boundary(api_layer, "API Gateway Layer") {
        Container(nginx_api, "Nginx API Gateway", "Nginx:alpine", "Load balancer e roteamento de APIs")
    }
    
    System_Boundary(backend, "Backend Layer") {
        Container(backend_1, "Backend Instance 1", "Spring/Quarkus/Micronaut", "Processa requisições de negócio")
        Container(backend_2, "Backend Instance 2", "Spring/Quarkus/Micronaut", "Processa requisições de negócio")
    }
    
    System_Boundary(data, "Data Layer") {
        ContainerDb(mysql, "MySQL Database", "MySQL 8.0", "Armazena dados persistentes")
        ContainerDb(redis, "Redis Cache", "Redis 7", "Cache de dados em memória")
    }
    
    Rel(user, nginx_frontend, "Acessa aplicação", "HTTPS/HTTP")
    Rel(nginx_frontend, nginx_api, "Proxy /api/*", "HTTP")
    Rel(nginx_api, backend_1, "Load balance", "HTTP")
    Rel(nginx_api, backend_2, "Load balance", "HTTP")
    Rel(backend_1, mysql, "Lê/Escreve dados", "JDBC")
    Rel(backend_2, mysql, "Lê/Escreve dados", "JDBC")
    Rel(backend_1, redis, "Cache", "Redis Protocol")
    Rel(backend_2, redis, "Cache", "Redis Protocol")
    
    UpdateLayoutConfig($c4ShapeInRow="3", $c4BoundaryInRow="2")
```

> 📘 **C4 Model**: Diagrama de Containers mostrando os principais componentes executáveis e suas interações

### Arquitetura Simplificada

```mermaid
graph TB
    User([👤 Cliente Browser])
    NginxFE[Nginx Frontend<br/>:80<br/>Serve Angular + Proxy]
    NginxAPI[Nginx API Gateway<br/>:8082/8081/8083<br/>Load Balancer]
    
    subgraph Backend["Backend Layer - 2 Instances"]
        B1[Backend Instance 1<br/>:8080]
        B2[Backend Instance 2<br/>:8080]
    end
    
    subgraph DataLayer["Data Layer"]
        DB[(MySQL<br/>:3306<br/>Persistent Data)]
        Cache[(Redis<br/>:6379<br/>Cache)]
    end
    
    User -->|HTTP/HTTPS| NginxFE
    NginxFE -->|/api/*| NginxAPI
    NginxAPI -.Round Robin.-> B1
    NginxAPI -.Round Robin.-> B2
    B1 & B2 -->|JDBC| DB
    B1 & B2 -->|Cache| Cache
    
    style NginxFE fill:none,stroke:#1976D2,stroke-width:2px
    style NginxAPI fill:none,stroke:#388E3C,stroke-width:2px
    style Backend fill:none,stroke:#F57C00,stroke-width:2px,stroke-dasharray: 5 5
    style DataLayer fill:none,stroke:#7B1FA2,stroke-width:2px,stroke-dasharray: 5 5
    style B1 fill:none,stroke:#F57C00,stroke-width:2px
    style B2 fill:none,stroke:#F57C00,stroke-width:2px
```

## 📚 Documentação por Backend

### Comparação de Componentes

| Tipo | Componente | Startup Time | Memory Usage | Web Server | Documentação |
|------|------------|--------------|--------------|------------|--------------|
| Backend | **☕ Spring Boot** | ~2.5-4s | ~720-830 MB | Undertow | **[📖 README-spring-boot.md](README-spring-boot.md)** |
| Backend | **⚡ Quarkus** | ~1.158s ⚡ | ~580-670 MB | Eclipse Vert.x | **[📖 README-quarkus.md](README-quarkus.md)** |
| Backend | **🔥 Micronaut** | ~3-5s | ~495-515 MB 🏆 | Netty | **[📖 README-micronaut.md](README-micronaut.md)** |
| Frontend | **🎨 Angular** | N/A | ~5 MB | Nginx (static) | **[📖 README-frontend.md](README-frontend.md)** |

> 🏆 **Micronaut**: Melhor eficiência de memória  
> ⚡ **Quarkus**: Startup mais rápido  
> ☕ **Spring Boot**: Ecossistema mais maduro


⚠️ **Frontend**: Build necessário antes de iniciar! Use `./build-frontend.sh`

## 🌐 Nginx Architecture

A infraestrutura utiliza **arquitetura dual Nginx**:
- **Nginx Frontend** (port 80): Serve Angular + Proxy APIs
- **Nginx API Gateway** (ports 8081/8082/8083): Load balancer por backend

📚 **Documentação completa**: [nginx/README.md](nginx/README.md)

## 🚀 Como Usar

### 1. Iniciar TODOS os Backends (6 instâncias)

```bash
cd docker-infrastructure
./start-all.sh
```

### 2. Iniciar Apenas UM Backend Específico

```bash
# Apenas Spring Boot (2 instâncias)
./start-spring.sh

# Apenas Quarkus (2 instâncias)
./start-quarkus.sh

# Apenas Micronaut (2 instâncias)
./start-micronaut.sh
```

### 3. Uso Manual com docker-compose

```bash
# Spring Boot apenas
docker-compose -f docker-compose.yml -f docker-compose.spring.yml up -d

# Quarkus apenas
docker-compose -f docker-compose.yml -f docker-compose.quarkus.yml up -d

# Micronaut apenas
docker-compose -f docker-compose.yml -f docker-compose.micronaut.yml up -d

# Combinar múltiplos backends
docker-compose -f docker-compose.yml \
               -f docker-compose.spring.yml \
               -f docker-compose.quarkus.yml \
               up -d
```

### 4. Ver Logs

```bash
# Todos os serviços
docker-compose logs -f

# Serviço específico
docker-compose logs -f nginx
docker-compose logs -f spring-boot-app-1
docker-compose logs -f mysql
```

### 5. Parar Serviços

```bash
# Parar todos
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

### 6. Verificar Status

```bash
docker-compose ps
```

## 🌐 Endpoints

### Frontend
- **URL**: http://localhost
- Servido estaticamente pelo Nginx

### Backend APIs (via Nginx)

| Framework | Endpoint | Backend Instances |
|-----------|----------|-------------------|
| **Spring Boot** | http://localhost/api/spring/* | 2 instâncias |
| **Quarkus** | http://localhost/api/quarkus/* | 2 instâncias |
| **Micronaut** | http://localhost/api/micronaut/* | 2 instâncias |

### Exemplos de Requisições

```bash
# Spring Boot - Load balanced entre 2 instâncias
curl http://localhost/api/spring/customers

# Quarkus - Load balanced entre 2 instâncias
curl http://localhost/api/quarkus/customers

# Micronaut - Load balanced entre 2 instâncias
curl http://localhost/api/micronaut/customers
```

### Serviços Diretos (sem Nginx)

| Serviço | Porta | URL |
|---------|-------|-----|
| **MySQL** | 3306 | localhost:3306 |
| **Redis** | 6379 | localhost:6379 |

## 📦 Estrutura de Pastas (Modular)

```
docker-infrastructure/
├── docker-compose.yml              # Base: MySQL, Redis, Nginx
├── docker-compose.spring.yml      # Spring Boot (2 instâncias)
├── docker-compose.quarkus.yml     # Quarkus (2 instâncias)
├── docker-compose.micronaut.yml   # Micronaut (2 instâncias)
│
├── start-all.sh                   # Inicia TODOS os backends
├── start-spring.sh                # Inicia apenas Spring Boot
├── start-quarkus.sh               # Inicia apenas Quarkus
├── start-micronaut.sh             # Inicia apenas Micronaut
├── stop-all.sh                    # Para tudo
├── scale-backend.sh               # Script genérico
│
├── nginx/
│   └── nginx.conf                 # Configuração Nginx (load balancer)
├── mysql/
│   └── init.sql                   # Script de inicialização do banco
├── backend/
│   ├── Dockerfile.spring-boot
│   ├── Dockerfile.quarkus
│   └── Dockerfile.micronaut
└── frontend/
    └── dist/                      # Build do Angular (copiar aqui)
```

### 🎯 Arquitetura Modular

A infraestrutura é **modular**: cada backend tem seu próprio arquivo `docker-compose`. Isso permite:

- ✅ Iniciar apenas o backend que você precisa
- ✅ Economizar recursos (não precisa rodar todos)
- ✅ Testes isolados por framework
- ✅ Combinar backends conforme necessário
- ✅ Manutenção simplificada

**Exemplo**: Se você só trabalha com Spring Boot, use apenas `docker-compose.spring.yml`!

## 🔧 Configuração

### Variáveis de Ambiente

Todas as configurações podem ser ajustadas no `docker-compose.yml`:

**MySQL**:
- `MYSQL_ROOT_PASSWORD`: root123
- `MYSQL_DATABASE`: restaurant_db
- `MYSQL_USER`: restaurant_user
- `MYSQL_PASSWORD`: restaurant123

**Backend**:
- Configurado para conectar automaticamente ao MySQL e Redis
- Porta interna: 8080 (não exposta externamente)
- Acesso apenas via Nginx na porta 80

### Load Balancing

O Nginx usa **round-robin** por padrão para distribuir requisições entre as instâncias.

Configuração em `nginx/nginx.conf`:
```nginx
upstream backend_spring {
    server spring-boot-app-1:8080;
    server spring-boot-app-2:8080;
}
```

## 🧪 Testando Load Balancing

```bash
# Fazer múltiplas requisições e ver distribuição
for i in {1..10}; do
  curl http://localhost/api/spring/customers
  echo "Request $i"
  sleep 1
done
```

Verifique nos logs qual instância respondeu:
```bash
docker-compose logs -f spring-boot-app-1 spring-boot-app-2
```

## 📊 Monitoramento

### Health Checks

```bash
# Nginx health
curl http://localhost/health

# Ver status dos containers
docker-compose ps

# Ver recursos utilizados
docker stats
```

### Logs Centralizados

```bash
# Todos os backends
docker-compose logs -f spring-boot-app-1 spring-boot-app-2 \
                       quarkus-app-1 quarkus-app-2 \
                       micronaut-app-1 micronaut-app-2
```

## 🛠️ Troubleshooting

### Problema: Container não inicia

```bash
# Verificar logs
docker-compose logs [service-name]

# Reconstruir imagem
docker-compose build --no-cache [service-name]
```

### Problema: Banco de dados não conecta

```bash
# Verificar se MySQL está rodando
docker-compose ps mysql

# Testar conexão
docker-compose exec mysql mysql -u restaurant_user -prestaurant123 restaurant_db
```

### Problema: Nginx não acessa backend

```bash
# Verificar configuração
docker-compose exec nginx cat /etc/nginx/nginx.conf

# Testar conectividade
docker-compose exec nginx ping spring-boot-app-1
```

## 🔄 Atualizar Aplicações

```bash
# Rebuild e restart
docker-compose up -d --build [service-name]

# Exemplo: Atualizar apenas Spring Boot
docker-compose up -d --build spring-boot-app-1 spring-boot-app-2
```

## 🎯 Próximos Passos

1. **Copiar build do frontend**: `cp -r ../frontend-angular/dist docker-infrastructure/frontend/`
2. **Ajustar Dockerfiles** se necessário para seu ambiente
3. **Configurar CI/CD** para builds automatizados
4. **Adicionar monitoring** (Prometheus + Grafana)
5. **Configurar SSL** para HTTPS

## 📝 Notas Importantes

- ⚠️ Esta configuração é para **desenvolvimento/teste**
- 🔒 Para **produção**, adicione SSL, secrets management, e hardening
- 📈 O load balancing é básico (round-robin). Para produção, considere algoritmos mais sofisticados
- 💾 Os dados do MySQL são persistidos em volume Docker
- 🔄 As instâncias backend compartilham o mesmo banco de dados e cache

## 🚀 Deploy em Produção

Para produção, considere:
- Usar Docker Swarm ou Kubernetes
- Adicionar secrets management
- Configurar SSL/TLS
- Implementar monitoring e alerting
- Adicionar backup automatizado do MySQL
- Usar imagens multi-stage otimizadas
- Configurar resource limits

---

**Documentação completa**: [../docs/](../docs/)

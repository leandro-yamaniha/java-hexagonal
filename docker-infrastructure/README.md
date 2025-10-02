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

```
┌─────────────────────────────────────────────┐
│           Cliente (Browser)                  │
└─────────────────┬───────────────────────────┘
                  │ HTTP (porta 80)
                  ▼
┌─────────────────────────────────────────────┐
│              Nginx                           │
│  - Serve frontend estático                   │
│  - Load balancer para backends               │
└──────┬──────────────────┬──────────────────┘
       │                  │
       ▼                  ▼
┌──────────────┐    ┌──────────────┐
│ Spring Boot  │    │ Spring Boot  │
│  Instance 1  │    │  Instance 2  │
│  (port 8080) │    │  (port 8080) │
└──────┬───────┘    └──────┬───────┘
       │                   │
┌──────────────┐    ┌──────────────┐
│   Quarkus    │    │   Quarkus    │
│  Instance 1  │    │  Instance 2  │
│  (port 8080) │    │  (port 8080) │
└──────┬───────┘    └──────┬───────┘
       │                   │
┌──────────────┐    ┌──────────────┐
│  Micronaut   │    │  Micronaut   │
│  Instance 1  │    │  Instance 2  │
│  (port 8080) │    │  (port 8080) │
└──────┬───────┘    └──────┬───────┘
       │                   │
       ▼                   ▼
┌─────────────────────────────────┐
│  MySQL (port 3306)               │
│  Redis (port 6379)               │
└─────────────────────────────────┘
```

## 🚀 Como Usar

### 1. Construir e Iniciar Todos os Serviços

```bash
cd docker-infrastructure
docker-compose up --build
```

### 2. Iniciar em Background

```bash
docker-compose up -d
```

### 3. Escalar Apenas Backend Específico

```bash
# Apenas Spring Boot (2 instâncias)
docker-compose up -d spring-boot-app-1 spring-boot-app-2

# Apenas Quarkus (2 instâncias)
docker-compose up -d quarkus-app-1 quarkus-app-2

# Apenas Micronaut (2 instâncias)
docker-compose up -d micronaut-app-1 micronaut-app-2
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

## 📦 Estrutura de Pastas

```
docker-infrastructure/
├── docker-compose.yml         # Configuração principal
├── nginx/
│   └── nginx.conf            # Configuração Nginx (load balancer)
├── mysql/
│   └── init.sql              # Script de inicialização do banco
├── backend/
│   ├── Dockerfile.spring-boot
│   ├── Dockerfile.quarkus
│   └── Dockerfile.micronaut
└── frontend/
    └── dist/                 # Build do Angular (copiar aqui)
```

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

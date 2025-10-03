# 🌐 Nginx Architecture - Dual Configuration

## 📊 Arquitetura com Dois Nginx

A infraestrutura utiliza **dois Nginx separados** para melhor separação de responsabilidades:

```
┌─────────────────────────────────────────────┐
│           Cliente (Browser)                  │
└─────────────────┬───────────────────────────┘
                  │ HTTP :80
                  ▼
        ┌──────────────────────┐
        │  Nginx Frontend       │
        │  - Serve Angular      │
        │  - Proxy /api/*       │
        └──────────┬─────────────┘
                   │ :8080
                   ▼
        ┌──────────────────────┐
        │  Nginx API Gateway    │
        │  - Load Balancer      │
        │  - API routing        │
        └──────────┬─────────────┘
                   │
       ┌───────────┴────────────┐
       ▼                        ▼
   [Backend 1]            [Backend 2]
```

## 🎯 Responsabilidades

### 1. Nginx Frontend (Port 80)
**Container**: `restaurant-nginx-frontend`

- ✅ Serve arquivos estáticos do Angular
- ✅ Gerencia roteamento SPA (Single Page App)
- ✅ Faz proxy de `/api/*` para o API Gateway
- ✅ Cache de assets estáticos
- ✅ Configuração otimizada para frontend

**Config**: `nginx-frontend.conf`

### 2. Nginx API Gateway (Port 8080)
**Container**: `restaurant-nginx-api`

- ✅ Load balancing entre backends
- ✅ Roteamento de APIs (/api/spring/, /api/quarkus/, /api/micronaut/)
- ✅ Health checks
- ✅ CORS headers
- ✅ Configuração otimizada para APIs

**Config**: `nginx-api.conf`

## 🔌 Portas

| Serviço | Container | Porta Externa | Porta Interna | Acesso |
|---------|-----------|---------------|---------------|---------|
| **Frontend** | nginx-frontend | 80 | 80 | http://localhost |
| **API Gateway** | nginx-api | 8080 | 80 | http://localhost:8080 |

## 📍 Endpoints

### Através do Frontend (Porta 80)

```bash
# Frontend SPA
http://localhost/                    # Angular App

# APIs (proxy para API Gateway)
http://localhost/api/spring/customers
http://localhost/api/quarkus/customers
http://localhost/api/micronaut/customers
```

### Acesso Direto ao API Gateway (Porta 8080)

```bash
# APIs diretas
http://localhost:8080/api/spring/customers
http://localhost:8080/api/quarkus/customers
http://localhost:8080/api/micronaut/customers

# Health check
http://localhost:8080/health
```

## 🎨 Fluxo de Requisições

### Requisição Frontend
```
Browser → nginx-frontend:80 → Angular App
```

### Requisição API do Frontend
```
Browser → nginx-frontend:80 → /api/* → nginx-api:8080 → Backend
```

### Requisição API Direta
```
Client → nginx-api:8080 → Backend
```

## ✅ Benefícios desta Arquitetura

### 1. Separação de Responsabilidades
- Frontend e API com configurações distintas
- Mais fácil de gerenciar e debugar
- Configurações otimizadas para cada caso

### 2. Escalabilidade Independente
- Escale o frontend separadamente dos backends
- API Gateway pode ter mais recursos se necessário
- Frontend pode ter CDN na frente

### 3. Segurança
- API Gateway não expõe arquivos estáticos
- Frontend Nginx não tem acesso direto aos backends
- Camada extra de isolamento

### 4. Performance
- Cache otimizado por tipo (estático vs API)
- Frontend Nginx com gzip para assets
- API Gateway com pool de conexões para backends

### 5. Flexibilidade
- Pode adicionar SSL apenas no frontend
- Pode ter rate limiting diferente por serviço
- Pode trocar um sem afetar o outro

## 🔧 Configurações

### nginx-frontend.conf

```nginx
# Principais configurações:
- Root: /usr/share/nginx/html
- Proxy /api/* para nginx-api:80
- Cache para assets (1 year)
- No cache para index.html
- SPA routing (try_files)
```

### nginx-api.conf

```nginx
# Principais configurações:
- Upstreams: spring, quarkus, micronaut
- Load balancing: round-robin
- CORS headers habilitados
- Rewrite de URLs (/api/spring/ → /api/v1/)
- Health check endpoint
```

## 🧪 Testando a Arquitetura

### 1. Testar Frontend

```bash
curl http://localhost
# Deve retornar HTML do Angular
```

### 2. Testar API via Frontend Nginx

```bash
curl http://localhost/api/spring/customers
# Proxy para API Gateway
```

### 3. Testar API Gateway Diretamente

```bash
curl http://localhost:8080/api/spring/customers
# Acesso direto
```

### 4. Testar Health Checks

```bash
curl http://localhost:8080/health
# API Gateway OK
```

### 5. Verificar Load Balancing

```bash
# Fazer múltiplas requisições
for i in {1..10}; do
  curl http://localhost:8080/api/spring/customers
done

# Ver logs para confirmar distribuição
docker logs restaurant-nginx-api
```

## 🔄 Atualizar Configurações

### Frontend Nginx

```bash
# 1. Editar configuração
vim docker-infrastructure/nginx/nginx-frontend.conf

# 2. Testar configuração
docker exec restaurant-nginx-frontend nginx -t

# 3. Reload
docker exec restaurant-nginx-frontend nginx -s reload
```

### API Gateway Nginx

```bash
# 1. Editar configuração
vim docker-infrastructure/nginx/nginx-api.conf

# 2. Testar configuração
docker exec restaurant-nginx-api nginx -t

# 3. Reload
docker exec restaurant-nginx-api nginx -s reload
```

## 📝 Arquivos

```
nginx/
├── nginx-frontend.conf      # Configuração do Frontend Nginx
├── nginx-api.conf          # Configuração do API Gateway
├── nginx.conf.old          # Backup da configuração antiga
└── README.md               # Esta documentação
```

## 🚀 Próximos Passos (Produção)

Para produção, considere:

1. **SSL/TLS**: Adicionar certificados no Frontend Nginx
2. **Rate Limiting**: Proteger APIs de abuse
3. **Monitoring**: Prometheus + Grafana para métricas
4. **Logging**: Centralizar logs (ELK stack)
5. **CDN**: Colocar CDN na frente do Frontend Nginx
6. **Caching**: Redis para cache de API responses
7. **Security Headers**: Adicionar headers de segurança

---

**Arquitetura dual Nginx para máxima flexibilidade e separação de responsabilidades!** 🌐✨

# 🛠️ Troubleshooting Guide

## 🔍 Common Issues and Solutions

### Quarkus – EntityManager não injeta (Unsatisfied dependency)

Se você vir erros como "Unsatisfied dependency for type jakarta.persistence.EntityManager" ao subir o Quarkus:

#### 1. Dependência do ORM

**Arquivo**: `quarkus-app/pom.xml`

Garanta que a dependência abaixo esteja presente:

```xml
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-hibernate-orm</artifactId>
</dependency>
```

#### 2. Pacotes de entidades JPA

**Arquivo**: `quarkus-app/src/main/resources/application.properties`

Informe o pacote onde estão as entidades JPA para o Quarkus montar a Persistence Unit e disponibilizar o `EntityManager`:

```properties
quarkus.hibernate-orm.packages=com.restaurant.infrastructure.persistence.entity
```

#### 3. Injeção do EntityManager

**Arquivo**: `quarkus-app/src/main/java/com/restaurant/quarkus/config/ApplicationConfig.java`

Utilize injeção nativa do Quarkus e consuma o `EntityManager` nos producers dos repositórios:

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

#### 4. Reiniciar o dev mode do Quarkus

Caso veja "Error restarting Quarkus", pare processos ativos e suba novamente:

```bash
# Em outro terminal:
pkill -f "quarkus:dev" || true

# No diretório quarkus-app
mvn clean compile
mvn quarkus:dev -Dquarkus.http.port=8081
```

#### 5. Verificar MySQL

Confirme que as credenciais e a URL batem com o `application.properties`:

```properties
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/restaurant_db
quarkus.datasource.username=restaurant_user
quarkus.datasource.password=restaurant_password
```

#### 6. Validar endpoints

Teste se a aplicação está respondendo:

```bash
curl -i http://localhost:8081/api/v1/customers
```

---

### Live Reload do Quarkus quebrou (página de erro 500 de reinício)

Às vezes o hot reload entra em estado inconsistente. Faça um restart limpo conforme o passo 4 acima.

**Solução rápida**:

```bash
# Parar Quarkus
pkill -f "quarkus:dev"

# Limpar e reiniciar
cd quarkus-app
mvn clean compile
mvn quarkus:dev -Dquarkus.http.port=8081
```

---

### Spring Boot – Porta já em uso

**Erro**: `Port 8082 is already in use`

**Solução**:

```bash
# Encontrar e matar processo na porta 8082
lsof -ti:8082 | xargs kill -9

# Ou usar script
./scripts-develop/stop-all-backends.sh
```

---

### Micronaut – Startup lento

**Problema**: Micronaut demora muito para iniciar

**Soluções**:

1. **Aumentar memória JVM**:
   ```bash
   export JAVA_OPTS="-Xmx2g -Xms512m"
   java $JAVA_OPTS -jar micronaut-app/target/micronaut-app-1.0.0.jar
   ```

2. **Desabilitar features não usadas** em `application.yml`:
   ```yaml
   micronaut:
     server:
       netty:
         use-native-transport: false
   ```

---

### MySQL – Connection refused

**Erro**: `Connection refused: localhost:3306`

**Soluções**:

1. **Verificar se MySQL está rodando**:
   ```bash
   # macOS
   brew services list | grep mysql
   
   # Linux
   sudo systemctl status mysql
   
   # Docker
   docker ps | grep mysql
   ```

2. **Iniciar MySQL via Docker**:
   ```bash
   cd docker-infrastructure
   docker compose up -d mysql
   ```

3. **Verificar credenciais**:
   ```bash
   mysql -u restaurant_user -p restaurant_db
   # Senha: restaurant_password
   ```

4. **Criar database se não existir**:
   ```sql
   CREATE DATABASE IF NOT EXISTS restaurant_db;
   CREATE USER IF NOT EXISTS 'restaurant_user'@'localhost' IDENTIFIED BY 'restaurant_password';
   GRANT ALL PRIVILEGES ON restaurant_db.* TO 'restaurant_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

---

### Redis – Connection timeout

**Erro**: `Could not connect to Redis at localhost:6379`

**Soluções**:

1. **Verificar se Redis está rodando**:
   ```bash
   redis-cli ping
   # Deve retornar: PONG
   ```

2. **Iniciar Redis via Docker**:
   ```bash
   cd docker-infrastructure
   docker compose up -d redis
   ```

3. **Testar conexão**:
   ```bash
   redis-cli
   > ping
   PONG
   > exit
   ```

---

### Build falha – Dependências não resolvidas

**Erro**: `Could not resolve dependencies`

**Soluções**:

1. **Limpar cache do Maven**:
   ```bash
   mvn dependency:purge-local-repository
   mvn clean install
   ```

2. **Forçar atualização**:
   ```bash
   mvn clean install -U
   ```

3. **Verificar ordem de build**:
   ```bash
   # Build correto (ordem importa!)
   mvn clean install -pl domain
   mvn clean install -pl application
   mvn clean install -pl infrastructure
   mvn clean install -pl spring-boot-app
   ```

---

### Docker Desktop não inicia

**Problema**: Scripts não conseguem iniciar Docker Desktop

**Soluções**:

1. **Iniciar manualmente**:
   ```bash
   open -a Docker
   ```

2. **Verificar se está rodando**:
   ```bash
   docker ps
   ```

3. **Reiniciar Docker Desktop**:
   ```bash
   killall Docker
   open -a Docker
   ```

---

### Virtual Threads não funcionam

**Problema**: Endpoints de Virtual Threads retornam erro

**Verificações**:

1. **Java 21+**:
   ```bash
   java -version
   # Deve ser 21 ou superior
   ```

2. **Spring Boot - Verificar configuração**:
   ```properties
   spring.threads.virtual.enabled=true
   ```

3. **Quarkus - Verificar anotação**:
   ```java
   @RunOnVirtualThread
   public Uni<List<Customer>> getCustomers() { ... }
   ```

4. **Micronaut - Verificar executor**:
   ```java
   @ExecuteOn(TaskExecutors.IO)
   public List<Customer> getCustomers() { ... }
   ```

---

### Testes de Arquitetura falhando

**Erro**: `Architecture Violation [Priority: MEDIUM]`

**Soluções**:

1. **Ver detalhes do erro**:
   ```bash
   mvn test -pl architecture-tests
   ```

2. **Verificar regras**:
   - Domain não pode depender de frameworks
   - Application não pode depender de infrastructure
   - Controllers devem usar apenas Input Ports

3. **Exemplo de violação comum**:
   ```java
   // ❌ ERRADO - Domain com anotação de framework
   @Component
   public class Customer { ... }
   
   // ✅ CORRETO - Domain puro
   public class Customer { ... }
   ```

---

## 🧹 Git & Hygiene

### Ignore arquivos de build

Adicione um `.gitignore` na raiz:

```gitignore
**/target/
node_modules/
.venv/
.idea/
.DS_Store
*.log
logs/
```

### Remover artefatos de build do Git

Se artefatos de build já estiverem versionados:

```bash
git rm -r --cached **/target
git rm -r --cached node_modules
git commit -m "chore: stop tracking build artifacts"
```

---

## 📞 Suporte Adicional

### Logs

Verificar logs de cada aplicação:

```bash
# Spring Boot
tail -f logs/spring-boot.log

# Quarkus
tail -f logs/quarkus.log

# Micronaut
tail -f logs/micronaut.log
```

### Health Checks

Verificar saúde das aplicações:

```bash
# Spring Boot
curl http://localhost:8082/actuator/health

# Quarkus
curl http://localhost:8081/q/health

# Micronaut
curl http://localhost:8083/health
```

### Documentação Relacionada

- **[README.md](../../README.md)** - Guia principal
- **[SETUP.md](../SETUP.md)** - Configuração inicial
- **[ARCHITECTURE.md](./ARCHITECTURE.md)** - Arquitetura detalhada
- **[scripts-develop/README.md](../../scripts-develop/README.md)** - Scripts de desenvolvimento

---

**Última atualização**: 2025-10-02  
**Status**: Ativo ✅

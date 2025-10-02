# Setup Guide

## Quick Start

### 1. Prerequisites Check
```bash
# Verify Java version
java -version  # Should be 21+

# Verify Maven version  
mvn -version   # Should be 3.9+

# Check MySQL is running
mysql -u root -p -e "SELECT VERSION();"
```

### 2. Database Setup
```sql
-- Connect to MySQL as root
mysql -u root -p

-- Create database and user
CREATE DATABASE restaurant_db;
CREATE USER 'restaurant_user'@'localhost' IDENTIFIED BY 'restaurant_password';
GRANT ALL PRIVILEGES ON restaurant_db.* TO 'restaurant_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Build and Run
```bash
# Clone and navigate to project
cd restaurant-management

# Build core modules (REQUIRED FIRST)
mvn clean install -DskipTests -pl domain,application,infrastructure

# Run Spring Boot application
cd spring-boot-app
mvn spring-boot:run
```

### 4. Verify Installation
```bash
# Check application health
curl http://localhost:8082/actuator/health

# Test customer endpoint
curl http://localhost:8082/api/customers
```

## Troubleshooting

### Common Issues

#### Port Already in Use
```bash
# Find process using port
lsof -ti:8082 | xargs kill -9

# Or change port in application.properties
server.port=8083
```

#### Database Connection Issues
```bash
# Test MySQL connection
mysql -u restaurant_user -p restaurant_db

# Check MySQL is running
sudo systemctl status mysql  # Linux
brew services list | grep mysql  # macOS
```

#### Build Failures
```bash
# Clean and rebuild
mvn clean install -DskipTests

# Build only working modules
mvn clean install -DskipTests -pl domain,application,infrastructure,spring-boot-app
```

### Module Build Order
1. `domain` - Core business entities
2. `application` - Use cases and services  
3. `infrastructure` - JPA repositories and adapters
4. `spring-boot-app` - REST API implementation

### Configuration Files

#### application.properties (Spring Boot)
```properties
# Server
server.port=8082

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/restaurant_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=restaurant_user
spring.datasource.password=restaurant_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Redis (optional)
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

## Development Workflow

### Adding New Features
1. Define domain entities in `domain/`
2. Create use case interfaces in `application/port/in/`
3. Create repository interfaces in `application/port/out/`
4. Implement services in `application/service/`
5. Implement repositories in `infrastructure/`
6. Add REST controllers in `spring-boot-app/`

### Testing
```bash
# Run unit tests
mvn test -pl domain,application

# Run integration tests
mvn test -pl infrastructure

# Run API tests
mvn test -pl spring-boot-app
```

## Docker Setup (Alternative)

### Using Docker Compose
```bash
# Start infrastructure services
docker-compose up -d mysql redis

# Build and run application
mvn clean install -DskipTests -pl domain,application,infrastructure
cd spring-boot-app
mvn spring-boot:run
```

### Docker Compose Configuration
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: restaurant_db
      MYSQL_USER: restaurant_user
      MYSQL_PASSWORD: restaurant_password
    ports:
      - "3306:3306"
  
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
```

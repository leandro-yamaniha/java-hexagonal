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

## 🎨 Frontend Setup

### Prerequisites

```bash
# Verify Node.js version
node -v  # Should be 18+

# Verify npm version
npm -v   # Should be 9+

# Install Angular CLI globally
npm install -g @angular/cli
```

### Installation

```bash
cd frontend-angular

# Install dependencies
npm install

# Verify installation
ng version
```

### Development Mode

```bash
# Start dev server with hot reload
ng serve

# Access: http://localhost:4200
# API Proxy: Configured to backend on port 8082
```

### Production Build

```bash
# Build for production
npm run build --prod

# Output: dist/ folder with optimized files
```

### Docker Integration

```bash
# Option 1: Build and copy to docker-infrastructure
cd docker-infrastructure
./build-frontend.sh

# Option 2: Manual build
cd frontend-angular
npm run build --prod
cp -r dist/* ../docker-infrastructure/frontend/dist/

# Start with Docker
cd docker-infrastructure
./start-spring.sh  # Frontend served on http://localhost
```

### Configuration

**environment.ts** (Development):
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8082/api'
};
```

**environment.prod.ts** (Production):
```typescript
export const environment = {
  production: true,
  apiUrl: '/api'  // Proxied by Nginx
};
```

### Troubleshooting

#### Port 4200 Already in Use
```bash
# Kill process on port 4200
lsof -ti:4200 | xargs kill -9

# Or use different port
ng serve --port 4201
```

#### Build Errors
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install

# Clear Angular cache
ng cache clean
```

📖 **Documentação completa**: [docker-infrastructure/README-frontend.md](../docker-infrastructure/README-frontend.md)

---

## 🐳 Docker Setup (Alternative)

### Using Docker Compose
```bash
# Start infrastructure services
cd docker-infrastructure
docker compose up -d mysql redis

# Build and run application
cd ../backend
mvn clean install -DskipTests -pl domain,application,infrastructure
cd spring-boot-app
mvn spring-boot:run
```

### Full Docker Infrastructure

```bash
# Complete containerized setup
cd docker-infrastructure

# Option 1: All backends
./start-all.sh

# Option 2: Single backend
./start-spring.sh
./start-quarkus.sh
./start-micronaut.sh
```

📖 **Documentação completa**: [docker-infrastructure/README.md](../docker-infrastructure/README.md)

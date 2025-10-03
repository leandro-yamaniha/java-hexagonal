# Backend Startup Scripts

This directory contains bash scripts to manage the three backend applications in the restaurant management system.

## 📋 Available Scripts

### 1. Individual Application Scripts

#### `start-spring-boot.sh`
Starts the Spring Boot application on **port 8082**.
```bash
Starts the Micronaut application on **port 8083**.
```bash
./scripts/start-micronaut.sh
- **Features**: Fast startup, virtual threads, low memory usage
- **Swagger UI**: http://localhost:8083/swagger-ui
- **Health Check**: http://localhost:8083/health
- **Memory**: 128MB-256MB

### 2. Orchestration Scripts

#### `start-all-backends.sh`
Starts all three backend applications simultaneously.
```bash
./scripts/start-all-backends.sh
```
- **Automatically starts Docker Compose** (MySQL + Redis)
- Runs all applications in background
- Creates log files in `logs/` directory
- Checks prerequisites (Java, MySQL, Redis)
- Automatically kills existing processes on the same ports
- Waits for MySQL to be healthy before starting apps
- Provides status summary after startup

#### `stop-all-backends.sh`
Stops all running backend applications.
```bash
./scripts/stop-all-backends.sh
```
- Gracefully shuts down all applications
- Cleans up any remaining processes
- Safe to run even if applications are not running

#### `start-dev-mode.sh`
Starts all applications in **development mode** with hot reload.
```bash
./scripts/start-dev-mode.sh
```
- **Quarkus**: Live coding with `mvn quarkus:dev`
- **Spring Boot**: DevTools hot reload
- **Micronaut**: Continuous mode
- Ideal for active development

#### `status.sh`
Check which backend applications are currently running.
```bash
./scripts/status.sh
```
- Shows PID, memory usage, uptime for each app
- Verifies MySQL and Redis connectivity
- Displays URLs and Swagger endpoints

#### `check-docker.sh`
Verify Docker installation and service health.
```bash
./scripts/check-docker.sh
```
- Checks if Docker is installed and running
- Verifies Docker Compose availability
- Shows running containers and images
- Checks MySQL and Redis health status
- Tests port connectivity (3306, 6379)
- Provides detailed diagnostics

#### `start-docker.sh`
Start Docker Desktop automatically (macOS).
```bash
./scripts/start-docker.sh
```
- Checks if Docker is installed
- Starts Docker Desktop if not running
- Waits for daemon to be ready (up to 60 seconds)
- Shows progress with visual feedback
- Verifies Docker Compose availability
- Optimized for macOS (with Linux/Windows instructions)

#### `stop-docker.sh`
Stop Docker Desktop gracefully (macOS).
```bash
./scripts/stop-docker.sh
```
- Stops Docker Compose services first (MySQL, Redis)
- Shows running containers before stopping
- Asks for confirmation to stop all containers
- Gracefully quits Docker Desktop
- Waits for complete shutdown (up to 30 seconds)
- Releases all Docker resources

## 🚀 Quick Start

### Complete Startup Workflow (Recommended)

**One-command startup** (everything is automatic):
```bash
cd /Volumes/Dock/ai/ide/windsurf/restaurant-management

# Option 1: Let start-all-backends handle everything
./scripts/start-all-backends.sh
# This will automatically start Docker Compose (MySQL + Redis) if needed
```

**Manual step-by-step** (if you prefer control):
```bash
# Step 1: Start Docker Desktop (if not running)
./scripts/start-docker.sh

# Step 2: Verify Docker is healthy
./scripts/check-docker.sh

# Step 3: Start all backend applications
./scripts/start-all-backends.sh

# Step 4: Check status
./scripts/status.sh
```

### Start All Applications (Production Mode)
```bash
cd /Volumes/Dock/ai/ide/windsurf/restaurant-management
./scripts/start-all-backends.sh
```

### Start All Applications (Development Mode)
```bash
./scripts/start-dev-mode.sh
```

### Stop All Applications
```bash
./scripts/stop-all-backends.sh
```

## 📊 Port Configuration

| Application  | Port | Swagger UI Path           |
|-------------|------|---------------------------|
| Quarkus     | 8081 | `/q/swagger-ui`          |
| Spring Boot | 8082 | `/swagger-ui.html`       |
| Micronaut   | 8083 | `/swagger-ui`            |

## 🐳 Docker Complete Integration

**All startup scripts now automatically manage the entire Docker stack!**

### What Happens Automatically (Zero Manual Steps Required):
1. **Checks if Docker Desktop is running** - If not, automatically starts it
2. **Waits for Docker daemon to be ready** (up to 60 seconds)
3. **Checks if Docker services are running**
4. **Starts MySQL + Redis via `docker-compose up -d`** if not running
5. **Waits for MySQL to be healthy** (up to 60 seconds)
6. **Verifies connections** before starting applications
7. **Retries if connection fails**

### The Magic: One Command Does Everything! 🎉
```bash
# Even if Docker Desktop is closed, this will:
# 1. Start Docker Desktop
# 2. Wait for it to be ready
# 3. Start MySQL and Redis
# 4. Start all your apps
./scripts/start-all-backends.sh

# Same for individual apps!
./scripts/start-spring-boot.sh  # Handles Docker automatically
./scripts/start-quarkus.sh      # Handles Docker automatically
./scripts/start-micronaut.sh    # Handles Docker automatically
```

### Manual Docker Management (Optional):
```bash
# Start infrastructure manually
cd docker
docker compose up -d

# Stop infrastructure
docker compose down

# View logs
docker compose logs -f mysql
docker compose logs -f redis

# Check status
docker compose ps
```

**You don't need to start Docker manually anymore!** The scripts handle everything automatically.

## ⚙️ Prerequisites

### Required
- **Java 21+** (for virtual threads support)
- **Maven 3.8+**
- **Docker & Docker Compose** (for MySQL and Redis)

### Automatic (Managed by Scripts)
- **MySQL 8.0+** on `localhost:3306` - Started automatically via Docker
  - Database: `restaurant_db`
  - User: `restaurant_user`
  - Password: `restaurant_password`
- **Redis** on `localhost:6379` - Started automatically via Docker (optional for Quarkus caching)

### Check Prerequisites
```bash
# Java version
java -version

# Maven version
mvn -version

# MySQL connection
nc -z localhost 3306 && echo "MySQL is running" || echo "MySQL is not running"

# Redis connection
nc -z localhost 6379 && echo "Redis is running" || echo "Redis is not running"
```

## 📝 Log Files

When using `start-all-backends.sh` or `start-dev-mode.sh`, logs are saved in:
```
logs/
├── quarkus-startup.log      # Quarkus production logs
├── spring boot-startup.log  # Spring Boot production logs
├── micronaut-startup.log    # Micronaut production logs
├── quarkus-dev.log          # Quarkus development logs
├── spring boot-dev.log      # Spring Boot development logs
└── micronaut-dev.log        # Micronaut development logs
```

View logs in real-time:
```bash
tail -f logs/quarkus-startup.log
tail -f logs/spring\ boot-startup.log
tail -f logs/micronaut-startup.log
```

## 🔧 Script Features

### Automatic Port Cleanup
All scripts automatically detect and kill processes on their target ports before starting.

### Health Checks
- Checks Java installation and version
- Verifies MySQL connectivity
- Checks Redis availability (optional)
- Validates port availability

### Color-Coded Output
- 🔵 **Blue**: Information messages
- 🟢 **Green**: Success messages
- 🟡 **Yellow**: Warnings
- 🔴 **Red**: Errors
- 🟣 **Purple**: Section headers

### Error Handling
- `set -e`: Exit on any error
- Graceful shutdown attempts before forced termination
- Detailed error messages and troubleshooting hints

## 🐛 Troubleshooting

### Port Already in Use
Scripts automatically handle this, but if needed manually:
```bash
# Find process on port
lsof -i :8081
lsof -i :8082
lsof -i :8083

# Kill process
kill -9 <PID>
```

### MySQL Not Running
```bash
# macOS with Homebrew
brew services start mysql

# Or start MySQL manually
mysql.server start
```

### Redis Not Running
```bash
# macOS with Homebrew
brew services start redis

# Or start Redis manually
redis-server
```

### Java Version Issues
Ensure Java 21+ is installed:
```bash
java -version
# Should show version 21 or higher
```

### Build Failures
Clean and rebuild:
```bash
cd backend/spring-boot-app && mvn clean package -DskipTests
cd ../quarkus-app && mvn clean package -DskipTests
cd ../micronaut-app && mvn clean package -DskipTests
```

## 🎯 Usage Scenarios

### Scenario 1: Quick Testing
Start all apps to test API compatibility across frameworks:
```bash
./scripts/start-all-backends.sh
```

### Scenario 2: Active Development
Work on one or all apps with hot reload:
```bash
./scripts/start-dev-mode.sh
```

### Scenario 3: Performance Comparison
Start all apps and compare startup time, memory usage, and throughput:
```bash
./scripts/start-all-backends.sh

# Check memory usage
ps aux | grep java

# Compare startup times in logs
grep "started" logs/*-startup.log
```

### Scenario 4: Single Framework Development
Work on just one framework:
```bash
./scripts/start-quarkus.sh
# or
./scripts/start-spring-boot.sh
# or
./scripts/start-micronaut.sh
```

## 📈 Performance Tips

### Memory Optimization
Each script uses optimized JVM settings:
- **Spring Boot**: `-Xmx512m -Xms256m`
- **Quarkus**: `-Xmx256m -Xms128m`
- **Micronaut**: `-Xmx256m -Xms128m`

Adjust in the script files if needed for your environment.

### Startup Time Optimization
1. **Quarkus**: Fastest (~2-3 seconds)
2. **Micronaut**: Fast (~3-5 seconds)
3. **Spring Boot**: Standard (~5-10 seconds)

### Development Mode
Development mode is slower but provides hot reload:
- Changes are automatically picked up
- No need to restart the application
- Ideal for rapid development

## 🔐 Security Notes

- Scripts check for existing processes to prevent duplicate instances
- Graceful shutdown (SIGTERM) before forced kill (SIGKILL)
- Database credentials are in application properties (not hardcoded in scripts)
- Scripts do not expose sensitive information in logs

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Quarkus Documentation](https://quarkus.io)
- [Micronaut Documentation](https://micronaut.io)
- Project Architecture: See `../ARCHITECTURE.md`
- Setup Instructions: See `../SETUP.md`

## 💡 Tips

1. **Always check logs** if an application fails to start
2. **Use development mode** during active coding
3. **Stop applications** when not in use to free resources
4. **Monitor ports** to avoid conflicts with other services
5. **Keep MySQL and Redis running** for best experience

## 🆘 Support

If you encounter issues:
1. Check the log files in `logs/` directory
2. Verify prerequisites are met
3. Ensure no port conflicts exist
4. Try stopping all apps and restarting
5. Check MySQL/Redis connectivity

---

**Note**: All scripts are designed for **macOS** with **zsh** shell. Minor adjustments may be needed for Linux or other shells.

#!/bin/bash

# Development Mode Startup Script
# Starts applications with hot reload and development features

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

echo -e "${PURPLE}============================================${NC}"
echo -e "${PURPLE}  Restaurant Management - Development Mode${NC}"
echo -e "${PURPLE}============================================${NC}"

# Function to check if a port is in use
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
        return 0  # Port is in use
    else
        return 1  # Port is free
    fi
}

# Function to kill process on port
kill_port() {
    local port=$1
    local app_name=$2
    if check_port $port; then
        echo -e "${YELLOW}⚠️  $app_name (port $port) is already running. Stopping...${NC}"
        PID=$(lsof -ti:$port)
        if [ ! -z "$PID" ]; then
            kill -9 $PID
            sleep 2
            echo -e "${GREEN}✅ Stopped $app_name${NC}"
        fi
    fi
}

# Function to start application in dev mode
start_dev_app() {
    local app_dir=$1
    local app_name=$2
    local port=$3
    local dev_command=$4
    
    echo -e "${BLUE}🚀 Starting $app_name in development mode...${NC}"
    
    cd "$PROJECT_DIR/$app_dir"
    
    # Start in background with dev mode
    nohup $dev_command > "$PROJECT_DIR/logs/${app_name,,}-dev.log" 2>&1 &
    local pid=$!
    
    echo -e "${GREEN}✅ $app_name dev mode started (PID: $pid)${NC}"
    echo -e "${BLUE}📋 Log file: logs/${app_name,,}-dev.log${NC}"
    echo -e "${BLUE}🔄 Hot reload enabled for $app_name${NC}"
    
    cd "$SCRIPT_DIR"
}

# Create logs directory if it doesn't exist
mkdir -p "$PROJECT_DIR/logs"

# Check prerequisites
echo -e "${BLUE}🔍 Checking prerequisites...${NC}"

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java is not installed or not in PATH${NC}"
    exit 1
fi

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven is not installed or not in PATH${NC}"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
echo -e "${GREEN}☕ Java Version: $JAVA_VERSION${NC}"

# Ensure Docker Desktop is running
echo -e "${BLUE}🐳 Ensuring Docker Desktop is running...${NC}"
if ! docker info &> /dev/null; then
    echo -e "${YELLOW}⚠️  Docker is not running. Starting Docker Desktop...${NC}"
    if [ -f "$SCRIPT_DIR/start-docker.sh" ]; then
        "$SCRIPT_DIR/start-docker.sh"
    else
        echo -e "${RED}❌ start-docker.sh not found${NC}"
        echo -e "${YELLOW}   Please start Docker Desktop manually${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}✅ Docker Desktop is running${NC}"
fi

# Start Docker infrastructure first
echo -e "${BLUE}🐳 Starting Docker infrastructure (MySQL + Redis)...${NC}"
cd "$PROJECT_DIR/docker"

if ! docker compose ps | grep -q "running"; then
    echo -e "${BLUE}🚀 Starting MySQL and Redis via docker-compose...${NC}"
    docker compose up -d
    echo -e "${BLUE}⏳ Waiting for services to be ready...${NC}"
    sleep 10
    
    # Wait for MySQL to be healthy
    for i in {1..30}; do
        if docker compose ps mysql | grep -q "healthy"; then
            echo -e "${GREEN}✅ MySQL is ready${NC}"
            break
        fi
        echo -e "${YELLOW}⏳ Waiting for MySQL... ($i/30)${NC}"
        sleep 2
    done
    
    # Check Redis
    if docker compose ps redis | grep -q "running"; then
        echo -e "${GREEN}✅ Redis is ready${NC}"
    fi
else
    echo -e "${GREEN}✅ Docker services are already running${NC}"
fi

cd "$SCRIPT_DIR"

# Verify MySQL connection
if ! nc -z localhost 3306; then
    echo -e "${RED}❌ MySQL is not accessible on localhost:3306${NC}"
    echo -e "${YELLOW}   Check docker-compose logs: cd docker && docker compose logs mysql${NC}"
fi

# Stop any existing instances
echo -e "${BLUE}🛑 Stopping existing instances...${NC}"
kill_port 8081 "Quarkus"
kill_port 8082 "Spring Boot"
kill_port 8083 "Micronaut"

# Start applications in development mode
echo -e "${BLUE}🚀 Starting all applications in development mode...${NC}"

# Start Quarkus in dev mode (with hot reload)
start_dev_app "backend/quarkus-app" "Quarkus" 8081 "mvn quarkus:dev -Ddebug=false"

# Start Spring Boot in dev mode (with hot reload)
start_dev_app "backend/spring-boot-app" "Spring Boot" 8082 "mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dspring.devtools.restart.enabled=true'"

# Start Micronaut in dev mode
start_dev_app "backend/micronaut-app" "Micronaut" 8083 "mvn mn:run"

# Wait for applications to start
echo -e "${BLUE}⏳ Waiting for applications to start...${NC}"
sleep 15

# Final status check
echo -e "${PURPLE}============================================${NC}"
echo -e "${PURPLE}  Development Mode Status${NC}"
echo -e "${PURPLE}============================================${NC}"

if check_port 8081; then
    echo -e "${GREEN}✅ Quarkus Dev: http://localhost:8081 (Hot reload enabled)${NC}"
    echo -e "${BLUE}   📖 Swagger: http://localhost:8081/q/swagger-ui${NC}"
    echo -e "${BLUE}   🔧 Dev UI: http://localhost:8081/q/dev${NC}"
else
    echo -e "${RED}❌ Quarkus: Not running${NC}"
fi

if check_port 8082; then
    echo -e "${GREEN}✅ Spring Boot Dev: http://localhost:8082 (Hot reload enabled)${NC}"
    echo -e "${BLUE}   📖 Swagger: http://localhost:8082/swagger-ui.html${NC}"
else
    echo -e "${RED}❌ Spring Boot: Not running${NC}"
fi

if check_port 8083; then
    echo -e "${GREEN}✅ Micronaut Dev: http://localhost:8083${NC}"
    echo -e "${BLUE}   📖 Swagger: http://localhost:8083/swagger-ui${NC}"
    echo -e "${BLUE}   🏥 Health: http://localhost:8083/health${NC}"
else
    echo -e "${RED}❌ Micronaut: Not running${NC}"
fi

echo -e "${PURPLE}============================================${NC}"
echo -e "${GREEN}🎉 Development mode started!${NC}"
echo -e "${BLUE}🔄 All applications have hot reload enabled${NC}"
echo -e "${BLUE}📋 Check individual log files in the logs/ directory${NC}"
echo -e "${BLUE}🛑 To stop all applications, run: ./scripts/stop-all-backends.sh${NC}"
echo -e "${PURPLE}============================================${NC}"

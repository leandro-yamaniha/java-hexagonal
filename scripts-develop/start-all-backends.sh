#!/bin/bash

# Master Startup Script for All Backend Applications
# Starts Spring Boot (8082), Quarkus (8081), and Micronaut (8083)

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
echo -e "${PURPLE}  Restaurant Management - All Backends${NC}"
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

# Function to start application in background
start_app() {
    local script_name=$1
    local app_name=$2
    local port=$3
    
    echo -e "${BLUE}🚀 Starting $app_name...${NC}"
    
    # Make script executable
    chmod +x "$SCRIPT_DIR/$script_name"
    
    # Start in background and redirect output to log file
    nohup "$SCRIPT_DIR/$script_name" > "$PROJECT_DIR/logs/${app_name,,}-startup.log" 2>&1 &
    local pid=$!
    
    echo -e "${GREEN}✅ $app_name started (PID: $pid)${NC}"
    echo -e "${BLUE}📋 Log file: logs/${app_name,,}-startup.log${NC}"
    
    # Wait a moment for startup
    sleep 3
    
    # Check if process is still running
    if kill -0 $pid 2>/dev/null; then
        echo -e "${GREEN}✅ $app_name is running on port $port${NC}"
    else
        echo -e "${RED}❌ $app_name failed to start. Check log file.${NC}"
    fi
}

# Function to wait for application to be ready
wait_for_app() {
    local port=$1
    local app_name=$2
    local max_attempts=30
    local attempt=1
    
    echo -e "${BLUE}⏳ Waiting for $app_name to be ready...${NC}"
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s -f "http://localhost:$port/health" > /dev/null 2>&1 || \
           curl -s -f "http://localhost:$port/q/health" > /dev/null 2>&1 || \
           curl -s -f "http://localhost:$port" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ $app_name is ready!${NC}"
            return 0
        fi
        
        echo -e "${YELLOW}⏳ Attempt $attempt/$max_attempts - $app_name not ready yet...${NC}"
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo -e "${RED}❌ $app_name did not become ready within expected time${NC}"
    return 1
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

# Start Docker infrastructure first (MySQL + Redis only)
echo -e "${BLUE}🐳 Starting Docker infrastructure (MySQL + Redis)...${NC}"
cd "$PROJECT_DIR/docker-infrastructure"

# Check if MySQL and Redis are already running
if docker compose ps mysql 2>/dev/null | grep -q "running" && \
   docker compose ps redis 2>/dev/null | grep -q "running"; then
    echo -e "${GREEN}✅ MySQL and Redis are already running${NC}"
else
    echo -e "${BLUE}🚀 Starting MySQL and Redis via docker-compose...${NC}"
    docker compose up -d mysql redis
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

# Verify connections
if ! nc -z localhost 3306; then
    echo -e "${RED}❌ MySQL is not accessible on localhost:3306${NC}"
    echo -e "${YELLOW}   Check docker-compose logs: cd docker && docker compose logs mysql${NC}"
fi

if nc -z localhost 6379; then
    echo -e "${GREEN}✅ Redis is accessible${NC}"
else
    echo -e "${YELLOW}⚠️  Redis is not accessible (optional)${NC}"
fi

# Stop any existing instances
echo -e "${BLUE}🛑 Stopping existing instances...${NC}"
kill_port 8081 "Quarkus"
kill_port 8082 "Spring Boot"
kill_port 8083 "Micronaut"

# Start applications
echo -e "${BLUE}🚀 Starting all backend applications...${NC}"

# Start Quarkus (fastest startup)
start_app "start-quarkus.sh" "Quarkus" 8081

# Start Micronaut
start_app "start-micronaut.sh" "Micronaut" 8083

# Start Spring Boot (slowest startup)
start_app "start-spring-boot.sh" "Spring Boot" 8082

# Wait for all applications to be ready
echo -e "${BLUE}⏳ Waiting for all applications to be ready...${NC}"
sleep 10

# Final status check
echo -e "${PURPLE}============================================${NC}"
echo -e "${PURPLE}  Final Status Check${NC}"
echo -e "${PURPLE}============================================${NC}"

if check_port 8081; then
    echo -e "${GREEN}✅ Quarkus: http://localhost:8081 (Swagger: /q/swagger-ui)${NC}"
else
    echo -e "${RED}❌ Quarkus: Not running${NC}"
fi

if check_port 8082; then
    echo -e "${GREEN}✅ Spring Boot: http://localhost:8082 (Swagger: /swagger-ui.html)${NC}"
else
    echo -e "${RED}❌ Spring Boot: Not running${NC}"
fi

if check_port 8083; then
    echo -e "${GREEN}✅ Micronaut: http://localhost:8083 (Swagger: /swagger-ui)${NC}"
else
    echo -e "${RED}❌ Micronaut: Not running${NC}"
fi

echo -e "${PURPLE}============================================${NC}"
echo -e "${GREEN}🎉 Startup process completed!${NC}"
echo -e "${BLUE}📋 Check individual log files in the logs/ directory for details${NC}"
echo -e "${BLUE}🛑 To stop all applications, run: ./scripts/stop-all-backends.sh${NC}"
echo -e "${PURPLE}============================================${NC}"

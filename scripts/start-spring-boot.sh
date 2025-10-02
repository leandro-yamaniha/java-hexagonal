#!/bin/bash

# Spring Boot Application Startup Script
# Port: 8082

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
APP_NAME="Spring Boot Restaurant Management"
APP_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)/backend/spring-boot-app"
PORT=8082
JAVA_OPTS="-Xmx512m -Xms256m"
PROFILE="dev"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Starting $APP_NAME${NC}"
echo -e "${BLUE}========================================${NC}"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java is not installed or not in PATH${NC}"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
echo -e "${BLUE}☕ Java Version: $JAVA_VERSION${NC}"

# Check if port is already in use
if lsof -Pi :$PORT -sTCP:LISTEN -t >/dev/null ; then
    echo -e "${YELLOW}⚠️  Port $PORT is already in use. Attempting to kill existing process...${NC}"
    PID=$(lsof -ti:$PORT)
    if [ ! -z "$PID" ]; then
        kill -9 $PID
        sleep 2
        echo -e "${GREEN}✅ Killed process on port $PORT${NC}"
    fi
fi

# Ensure Docker Desktop is running
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
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

# Start Docker Compose if needed
echo -e "${BLUE}🐳 Starting Docker infrastructure (MySQL + Redis)...${NC}"
DOCKER_DIR="$(cd "$APP_DIR/.." && pwd)/docker"
cd "$DOCKER_DIR"

if ! docker compose ps | grep -q "running"; then
    echo -e "${BLUE}🚀 Starting MySQL and Redis via docker-compose...${NC}"
    docker compose up -d
    echo -e "${BLUE}⏳ Waiting for MySQL to be ready...${NC}"
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
else
    echo -e "${GREEN}✅ Docker services are already running${NC}"
fi

# Navigate to application directory
cd "$APP_DIR"
echo -e "${BLUE}📁 Working directory: $(pwd)${NC}"

# Verify MySQL connection
echo -e "${BLUE}🔍 Verifying MySQL connection...${NC}"
if ! nc -z localhost 3306; then
    echo -e "${RED}❌ MySQL is not accessible on localhost:3306${NC}"
    echo -e "${YELLOW}   Trying to start docker-compose again...${NC}"
    cd "$DOCKER_DIR"
    docker compose up -d mysql
    sleep 5
    cd "$APP_DIR"
fi

# Build the application if needed
if [ ! -f "target/spring-boot-app-1.0.0.jar" ]; then
    echo -e "${BLUE}🔨 Building application...${NC}"
    mvn clean package -DskipTests
fi

# Start the application
echo -e "${GREEN}🚀 Starting $APP_NAME on port $PORT...${NC}"
echo -e "${BLUE}📊 Profile: $PROFILE${NC}"
echo -e "${BLUE}🔧 Java Options: $JAVA_OPTS${NC}"

# Run the application
java $JAVA_OPTS -Dspring.profiles.active=$PROFILE -jar target/spring-boot-app-1.0.0.jar

echo -e "${GREEN}✅ $APP_NAME started successfully!${NC}"
echo -e "${GREEN}🌐 Application URL: http://localhost:$PORT${NC}"
echo -e "${GREEN}📖 Swagger UI: http://localhost:$PORT/swagger-ui.html${NC}"

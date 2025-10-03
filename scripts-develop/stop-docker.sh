#!/bin/bash

# Docker Stop Script for macOS
# Stops Docker Desktop and all running containers

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

echo -e "${PURPLE}============================================${NC}"
echo -e "${PURPLE}  Docker Desktop Shutdown${NC}"
echo -e "${PURPLE}============================================${NC}"

# Check if Docker is installed
echo -e "${BLUE}🔍 Checking Docker installation...${NC}"
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker is not installed${NC}"
    exit 1
fi

# Check if Docker daemon is running
echo -e "${BLUE}🔍 Checking Docker daemon status...${NC}"
if ! docker info &> /dev/null; then
    echo -e "${GREEN}✅ Docker daemon is not running${NC}"
    echo -e "${BLUE}ℹ️  Nothing to stop${NC}"
    exit 0
fi

echo -e "${YELLOW}⚠️  Docker daemon is running${NC}"

# Stop Docker Compose services first
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
DOCKER_DIR="$PROJECT_DIR/docker"

if [ -f "$DOCKER_DIR/docker-compose.yml" ]; then
    echo -e "${BLUE}🐳 Stopping Docker Compose services...${NC}"
    cd "$DOCKER_DIR"
    
    if docker compose ps | grep -q "Up"; then
        echo -e "${YELLOW}⏳ Stopping MySQL, Redis, and other services...${NC}"
        docker compose down
        echo -e "${GREEN}✅ Docker Compose services stopped${NC}"
    else
        echo -e "${BLUE}ℹ️  No Docker Compose services running${NC}"
    fi
    
    cd "$SCRIPT_DIR"
fi

# Show running containers
RUNNING_CONTAINERS=$(docker ps -q | wc -l | xargs)
if [ "$RUNNING_CONTAINERS" -gt 0 ]; then
    echo -e "${YELLOW}⚠️  Found $RUNNING_CONTAINERS running container(s)${NC}"
    echo -e "${BLUE}📦 Running containers:${NC}"
    docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    echo ""
    
    # Ask user if they want to stop all containers
    read -p "$(echo -e ${YELLOW}Stop all running containers? [y/N]:${NC} )" -n 1 -r
    echo ""
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo -e "${BLUE}🛑 Stopping all containers...${NC}"
        docker stop $(docker ps -q)
        echo -e "${GREEN}✅ All containers stopped${NC}"
    else
        echo -e "${BLUE}ℹ️  Containers left running${NC}"
    fi
fi

# Stop Docker Desktop on macOS
if [ "$(uname)" == "Darwin" ]; then
    echo -e "${BLUE}🛑 Stopping Docker Desktop...${NC}"
    
    # Check if Docker app is running (check multiple process names)
    if pgrep -i "docker" > /dev/null || pgrep -i "com.docker" > /dev/null; then
        echo -e "${YELLOW}⏳ Quitting Docker Desktop application...${NC}"
        osascript -e 'quit app "Docker"' 2>/dev/null || killall Docker 2>/dev/null || true
        
        echo -e "${BLUE}⏳ Waiting for Docker to stop...${NC}"
        
        # Wait for Docker to stop (max 30 seconds)
        MAX_ATTEMPTS=30
        ATTEMPT=1
        
        while [ $ATTEMPT -le $MAX_ATTEMPTS ]; do
            if ! docker info &> /dev/null 2>&1 && ! pgrep -i "docker" > /dev/null; then
                echo ""
                echo -e "${GREEN}✅ Docker Desktop stopped successfully!${NC}"
                
                echo -e "${PURPLE}============================================${NC}"
                echo -e "${GREEN}🎉 Docker shutdown completed!${NC}"
                echo -e "${PURPLE}============================================${NC}"
                
                echo -e "${BLUE}ℹ️  Docker resources released${NC}"
                echo -e "${BLUE}   To start Docker again, run: ./scripts/start-docker.sh${NC}"
                
                exit 0
            fi
            
            if [ $((ATTEMPT % 5)) -eq 0 ]; then
                echo -e "${YELLOW}⏳ Still stopping... ($ATTEMPT/$MAX_ATTEMPTS seconds)${NC}"
            fi
            
            sleep 1
            ATTEMPT=$((ATTEMPT + 1))
        done
        
        # Timeout reached
        echo ""
        echo -e "${YELLOW}⚠️  Docker is taking longer than expected to stop${NC}"
        echo -e "${BLUE}ℹ️  Docker Desktop may still be shutting down in background${NC}"
        echo -e "${BLUE}   You can verify by checking Activity Monitor${NC}"
        exit 0
        
    else
        echo -e "${BLUE}ℹ️  Docker Desktop is not running${NC}"
        exit 0
    fi
    
else
    # Non-macOS systems
    echo -e "${YELLOW}⚠️  This script is optimized for macOS${NC}"
    echo -e "${BLUE}ℹ️  For Linux, try:${NC}"
    echo -e "${BLUE}   sudo systemctl stop docker${NC}"
    echo -e "${BLUE}ℹ️  For Windows, close Docker Desktop manually${NC}"
    exit 1
fi

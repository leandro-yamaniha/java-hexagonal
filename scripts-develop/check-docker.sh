#!/bin/bash

# Docker Health Check Script
# Verifies if Docker is running and checks service status

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

echo -e "${PURPLE}============================================${NC}"
echo -e "${PURPLE}  Docker Health Check${NC}"
echo -e "${PURPLE}============================================${NC}"

# Check if Docker is installed
echo -e "${BLUE}🔍 Checking Docker installation...${NC}"
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker is not installed${NC}"
    echo -e "${YELLOW}   Please install Docker Desktop from: https://www.docker.com/products/docker-desktop${NC}"
    exit 1
else
    DOCKER_VERSION=$(docker --version)
    echo -e "${GREEN}✅ Docker is installed: $DOCKER_VERSION${NC}"
fi

# Check if Docker daemon is running
echo -e "${BLUE}🔍 Checking Docker daemon...${NC}"
if ! docker info &> /dev/null; then
    echo -e "${RED}❌ Docker daemon is not running${NC}"
    echo -e "${YELLOW}   Please start Docker Desktop${NC}"
    exit 1
else
    echo -e "${GREEN}✅ Docker daemon is running${NC}"
fi

# Check if docker-compose is available
echo -e "${BLUE}🔍 Checking docker-compose...${NC}"
if docker compose version &> /dev/null; then
    COMPOSE_VERSION=$(docker compose version)
    echo -e "${GREEN}✅ Docker Compose is available: $COMPOSE_VERSION${NC}"
elif command -v docker-compose &> /dev/null; then
    COMPOSE_VERSION=$(docker-compose --version)
    echo -e "${GREEN}✅ Docker Compose is available: $COMPOSE_VERSION${NC}"
else
    echo -e "${RED}❌ Docker Compose is not available${NC}"
    exit 1
fi

# Check Docker resources
echo -e "${BLUE}🔍 Checking Docker resources...${NC}"
RUNNING_CONTAINERS=$(docker ps -q | wc -l | xargs)
TOTAL_CONTAINERS=$(docker ps -a -q | wc -l | xargs)
IMAGES=$(docker images -q | wc -l | xargs)

echo -e "${BLUE}   Running containers: ${GREEN}$RUNNING_CONTAINERS${NC}"
echo -e "${BLUE}   Total containers: ${BLUE}$TOTAL_CONTAINERS${NC}"
echo -e "${BLUE}   Images: ${BLUE}$IMAGES${NC}"

# Check project docker-compose services
echo -e "${BLUE}🐳 Checking restaurant management services...${NC}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
DOCKER_DIR="$PROJECT_DIR/docker"

if [ -f "$DOCKER_DIR/docker-compose.yml" ]; then
    echo -e "${GREEN}✅ docker-compose.yml found${NC}"
    
    cd "$DOCKER_DIR"
    
    # Check if services are running
    if docker compose ps | grep -q "Up"; then
        echo -e "${GREEN}✅ Docker Compose services are running:${NC}"
        echo ""
        docker compose ps
        echo ""
        
        # Check MySQL specifically
        if docker compose ps mysql | grep -q "healthy"; then
            echo -e "${GREEN}✅ MySQL is healthy and ready${NC}"
            
            # Get MySQL connection info
            MYSQL_PORT=$(docker compose port mysql 3306 2>/dev/null | cut -d: -f2)
            if [ ! -z "$MYSQL_PORT" ]; then
                echo -e "${BLUE}   MySQL port: $MYSQL_PORT${NC}"
            fi
        elif docker compose ps mysql | grep -q "Up"; then
            echo -e "${YELLOW}⚠️  MySQL is running but not yet healthy${NC}"
        else
            echo -e "${RED}❌ MySQL is not running${NC}"
        fi
        
        # Check Redis specifically
        if docker compose ps redis | grep -q "Up"; then
            echo -e "${GREEN}✅ Redis is running${NC}"
            
            # Get Redis connection info
            REDIS_PORT=$(docker compose port redis 6379 2>/dev/null | cut -d: -f2)
            if [ ! -z "$REDIS_PORT" ]; then
                echo -e "${BLUE}   Redis port: $REDIS_PORT${NC}"
            fi
        else
            echo -e "${YELLOW}⚠️  Redis is not running${NC}"
        fi
    else
        echo -e "${YELLOW}⚠️  Docker Compose services are not running${NC}"
        echo -e "${BLUE}   To start services, run: cd docker && docker compose up -d${NC}"
    fi
else
    echo -e "${RED}❌ docker-compose.yml not found in $DOCKER_DIR${NC}"
fi

# Network connectivity check
echo -e "${BLUE}🌐 Checking network connectivity...${NC}"

if command -v nc &> /dev/null; then
    # Check MySQL port
    if nc -z localhost 3306 2>/dev/null; then
        echo -e "${GREEN}✅ MySQL port 3306 is accessible${NC}"
    else
        echo -e "${YELLOW}⚠️  MySQL port 3306 is not accessible${NC}"
    fi
    
    # Check Redis port
    if nc -z localhost 6379 2>/dev/null; then
        echo -e "${GREEN}✅ Redis port 6379 is accessible${NC}"
    else
        echo -e "${YELLOW}⚠️  Redis port 6379 is not accessible${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  'nc' command not available, skipping port checks${NC}"
fi

# Summary
echo -e "${PURPLE}============================================${NC}"
echo -e "${PURPLE}  Summary${NC}"
echo -e "${PURPLE}============================================${NC}"

if docker info &> /dev/null && docker compose ps 2>/dev/null | grep -q "healthy"; then
    echo -e "${GREEN}🎉 Docker environment is healthy and ready!${NC}"
    echo -e "${GREEN}   You can start the backend applications now.${NC}"
    exit 0
elif docker info &> /dev/null; then
    echo -e "${YELLOW}⚠️  Docker is running but services need attention${NC}"
    echo -e "${BLUE}   Run: ./scripts/start-all-backends.sh${NC}"
    echo -e "${BLUE}   This will automatically start required services${NC}"
    exit 0
else
    echo -e "${RED}❌ Docker is not ready${NC}"
    echo -e "${YELLOW}   Please start Docker Desktop and try again${NC}"
    exit 1
fi

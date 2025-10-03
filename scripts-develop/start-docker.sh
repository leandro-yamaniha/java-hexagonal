#!/bin/bash

# Docker Startup Script for macOS
# Starts Docker Desktop and waits for it to be ready

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

echo -e "${PURPLE}============================================${NC}"
echo -e "${PURPLE}  Docker Desktop Startup${NC}"
echo -e "${PURPLE}============================================${NC}"

# Check if Docker is installed
echo -e "${BLUE}🔍 Checking Docker installation...${NC}"
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker is not installed${NC}"
    echo -e "${YELLOW}📥 Please install Docker Desktop from: https://www.docker.com/products/docker-desktop${NC}"
    echo -e "${YELLOW}   Or use Homebrew: brew install --cask docker${NC}"
    exit 1
else
    DOCKER_VERSION=$(docker --version)
    echo -e "${GREEN}✅ Docker is installed: $DOCKER_VERSION${NC}"
fi

# Check if Docker daemon is already running
echo -e "${BLUE}🔍 Checking Docker daemon status...${NC}"
if docker info &> /dev/null; then
    echo -e "${GREEN}✅ Docker daemon is already running${NC}"
    echo -e "${GREEN}🎉 Docker is ready to use!${NC}"
    exit 0
fi

echo -e "${YELLOW}⚠️  Docker daemon is not running${NC}"

# Check if Docker Desktop app exists (macOS)
if [ "$(uname)" == "Darwin" ]; then
    DOCKER_APP="/Applications/Docker.app"
    
    if [ ! -d "$DOCKER_APP" ]; then
        echo -e "${RED}❌ Docker Desktop not found at $DOCKER_APP${NC}"
        echo -e "${YELLOW}📥 Please install Docker Desktop${NC}"
        exit 1
    fi
    
    echo -e "${BLUE}🚀 Starting Docker Desktop...${NC}"
    echo -e "${YELLOW}⏳ This may take 30-60 seconds...${NC}"
    
    # Start Docker Desktop
    open -a Docker
    
    # Wait for Docker daemon to be ready
    MAX_ATTEMPTS=60
    ATTEMPT=1
    
    echo -e "${BLUE}⏳ Waiting for Docker daemon to start...${NC}"
    
    while [ $ATTEMPT -le $MAX_ATTEMPTS ]; do
        if docker info &> /dev/null; then
            echo ""
            echo -e "${GREEN}✅ Docker daemon is now running!${NC}"
            
            # Additional check for docker compose
            if docker compose version &> /dev/null; then
                echo -e "${GREEN}✅ Docker Compose is available${NC}"
            fi
            
            echo -e "${PURPLE}============================================${NC}"
            echo -e "${GREEN}🎉 Docker Desktop started successfully!${NC}"
            echo -e "${PURPLE}============================================${NC}"
            
            # Show Docker info
            echo -e "${BLUE}📊 Docker Information:${NC}"
            docker version --format '   Docker Engine: {{.Server.Version}}'
            docker compose version | awk '{print "   " $0}'
            
            echo ""
            echo -e "${GREEN}✅ Ready to start backend applications!${NC}"
            echo -e "${BLUE}   Run: ./scripts/start-all-backends.sh${NC}"
            echo -e "${BLUE}   Or:  ./scripts/check-docker.sh${NC}"
            
            exit 0
        fi
        
        # Progress indicator
        if [ $((ATTEMPT % 5)) -eq 0 ]; then
            echo -e "${YELLOW}⏳ Still waiting... ($ATTEMPT/$MAX_ATTEMPTS seconds)${NC}"
        fi
        
        sleep 1
        ATTEMPT=$((ATTEMPT + 1))
    done
    
    # Timeout reached
    echo ""
    echo -e "${RED}❌ Timeout: Docker daemon did not start within $MAX_ATTEMPTS seconds${NC}"
    echo -e "${YELLOW}🔍 Troubleshooting:${NC}"
    echo -e "${YELLOW}   1. Check if Docker Desktop is opening in your Applications${NC}"
    echo -e "${YELLOW}   2. Look for error messages in Docker Desktop${NC}"
    echo -e "${YELLOW}   3. Try starting Docker Desktop manually${NC}"
    echo -e "${YELLOW}   4. Check system resources (RAM, disk space)${NC}"
    echo -e "${YELLOW}   5. Restart your computer if needed${NC}"
    exit 1
    
else
    # Non-macOS systems
    echo -e "${YELLOW}⚠️  This script is optimized for macOS${NC}"
    echo -e "${BLUE}ℹ️  For Linux, try:${NC}"
    echo -e "${BLUE}   sudo systemctl start docker${NC}"
    echo -e "${BLUE}ℹ️  For Windows, start Docker Desktop manually${NC}"
    exit 1
fi

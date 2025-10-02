#!/bin/bash

# Stop All Backend Applications Script

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

echo -e "${PURPLE}============================================${NC}"
echo -e "${PURPLE}  Stopping All Backend Applications${NC}"
echo -e "${PURPLE}============================================${NC}"

# Function to stop application on port
stop_app() {
    local port=$1
    local app_name=$2
    
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
        echo -e "${YELLOW}🛑 Stopping $app_name on port $port...${NC}"
        PID=$(lsof -ti:$port)
        if [ ! -z "$PID" ]; then
            kill -15 $PID  # Graceful shutdown first
            sleep 3
            
            # Check if still running
            if kill -0 $PID 2>/dev/null; then
                echo -e "${YELLOW}⚠️  $app_name still running, forcing shutdown...${NC}"
                kill -9 $PID
                sleep 1
            fi
            
            echo -e "${GREEN}✅ $app_name stopped${NC}"
        fi
    else
        echo -e "${BLUE}ℹ️  $app_name is not running on port $port${NC}"
    fi
}

# Stop all applications
stop_app 8081 "Quarkus"
stop_app 8082 "Spring Boot"
stop_app 8083 "Micronaut"

# Clean up any remaining Java processes related to restaurant management
echo -e "${BLUE}🧹 Cleaning up any remaining processes...${NC}"
pkill -f "restaurant" || true

echo -e "${PURPLE}============================================${NC}"
echo -e "${GREEN}✅ All backend applications stopped!${NC}"
echo -e "${PURPLE}============================================${NC}"

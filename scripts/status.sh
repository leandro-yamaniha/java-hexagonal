#!/bin/bash

# Status Check Script - Shows which backend applications are running

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

echo -e "${PURPLE}============================================${NC}"
echo -e "${PURPLE}  Backend Applications Status${NC}"
echo -e "${PURPLE}============================================${NC}"

# Function to check if a port is in use and get PID
check_app_status() {
    local port=$1
    local app_name=$2
    local swagger_path=$3
    
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
        PID=$(lsof -ti:$port)
        MEMORY=$(ps -o rss= -p $PID | awk '{print $1/1024 " MB"}')
        UPTIME=$(ps -o etime= -p $PID | xargs)
        
        echo -e "${GREEN}✅ $app_name${NC}"
        echo -e "   ${BLUE}Port:${NC} $port"
        echo -e "   ${BLUE}PID:${NC} $PID"
        echo -e "   ${BLUE}Memory:${NC} $MEMORY"
        echo -e "   ${BLUE}Uptime:${NC} $UPTIME"
        echo -e "   ${BLUE}URL:${NC} http://localhost:$port"
        echo -e "   ${BLUE}Swagger:${NC} http://localhost:$port$swagger_path"
        echo ""
    else
        echo -e "${RED}❌ $app_name${NC}"
        echo -e "   ${YELLOW}Not running on port $port${NC}"
        echo ""
    fi
}

# Check all three applications
check_app_status 8081 "Quarkus" "/q/swagger-ui"
check_app_status 8082 "Spring Boot" "/swagger-ui.html"
check_app_status 8083 "Micronaut" "/swagger-ui"

# Summary
echo -e "${PURPLE}============================================${NC}"

RUNNING_COUNT=0
if lsof -Pi :8081 -sTCP:LISTEN -t >/dev/null ; then ((RUNNING_COUNT++)); fi
if lsof -Pi :8082 -sTCP:LISTEN -t >/dev/null ; then ((RUNNING_COUNT++)); fi
if lsof -Pi :8083 -sTCP:LISTEN -t >/dev/null ; then ((RUNNING_COUNT++)); fi

if [ $RUNNING_COUNT -eq 3 ]; then
    echo -e "${GREEN}🎉 All 3 applications are running${NC}"
elif [ $RUNNING_COUNT -eq 0 ]; then
    echo -e "${YELLOW}⚠️  No applications are running${NC}"
    echo -e "${BLUE}   Run: ./scripts/start-all-backends.sh${NC}"
else
    echo -e "${YELLOW}⚠️  $RUNNING_COUNT out of 3 applications are running${NC}"
fi

echo -e "${PURPLE}============================================${NC}"

# Check prerequisites
echo -e "${BLUE}Prerequisites Status:${NC}"

# MySQL
if nc -z localhost 3306 2>/dev/null; then
    echo -e "${GREEN}✅ MySQL (port 3306)${NC}"
else
    echo -e "${RED}❌ MySQL (port 3306)${NC}"
fi

# Redis
if nc -z localhost 6379 2>/dev/null; then
    echo -e "${GREEN}✅ Redis (port 6379)${NC}"
else
    echo -e "${YELLOW}⚠️  Redis (port 6379) - Optional${NC}"
fi

echo -e "${PURPLE}============================================${NC}"

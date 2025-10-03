#!/bin/bash

# Stop All Services Script

echo "🛑 Stopping Restaurant Management Infrastructure..."

# Stop all services
docker-compose -f docker-compose.yml \
               -f docker-compose.spring.yml \
               -f docker-compose.quarkus.yml \
               -f docker-compose.micronaut.yml \
               down

echo ""
echo "✅ All services stopped!"
echo ""
echo "💡 To remove volumes as well, use: docker-compose ... down -v"

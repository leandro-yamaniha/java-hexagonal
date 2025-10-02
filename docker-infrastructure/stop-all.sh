#!/bin/bash

# Stop All Services Script

echo "🛑 Stopping Restaurant Management Infrastructure..."

# Stop all services
docker-compose down

echo ""
echo "✅ All services stopped!"
echo ""
echo "💡 To remove volumes as well, use: docker-compose down -v"

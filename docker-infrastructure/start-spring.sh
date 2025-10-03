#!/bin/bash

# Start Spring Boot Backend Only
# This script starts only the Spring Boot backend with infrastructure

echo "🚀 Starting Spring Boot Backend..."
echo ""

docker-compose -f docker-compose.yml -f docker-compose.spring.yml up -d --build

echo ""
echo "✅ Spring Boot backend is running!"
echo ""
echo "📍 Access:"
echo "   API:  http://localhost/api/spring/customers"
echo ""
echo "📋 Commands:"
echo "   Logs:  docker-compose -f docker-compose.yml -f docker-compose.spring.yml logs -f"
echo "   Stop:  docker-compose -f docker-compose.yml -f docker-compose.spring.yml down"
echo ""

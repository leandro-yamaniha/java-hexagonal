#!/bin/bash

# Start All Services Script
# This script starts the complete infrastructure

echo "🚀 Starting Restaurant Management Infrastructure..."
echo ""

# Check if docker-compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ docker-compose not found. Please install Docker Compose."
    exit 1
fi

# Build and start all services
echo "📦 Building and starting all services..."
docker-compose up -d --build

# Wait for services to be healthy
echo ""
echo "⏳ Waiting for services to be healthy..."
sleep 10

# Check service status
echo ""
echo "📊 Service Status:"
docker-compose ps

# Show useful URLs
echo ""
echo "✅ Infrastructure is running!"
echo ""
echo "📍 Access Points:"
echo "   Frontend:     http://localhost"
echo "   Spring Boot:  http://localhost/api/spring/customers"
echo "   Quarkus:      http://localhost/api/quarkus/customers"
echo "   Micronaut:    http://localhost/api/micronaut/customers"
echo "   MySQL:        localhost:3306"
echo "   Redis:        localhost:6379"
echo ""
echo "📋 Useful Commands:"
echo "   View logs:    docker-compose logs -f"
echo "   Stop all:     docker-compose down"
echo "   Restart:      docker-compose restart"
echo ""

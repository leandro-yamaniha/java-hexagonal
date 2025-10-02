#!/bin/bash

# Start Micronaut Backend Only
# This script starts only the Micronaut backend with infrastructure

echo "🔥 Starting Micronaut Backend..."
echo ""

docker-compose -f docker-compose.yml -f docker-compose.micronaut.yml up -d --build

echo ""
echo "✅ Micronaut backend is running!"
echo ""
echo "📍 Access:"
echo "   API:  http://localhost/api/micronaut/customers"
echo ""
echo "📋 Commands:"
echo "   Logs:  docker-compose -f docker-compose.yml -f docker-compose.micronaut.yml logs -f"
echo "   Stop:  docker-compose -f docker-compose.yml -f docker-compose.micronaut.yml down"
echo ""

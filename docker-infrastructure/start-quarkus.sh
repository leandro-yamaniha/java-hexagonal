#!/bin/bash

# Start Quarkus Backend Only
# This script starts only the Quarkus backend with infrastructure

echo "⚡ Starting Quarkus Backend..."
echo ""

docker-compose -f docker-compose.yml -f docker-compose.quarkus.yml up -d --build

echo ""
echo "✅ Quarkus backend is running!"
echo ""
echo "📍 Access:"
echo "   API:  http://localhost/api/quarkus/customers"
echo ""
echo "📋 Commands:"
echo "   Logs:  docker-compose -f docker-compose.yml -f docker-compose.quarkus.yml logs -f"
echo "   Stop:  docker-compose -f docker-compose.yml -f docker-compose.quarkus.yml down"
echo ""

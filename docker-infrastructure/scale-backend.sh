#!/bin/bash

# Scale Backend Script
# Usage: ./scale-backend.sh [spring|quarkus|micronaut]

FRAMEWORK=$1

if [ -z "$FRAMEWORK" ]; then
    echo "Usage: ./scale-backend.sh [spring|quarkus|micronaut]"
    echo ""
    echo "Examples:"
    echo "  ./scale-backend.sh spring     # Start Spring Boot only (2 instances)"
    echo "  ./scale-backend.sh quarkus    # Start Quarkus only (2 instances)"
    echo "  ./scale-backend.sh micronaut  # Start Micronaut only (2 instances)"
    exit 1
fi

case "$FRAMEWORK" in
    spring)
        echo "🚀 Starting Spring Boot backend (2 instances)..."
        docker-compose -f docker-compose.yml -f docker-compose.spring.yml up -d --build
        ;;
    quarkus)
        echo "⚡ Starting Quarkus backend (2 instances)..."
        docker-compose -f docker-compose.yml -f docker-compose.quarkus.yml up -d --build
        ;;
    micronaut)
        echo "🔥 Starting Micronaut backend (2 instances)..."
        docker-compose -f docker-compose.yml -f docker-compose.micronaut.yml up -d --build
        ;;
    *)
        echo "❌ Unknown framework: $FRAMEWORK"
        echo "Valid options: spring, quarkus, micronaut"
        exit 1
        ;;
esac

echo ""
echo "✅ Backend started successfully!"
echo ""
echo "📊 Container Status:"
docker-compose -f docker-compose.yml -f docker-compose.$FRAMEWORK.yml ps

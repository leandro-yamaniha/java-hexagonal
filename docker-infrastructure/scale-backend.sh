#!/bin/bash

# Scale Backend Script
# Usage: ./scale-backend.sh [spring|quarkus|micronaut]

FRAMEWORK=$1

if [ -z "$FRAMEWORK" ]; then
    echo "Usage: ./scale-backend.sh [spring|quarkus|micronaut]"
    echo ""
    echo "Examples:"
    echo "  ./scale-backend.sh spring     # Scale Spring Boot only"
    echo "  ./scale-backend.sh quarkus    # Scale Quarkus only"
    echo "  ./scale-backend.sh micronaut  # Scale Micronaut only"
    exit 1
fi

case "$FRAMEWORK" in
    spring)
        echo "🚀 Scaling Spring Boot backend (2 instances)..."
        docker-compose up -d spring-boot-app-1 spring-boot-app-2
        ;;
    quarkus)
        echo "⚡ Scaling Quarkus backend (2 instances)..."
        docker-compose up -d quarkus-app-1 quarkus-app-2
        ;;
    micronaut)
        echo "🔥 Scaling Micronaut backend (2 instances)..."
        docker-compose up -d micronaut-app-1 micronaut-app-2
        ;;
    *)
        echo "❌ Unknown framework: $FRAMEWORK"
        echo "Valid options: spring, quarkus, micronaut"
        exit 1
        ;;
esac

echo ""
echo "✅ Backend scaled successfully!"
echo ""
echo "📊 Container Status:"
docker-compose ps | grep $FRAMEWORK

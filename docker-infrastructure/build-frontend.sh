#!/bin/bash

# Build Frontend Script
# This script builds the Angular frontend and copies it to the infrastructure

echo "🎨 Building Angular Frontend..."
echo ""

# Check if frontend-angular exists
if [ ! -d "../frontend-angular" ]; then
    echo "❌ Frontend directory not found: ../frontend-angular"
    exit 1
fi

# Go to frontend directory
cd ../frontend-angular

# Check if node_modules exists
if [ ! -d "node_modules" ]; then
    echo "📦 Installing dependencies..."
    npm install
fi

# Build the application
echo "🔨 Building production bundle..."
npm run build --prod

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

# Copy build to docker-infrastructure
echo "📋 Copying build to docker-infrastructure..."
cd ../docker-infrastructure

# Create frontend/dist directory if it doesn't exist
mkdir -p frontend/dist

# Copy the built files
if [ -d "../frontend-angular/dist" ]; then
    cp -r ../frontend-angular/dist/* frontend/dist/
    echo "✅ Frontend built and copied successfully!"
    echo ""
    echo "📍 Files copied to: docker-infrastructure/frontend/dist/"
    echo ""
    echo "🚀 You can now start the infrastructure:"
    echo "   ./start-all.sh"
else
    echo "❌ Build directory not found!"
    exit 1
fi

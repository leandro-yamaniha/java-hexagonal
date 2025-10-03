# Multi-stage build for Angular Frontend

# Stage 1: Builder
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production

# Copy source code
COPY . .

# Build the application
RUN npm run build --prod

# Stage 2: Runtime with Nginx (optional, can use volume with main nginx)
FROM nginx:alpine AS runtime

# Copy built files from builder
COPY --from=builder /app/dist/restaurant-frontend /usr/share/nginx/html

# Copy custom nginx config if needed
# COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

-- Restaurant Management Database Schema

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS restaurant_db;
USE restaurant_db;

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX idx_customer_email (email),
    INDEX idx_customer_active (active),
    INDEX idx_customer_name (name)
);

-- Menu items table
CREATE TABLE IF NOT EXISTS menu_items (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    category ENUM('APPETIZER', 'MAIN_COURSE', 'DESSERT', 'BEVERAGE', 'SALAD', 'SOUP', 'PASTA', 'PIZZA', 'SEAFOOD', 'MEAT', 'VEGETARIAN', 'VEGAN', 'GLUTEN_FREE', 'KIDS_MENU', 'SPECIAL') NOT NULL,
    image_url VARCHAR(500),
    available BOOLEAN NOT NULL DEFAULT TRUE,
    preparation_time_minutes INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_menu_item_name (name),
    INDEX idx_menu_item_category (category),
    INDEX idx_menu_item_available (available)
);

-- Restaurant tables
CREATE TABLE IF NOT EXISTS restaurant_tables (
    id BINARY(16) PRIMARY KEY,
    table_number VARCHAR(10) NOT NULL UNIQUE,
    capacity INT NOT NULL,
    status ENUM('AVAILABLE', 'OCCUPIED', 'RESERVED', 'CLEANING', 'OUT_OF_SERVICE') NOT NULL DEFAULT 'AVAILABLE',
    location VARCHAR(200),
    current_order_id BINARY(16),
    last_occupied_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_table_number (table_number),
    INDEX idx_table_status (status),
    INDEX idx_table_capacity (capacity)
);

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    id BINARY(16) PRIMARY KEY,
    customer_id BINARY(16) NOT NULL,
    table_id BINARY(16),
    status ENUM('PENDING', 'CONFIRMED', 'PREPARING', 'READY', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    special_instructions VARCHAR(500),
    order_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estimated_delivery_time TIMESTAMP,
    actual_delivery_time TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_order_customer (customer_id),
    INDEX idx_order_table (table_id),
    INDEX idx_order_status (status),
    INDEX idx_order_time (order_time),
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (table_id) REFERENCES restaurant_tables(id) ON DELETE SET NULL
);

-- Order items table
CREATE TABLE IF NOT EXISTS order_items (
    id BINARY(16) PRIMARY KEY,
    order_id BINARY(16) NOT NULL,
    menu_item_id BINARY(16) NOT NULL,
    menu_item_name VARCHAR(100) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    quantity INT NOT NULL DEFAULT 1,
    notes VARCHAR(200),
    INDEX idx_order_item_order (order_id),
    INDEX idx_order_item_menu (menu_item_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE
);

-- Insert sample data
INSERT INTO customers (id, name, email, phone, address) VALUES
(UNHEX(REPLACE(UUID(), '-', '')), 'John Doe', 'john.doe@email.com', '+1234567890', '123 Main St, City, State'),
(UNHEX(REPLACE(UUID(), '-', '')), 'Jane Smith', 'jane.smith@email.com', '+1234567891', '456 Oak Ave, City, State'),
(UNHEX(REPLACE(UUID(), '-', '')), 'Bob Johnson', 'bob.johnson@email.com', '+1234567892', '789 Pine Rd, City, State');

INSERT INTO menu_items (id, name, description, price, category, preparation_time_minutes) VALUES
(UNHEX(REPLACE(UUID(), '-', '')), 'Caesar Salad', 'Fresh romaine lettuce with caesar dressing', 12.99, 'SALAD', 10),
(UNHEX(REPLACE(UUID(), '-', '')), 'Grilled Salmon', 'Atlantic salmon with herbs and lemon', 24.99, 'MAIN_COURSE', 25),
(UNHEX(REPLACE(UUID(), '-', '')), 'Margherita Pizza', 'Classic pizza with tomato, mozzarella, and basil', 18.99, 'PIZZA', 20),
(UNHEX(REPLACE(UUID(), '-', '')), 'Chocolate Cake', 'Rich chocolate cake with vanilla ice cream', 8.99, 'DESSERT', 5),
(UNHEX(REPLACE(UUID(), '-', '')), 'House Wine', 'Red or white wine selection', 7.99, 'BEVERAGE', 2);

INSERT INTO restaurant_tables (id, table_number, capacity, location) VALUES
(UNHEX(REPLACE(UUID(), '-', '')), 'T01', 2, 'Window side'),
(UNHEX(REPLACE(UUID(), '-', '')), 'T02', 4, 'Center area'),
(UNHEX(REPLACE(UUID(), '-', '')), 'T03', 6, 'Private section'),
(UNHEX(REPLACE(UUID(), '-', '')), 'T04', 2, 'Bar area'),
(UNHEX(REPLACE(UUID(), '-', '')), 'T05', 8, 'Large party section');

-- Restaurant Management System Database Initialization
-- This script creates the initial database structure

USE restaurant_db;

-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create menu_items table
CREATE TABLE IF NOT EXISTS menu_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Create order_items table
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Insert sample customers
INSERT INTO customers (name, email, phone, active) VALUES
('João Silva', 'joao.silva@email.com', '11987654321', TRUE),
('Maria Santos', 'maria.santos@email.com', '11876543210', TRUE),
('Pedro Oliveira', 'pedro.oliveira@email.com', '11765432109', TRUE)
ON DUPLICATE KEY UPDATE name=name;

-- Insert sample menu items
INSERT INTO menu_items (name, description, price, category, available) VALUES
('Hambúrguer Clássico', 'Hambúrguer artesanal com queijo, alface e tomate', 25.90, 'MAIN_COURSE', TRUE),
('Pizza Margherita', 'Pizza tradicional com molho de tomate, mussarela e manjericão', 35.00, 'MAIN_COURSE', TRUE),
('Salada Caesar', 'Alface romana, croutons, parmesão e molho caesar', 18.50, 'APPETIZER', TRUE),
('Refrigerante', 'Refrigerante 350ml', 5.00, 'BEVERAGE', TRUE),
('Brownie com Sorvete', 'Brownie de chocolate com sorvete de baunilha', 15.00, 'DESSERT', TRUE)
ON DUPLICATE KEY UPDATE name=name;

-- Create indexes for better performance
CREATE INDEX idx_customer_email ON customers(email);
CREATE INDEX idx_customer_active ON customers(active);
CREATE INDEX idx_menu_item_category ON menu_items(category);
CREATE INDEX idx_menu_item_available ON menu_items(available);
CREATE INDEX idx_order_customer ON orders(customer_id);
CREATE INDEX idx_order_status ON orders(status);

-- Grant permissions
GRANT ALL PRIVILEGES ON restaurant_db.* TO 'restaurant_user'@'%';
FLUSH PRIVILEGES;

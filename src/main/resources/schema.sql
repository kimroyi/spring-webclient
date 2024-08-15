-- Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR2(20) NOT NULL,
    username VARCHAR2(50) NOT NULL,
    password VARCHAR2(100) NOT NULL,
    update_date TIMESTAMP,
    PRIMARY KEY (user_id)
);

-- Orders Table
CREATE TABLE IF NOT EXISTS orders (
    order_id NUMBER NOT NULL,
    user_id VARCHAR2(20) NOT NULL,
    order_date TIMESTAMP,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users (user_id)
);

-- Products Table
CREATE TABLE IF NOT EXISTS products (
    product_id NUMBER PRIMARY KEY,
    product_name VARCHAR2(100) NOT NULL,
    price NUMBER
);

-- OrderDetail Table
CREATE TABLE IF NOT EXISTS order_details (
    order_detail_id NUMBER PRIMARY KEY,
    order_id NUMBER,
    product_id NUMBER,
    quantity NUMBER,
    CONSTRAINT fk_order
        FOREIGN KEY (order_id)
        REFERENCES orders (order_id),
    CONSTRAINT fk_product
        FOREIGN KEY (product_id)
        REFERENCES products (product_id)
);
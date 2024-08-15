INSERT INTO users (user_id, username, password) VALUES
('user1', '이름1', 'password1'),
('user2', '이름2', 'password2'),
('user3', '이름3', 'password3');

INSERT INTO products (product_id, product_name, price) VALUES
(1, 'Laptop', 1200),
(2, 'Smartphone', 800),
(3, 'Tablet', 300);

INSERT INTO orders (order_id, user_id, order_date) VALUES
(1001, 'user1', TIMESTAMP '2024-08-01 10:00:00');
INSERT INTO orders (order_id, user_id, order_date) VALUES
(1002, 'user1', TIMESTAMP '2024-08-01 11:00:00');

INSERT INTO order_details (order_detail_id, order_id, product_id, quantity) VALUES
(1, 1001, 1, 1),
(2, 1001, 2, 2),
(3, 1002, 3, 1);
INSERT INTO users (user_id, username, password, update_date) VALUES
('user1', '이름1', 'password1', TIMESTAMP '2024-08-01 10:00:00'),
('user2', '이름2', 'password2', TIMESTAMP '2024-08-02 10:00:00'),
('user3', '이름2', 'password3', TIMESTAMP '2024-08-03 10:00:00'),
('user4', '이름2', 'password', TIMESTAMP '2024-08-03 11:00:00'),
('user5', '이름2', 'password', TIMESTAMP '2024-08-03 12:00:00'),
('user6', '이름2', 'password', TIMESTAMP '2024-08-03 13:00:00'),
('user7', '이름2', 'password', TIMESTAMP '2024-08-03 14:00:00'),
('user8', '이름2', 'password', TIMESTAMP '2024-08-03 14:00:00'),
('user9', '이름2', 'password', TIMESTAMP '2024-08-03 14:30:00'),
('user10', '이름2', 'password', TIMESTAMP '2024-08-03 14:40:00'),
('user11', '이름2', 'password', TIMESTAMP '2024-08-03 20:00:00'),
('user12', '이름2', 'password', TIMESTAMP '2024-08-03 23:00:00'),
('user13', '이름2', 'password', TIMESTAMP '2024-08-03 23:59:00'),
('user14', '이름2', 'password', TIMESTAMP '2024-08-04 14:00:00'),
('user15', '이름2', 'password', TIMESTAMP '2024-08-05 14:00:00')
;

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
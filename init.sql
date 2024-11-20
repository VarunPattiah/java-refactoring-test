CREATE TABLE IF NOT EXISTS userinfo (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(100),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE IF NOT EXISTS roles (
    user_id INTEGER NOT NULL,
    role VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES userinfo(user_id)
);

-- Insert 10 records
INSERT INTO userinfo (user_name, email) VALUES
('Varun Sankaralingam', 'varun.sankaralingam@sap.com'),
('Jane Smith', 'jane.smith@example.com'),
('Michael Johnson', 'michael.johnson@example.com'),
('Emily Davis', 'emily.davis@example.com'),
('David Wilson', 'david.wilson@example.com'),
('Sarah Brown', 'sarah.brown@example.com'),
('James Garcia', 'james.garcia@example.com'),
('Mary Martinez', 'mary.martinez@example.com'),
('Robert Lee', 'robert.lee@example.com'),
('Linda Walker', 'linda.walker@example.com');

-- Add roles to users
INSERT INTO roles (user_id, role) VALUES
(1, 'ADMIN'),
(2, 'USER'),
(3, 'USER'),
(4, 'USER'),
(5, 'USER'),
(6, 'USER'),
(7, 'ADMIN'),
(8, 'USER'),
(9, 'USER'),
(10, 'USER');

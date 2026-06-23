CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    token VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- inject untuk testing lokal
INSERT INTO users (username, password, role, token, created_at) 
VALUES 
    ('admin_user', 'secure_password', 'ADMIN', 'token-admin', NOW()),
    ('staff_user', 'secure_password', 'STAFF', 'token-staff', NOW()),
    ('approver_user', 'secure_password', 'APPROVER', 'token-approver', NOW()),
    ('manager_user', 'secure_password', 'MANAGER', 'token-manager', NOW());
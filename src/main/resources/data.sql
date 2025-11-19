-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Insert roles
INSERT INTO role (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_DOCTOR'),
       ('ROLE_NURSE');
INSERT INTO department (name, created_at, updated_at)
VALUES ('Cardiology', now(), now()),
       ('Neurology', now(), now()),
       ('Pediatrics', now(), now()),
       ('Oncology', now(), now());

-- Insert initial admin user with UUID
INSERT INTO users (id,
                   first_name,
                   last_name,
                   username,
                   email,
                   password,
                   is_active,
                   created_at,
                   updated_at)
VALUES (gen_random_uuid(), -- generate UUID
        'Super',
        'Admin',
        'admin',
        'admin@pulsecare.com',
        '$2a$12$3YTLMPGicXFsFcK4o3/AJu9tPP8SYcVlr7CHayZak7EfueH8HfzHS', -- bcrypt 'admin123'
        TRUE,
        now(),
        now());

-- Assign ADMIN role to the initial admin user
INSERT INTO user_role (user_id, role_id)
SELECT id, 1
FROM users
WHERE username = 'admin';

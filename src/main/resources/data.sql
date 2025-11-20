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

INSERT INTO specialization (name, created_at, updated_at) VALUES ('Cardiology', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Neurology', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Pediatrics', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Dermatology', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Orthopedics', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Gynecology', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('General Surgery', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Endocrinology', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Psychiatry', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Radiology', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Oncology', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('ENT', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Urology', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Nephrology', NOW(), NOW());
INSERT INTO specialization (name, created_at, updated_at) VALUES ('Gastroenterology', NOW(), NOW());


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

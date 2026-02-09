-- =========================================================
-- Enable UUID support
-- =========================================================
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =========================================================
-- ROLES
-- =========================================================
INSERT INTO role (name, created_at, updated_at)
VALUES
    ('SUPER_ADMIN',  NOW(), NOW()),
    ('SUPER_DOCTOR', NOW(), NOW()),
    ('SUPER_NURSE',  NOW(), NOW()),
    ('ADMIN',  NOW(), NOW()),
    ('DOCTOR', NOW(), NOW()),
    ('NURSE',  NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

-- =========================================================
-- DEPARTMENTS
-- =========================================================
INSERT INTO department (name, created_at, updated_at)
VALUES
    ('Cardiology', NOW(), NOW()),
    ('Neurology', NOW(), NOW()),
    ('Pediatrics', NOW(), NOW()),
    ('Oncology', NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

-- =========================================================
-- SPECIALIZATIONS
-- =========================================================
INSERT INTO specialization (name, created_at, updated_at)
VALUES
    ('Cardiology', NOW(), NOW()),
    ('Neurology', NOW(), NOW()),
    ('Pediatrics', NOW(), NOW()),
    ('Dermatology', NOW(), NOW()),
    ('Orthopedics', NOW(), NOW()),
    ('Gynecology', NOW(), NOW()),
    ('General Surgery', NOW(), NOW()),
    ('Endocrinology', NOW(), NOW()),
    ('Psychiatry', NOW(), NOW()),
    ('Radiology', NOW(), NOW()),
    ('Oncology', NOW(), NOW()),
    ('ENT', NOW(), NOW()),
    ('Urology', NOW(), NOW()),
    ('Nephrology', NOW(), NOW()),
    ('Gastroenterology', NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

-- =========================================================
-- USERS (ROLE STORED DIRECTLY IN users.role_id)
-- =========================================================

-- ADMIN USER
INSERT INTO users (
    id, first_name, last_name, username, email, password,
    is_active, role_id, created_at, updated_at
)
VALUES (
           gen_random_uuid(),
           'Super',
           'Admin',
           'admin',
           'admin@pulsecare.com',
           '$2a$12$3YTLMPGicXFsFcK4o3/AJu9tPP8SYcVlr7CHayZak7EfueH8HfzHS', -- admin123
           TRUE,
           (SELECT id FROM role WHERE name = 'SUPER_ADMIN'),
           NOW(),
           NOW()
       )
ON CONFLICT (username) DO NOTHING;

-- DOCTOR USER
INSERT INTO users (
    id, first_name, last_name, username, email, password,
    is_active, role_id, created_at, updated_at
)
VALUES (
           gen_random_uuid(),
           'Super',
           'Doctor',
           'doctor',
           'doctor@pulsecare.com',
           '$2a$12$1IyROik02OCOoPVR7q1bEedEAzPybu38KKssC88a15M7dPZ5b73my', -- doctor123
           TRUE,
           (SELECT id FROM role WHERE name = 'SUPER_DOCTOR'),
           NOW(),
           NOW()
       )
ON CONFLICT (username) DO NOTHING;

-- NURSE USER
INSERT INTO users (
    id, first_name, last_name, username, email, password,
    is_active, role_id, created_at, updated_at
)
VALUES (
           gen_random_uuid(),
           'Super',
           'Nurse',
           'nurse',
           'nurse@pulsecare.com',
           '$2a$12$9DR.a6thEGLbOClaujtEV.k/mAjeGXT0bGuZtjLV6kkTO/zZsLmNu', -- nurse123
           TRUE,
           (SELECT id FROM role WHERE name = 'SUPER_NURSE'),
           NOW(),
           NOW()
       )
ON CONFLICT (username) DO NOTHING;

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

---------------------------------------------------------
-- INSERT ROLES
---------------------------------------------------------
INSERT INTO role (name, created_at, updated_at)
VALUES ('ADMIN', now(), now()),
       ('DOCTOR', now(), now()),
       ('NURSE',  now(), now());

---------------------------------------------------------
-- INSERT DEPARTMENTS
---------------------------------------------------------
INSERT INTO department (name, created_at, updated_at)
VALUES ('Cardiology',  now(), now()),
       ('Neurology',   now(), now()),
       ('Pediatrics',  now(), now()),
       ('Oncology',    now(), now());

---------------------------------------------------------
-- INSERT SPECIALIZATIONS
---------------------------------------------------------
INSERT INTO specialization (name, created_at, updated_at)
VALUES ('Cardiology', NOW(), NOW()),
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
       ('Gastroenterology', NOW(), NOW());

---------------------------------------------------------
-- INSERT ADMIN USER
---------------------------------------------------------
INSERT INTO users (id, first_name, last_name, username, email, password,
                   is_active, created_at, updated_at)
VALUES (
           gen_random_uuid(),
           'Super',
           'Admin',
           'admin',
           'admin@pulsecare.com',
           '$2a$12$3YTLMPGicXFsFcK4o3/AJu9tPP8SYcVlr7CHayZak7EfueH8HfzHS', -- admin123
           TRUE,
           NOW(),
           NOW()
       );

---------------------------------------------------------
-- ASSIGN ADMIN ROLE TO ADMIN USER
---------------------------------------------------------
INSERT INTO user_role (user_id, role_id)
VALUES (
           (SELECT id FROM users WHERE username = 'admin'),
           (SELECT id FROM role  WHERE name = 'ADMIN')
       );

---------------------------------------------------------
-- INSERT DOCTOR USER
---------------------------------------------------------
INSERT INTO users (id, first_name, last_name, username, email, password,
                   is_active, created_at, updated_at)
VALUES (
           gen_random_uuid(),
           'Super',
           'Doctor',
           'doctor',
           'doctor@pulsecare.com',
           '$2a$12$1IyROik02OCOoPVR7q1bEedEAzPybu38KKssC88a15M7dPZ5b73my', -- doctor123
           TRUE,
           NOW(),
           NOW()
       );

---------------------------------------------------------
-- ASSIGN DOCTOR ROLE TO DOCTOR USER
---------------------------------------------------------
INSERT INTO user_role (user_id, role_id)
VALUES (
           (SELECT id FROM users WHERE username = 'doctor'),
           (SELECT id FROM role  WHERE name = 'DOCTOR')
       );


---------------------------------------------------------
-- INSERT NURSE USER
---------------------------------------------------------
INSERT INTO users (id, first_name, last_name, username, email, password,
                   is_active, created_at, updated_at)
VALUES (
           gen_random_uuid(),
           'Super',
           'Nurse',
           'nurse',
           'nurse@pulsecare.com',
           '$2a$12$wGk9F/dRZgUS9PB9jFdIsiIp5Bl93kKE/Z1VSB2mQ3G.U5m7T7jSy', -- bcrypt: nurse123
           TRUE,
           NOW(),
           NOW()
       );

---------------------------------------------------------
-- ASSIGN NURSE ROLE TO NURSE USER
---------------------------------------------------------
INSERT INTO user_role (user_id, role_id)
VALUES (
           (SELECT id FROM users WHERE username = 'nurse'),
           (SELECT id FROM role  WHERE name = 'NURSE')
       );

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
-- WARDS
-- =========================================================
INSERT INTO ward (name, bed_count, occupied_beds, department_id, created_at, updated_at)
VALUES
    ('Cardiology Ward A', 10, 0, (SELECT id FROM department WHERE name='Cardiology'), NOW(), NOW()),
    ('Cardiology Ward B', 8,  0, (SELECT id FROM department WHERE name='Cardiology'), NOW(), NOW()),

    ('Neurology Ward A',  10, 0, (SELECT id FROM department WHERE name='Neurology'), NOW(), NOW()),
    ('Neurology Ward B',  6,  0, (SELECT id FROM department WHERE name='Neurology'), NOW(), NOW()),

    ('Pediatrics Ward A', 12, 0, (SELECT id FROM department WHERE name='Pediatrics'), NOW(), NOW()),
    ('Pediatrics Ward B', 8,  0, (SELECT id FROM department WHERE name='Pediatrics'), NOW(), NOW()),

    ('Oncology Ward A',   10, 0, (SELECT id FROM department WHERE name='Oncology'), NOW(), NOW()),
    ('Oncology Ward B',   6,  0, (SELECT id FROM department WHERE name='Oncology'), NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

-- =========================================================
-- BEDS (created based on ward.bed_count)
-- =========================================================
INSERT INTO bed (bed_no, is_taken, ward_id, created_at, updated_at)
SELECT
    CONCAT(
            CASE
                WHEN w.name ILIKE 'Cardiology%' THEN 'C'
                WHEN w.name ILIKE 'Neurology%'  THEN 'N'
                WHEN w.name ILIKE 'Pediatrics%' THEN 'P'
                WHEN w.name ILIKE 'Oncology%'   THEN 'O'
                ELSE 'W'
                END,
            '-',
            LPAD(gs::text, 2, '0')
    ) AS bed_no,
    FALSE AS is_taken,
    w.id AS ward_id,
    NOW() AS created_at,
    NOW() AS updated_at
FROM ward w
         JOIN generate_series(1, 50) gs ON gs <= w.bed_count
WHERE w.name IN (
                 'Cardiology Ward A','Cardiology Ward B',
                 'Neurology Ward A','Neurology Ward B',
                 'Pediatrics Ward A','Pediatrics Ward B',
                 'Oncology Ward A','Oncology Ward B'
    )
ON CONFLICT DO NOTHING;


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

INSERT INTO patient (
    full_name, dob, blood_group, nic, phone, gender, created_at, updated_at
)
VALUES
    ('Nimal Perera',        DATE '1991-03-14', 'O+',  '911234567V',  '0771234567', 'MALE',   NOW(), NOW()),
    ('Kavindi Silva',       DATE '1998-11-02', 'A+',  '987654321V',  '0712345678', 'FEMALE', NOW(), NOW()),
    ('Sahan Fernando',      DATE '1987-07-25', 'B+',  '871112223V',  '0769876543', 'MALE',   NOW(), NOW()),
    ('Dinithi Jayasinghe',  DATE '2000-01-19', 'AB+', '200012345678','0751122334', 'FEMALE', NOW(), NOW()),
    ('Tharindu Wijesinghe', DATE '1995-09-08', 'O-',  '951234567V',  '0784455667', 'MALE',   NOW(), NOW()),
    ('Ishara Gunasekara',   DATE '1993-05-30', 'A-',  '931998877V',  '0703344556', 'FEMALE', NOW(), NOW()),
    ('Chamika Ranasinghe',  DATE '1983-12-12', 'B-',  '831234567V',  '0722233445', 'MALE',   NOW(), NOW()),
    ('Sanduni Abeysekara',  DATE '1999-04-21', 'O+',  '991234567V',  '0778899001', 'FEMALE', NOW(), NOW()),
    ('Malith Karunaratne',  DATE '1990-08-16', 'AB-', '901234567V',  '0745566778', 'MALE',   NOW(), NOW()),
    ('Thilini Samarasinghe',DATE '1996-02-05', 'A+',  '961234567V',  '0719988776', 'FEMALE', NOW(), NOW())
ON CONFLICT (nic) DO NOTHING;
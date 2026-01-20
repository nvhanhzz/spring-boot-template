CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create Tables
CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    created_by UUID,
    updated_by UUID,
    status INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    avatar VARCHAR(255),
    address VARCHAR(255),
    dob DATE,
    settings JSONB
);

CREATE TABLE roles (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    created_by UUID,
    updated_by UUID,
    status INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE permissions (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    created_by UUID,
    updated_by UUID,
    status INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE user_role (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE role_permission (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (permission_id) REFERENCES permissions (id)
);

CREATE TABLE pending_files (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP
    WITH
        TIME ZONE,
        updated_at TIMESTAMP
    WITH
        TIME ZONE,
        created_by UUID,
        updated_by UUID,
        status INTEGER NOT NULL,
        filename VARCHAR(255) NOT NULL UNIQUE,
        original_filename VARCHAR(255) NOT NULL,
        content_type VARCHAR(100),
        size BIGINT
);
-- Insert Roles (lowercase)
INSERT INTO
    roles (
        id,
        created_at,
        updated_at,
        status,
        name,
        description
    )
VALUES
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'admin',
        'Administrator with full access'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'user',
        'Standard user'
    ) ON CONFLICT (name) DO NOTHING;

-- Insert Permissions
-- User Permissions
INSERT INTO
    permissions (
        id,
        created_at,
        updated_at,
        status,
        name,
        description
    )
VALUES
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'user.view',
        'View users'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'user.create',
        'Create users'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'user.update',
        'Update users'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'user.delete',
        'Delete users'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'user.restore',
        'Restore users'
    ) ON CONFLICT (name) DO NOTHING;

-- Role Permissions
INSERT INTO
    permissions (
        id,
        created_at,
        updated_at,
        status,
        name,
        description
    )
VALUES
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'role.view',
        'View roles'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'role.create',
        'Create roles'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'role.update',
        'Update roles'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'role.delete',
        'Delete roles'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'role.restore',
        'Restore roles'
    ) ON CONFLICT (name) DO NOTHING;

-- Permission Permissions
INSERT INTO
    permissions (
        id,
        created_at,
        updated_at,
        status,
        name,
        description
    )
VALUES
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'permission.view',
        'View permissions'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'permission.create',
        'Create permissions'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'permission.update',
        'Update permissions'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'permission.delete',
        'Delete permissions'
    ),
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'permission.restore',
        'Restore permissions'
    ) ON CONFLICT (name) DO NOTHING;

-- Assign all permissions to 'admin' role
INSERT INTO
    role_permission (role_id, permission_id)
SELECT
    r.id,
    p.id
FROM
    roles r,
    permissions p
WHERE
    r.name = 'admin' ON CONFLICT DO NOTHING;

-- Insert Default Accounts
-- 1. Admin Account
INSERT INTO
    users (
        id,
        created_at,
        updated_at,
        status,
        name,
        email,
        password,
        phone
    )
VALUES
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'Admin',
        'admin@example.com',
        crypt ('Admin@123', gen_salt ('bf')),
        '0123456789'
    ) ON CONFLICT (email) DO NOTHING;

-- Assign 'admin' role to Admin User
INSERT INTO
    user_role (user_id, role_id)
SELECT
    u.id,
    r.id
FROM
    users u,
    roles r
WHERE
    u.email = 'admin@example.com'
    AND r.name = 'admin' ON CONFLICT DO NOTHING;

-- 2. User Account
INSERT INTO
    users (
        id,
        created_at,
        updated_at,
        status,
        name,
        email,
        password,
        phone
    )
VALUES
    (
        gen_random_uuid (),
        NOW (),
        NOW (),
        1,
        'User',
        'user@example.com',
        crypt ('User@123', gen_salt ('bf')),
        '0987654321'
    ) ON CONFLICT (email) DO NOTHING;

-- Assign 'user' role to User Account
INSERT INTO
    user_role (user_id, role_id)
SELECT
    u.id,
    r.id
FROM
    users u,
    roles r
WHERE
    u.email = 'user@example.com'
    AND r.name = 'user' ON CONFLICT DO NOTHING;
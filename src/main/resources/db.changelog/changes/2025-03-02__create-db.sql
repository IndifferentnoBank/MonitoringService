--liquibase formatted sql

--changeset AI:create-table-users
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password TEXT NOT NULL, -- в зашифрованном виде (BCrypt)
    created_at TIMESTAMP DEFAULT now()
);

--changeset AI:create-table-deleted_tokens
CREATE TABLE IF NOT EXISTS deleted_tokens (
    id VARCHAR(256) PRIMARY KEY
);

--changeset AI:create-table-user_role
CREATE TABLE IF NOT EXISTS user_role (
    id UUID PRIMARY KEY,
    user_id UUID,
    role VARCHAR(255)
);
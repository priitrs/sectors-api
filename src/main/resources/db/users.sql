--liquibase formatted sql

--changeset priitrs:users
CREATE TABLE IF NOT EXISTS users
(
    id            UUID         DEFAULT gen_random_uuid() PRIMARY KEY,
    username      VARCHAR(100) NOT NULL,
    first_name    VARCHAR(50)  NOT NULL,
    last_name     VARCHAR(50)  NOT NULL,
    password      VARCHAR(100) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT now()
);

--changeset priitrs:users-updated-at-trigger splitStatements:false
CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

--changeset priitrs:users-updated-at-trigger-create
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

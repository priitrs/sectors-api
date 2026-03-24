--liquibase formatted sql

--changeset priitrs:users
CREATE TABLE IF NOT EXISTS users
(
    id            UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    accept_terms  BOOLEAN NOT NULL
);

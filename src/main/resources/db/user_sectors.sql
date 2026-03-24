--liquibase formatted sql

--changeset priitrs:user-sectors
CREATE TABLE IF NOT EXISTS user_sectors
(
    id              UUID        DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id         UUID        NOT NULL REFERENCES users (id),
    sector_id       BIGINT      NOT NULL REFERENCES sectors (id),
    created_at      TIMESTAMP   NOT NULL DEFAULT now()
);

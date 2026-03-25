--liquibase formatted sql

--changeset priitrs:user-sectors
CREATE TABLE IF NOT EXISTS user_sectors
(
    id              UUID        DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id         UUID        NOT NULL REFERENCES users (id),
    sector_id       BIGINT      NOT NULL REFERENCES sectors (id),
    is_active       BOOLEAN     NOT NULL,
    created_at      TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP   NOT NULL DEFAULT now()
);

--changeset priitrs:user-sectors-updated-at-trigger splitStatements:false
CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

--changeset priitrs:user-sectors-updated-at-trigger-create
CREATE TRIGGER update_user_sectors_updated_at
    BEFORE UPDATE ON user_sectors
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
--liquibase formatted sql

--changeset priitrs:user-terms-acceptance
CREATE TABLE IF NOT EXISTS user_terms_acceptance
(
    id              UUID        DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id         UUID        NOT NULL REFERENCES users(id),
    accept_terms    BOOLEAN     NOT NULL,
    created_at      TIMESTAMP   NOT NULL DEFAULT now()
);
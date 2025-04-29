--liquibase formatted sql

--changeset AI:alter-table-deleted_tokens
ALTER TABLE deleted_tokens ALTER COLUMN id TYPE TEXT;
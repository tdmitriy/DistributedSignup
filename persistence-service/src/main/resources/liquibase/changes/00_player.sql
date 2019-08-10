--liquibase formatted sql

--changeset ps:0-0-1#1
CREATE EXTENSION "uuid-ossp";

CREATE TABLE player
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email      VARCHAR(128) NOT NULL UNIQUE,
    password   VARCHAR(64)  NOT NULL,
    sign_up_ts TIMESTAMP        DEFAULT NULL
);
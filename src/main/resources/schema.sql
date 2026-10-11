-- ========================================================
-- Table: users
-- Note: 'user' is a reserved keyword in PostgreSQL, 
-- so 'users' (or "user" with double quotes) is used.
-- ========================================================

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    mobileno VARCHAR(20),
    hashkey VARCHAR(255),
    password VARCHAR(255),
    isactive BOOLEAN NOT NULL DEFAULT TRUE,
    createby INT DEFAULT 1,
    createddatetime TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updatedatetime TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

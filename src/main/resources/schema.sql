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
    email VARCHAR(150) UNIQUE,
    hashkey VARCHAR(255),
    password VARCHAR(255),
    isactive BOOLEAN NOT NULL DEFAULT TRUE,
    createby INT DEFAULT 1,
    createddatetime TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updatedatetime TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE users ADD COLUMN IF NOT EXISTS email VARCHAR(150);
CREATE UNIQUE INDEX IF NOT EXISTS users_email_unique_idx ON users (LOWER(email)) WHERE email IS NOT NULL;

CREATE TABLE IF NOT EXISTS banners (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    subtitle TEXT,
    image TEXT NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_by INT,
    updated_by INT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS banner_bullets (
    banner_id BIGINT NOT NULL REFERENCES banners(id) ON DELETE CASCADE,
    display_order INT NOT NULL,
    bullet TEXT NOT NULL,
    PRIMARY KEY (banner_id, display_order)
);

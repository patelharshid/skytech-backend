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

CREATE TABLE IF NOT EXISTS category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    image_url TEXT
);

CREATE TABLE IF NOT EXISTS brand (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    logo_url TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_date_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    category_id BIGINT REFERENCES category(id) ON DELETE RESTRICT,
    brand_id BIGINT REFERENCES brand(id) ON DELETE RESTRICT,
    model VARCHAR(100),
    sku VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    featured_image_url TEXT NOT NULL,
    created_date_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_date_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_product_category ON product(category_id);
CREATE INDEX IF NOT EXISTS idx_product_brand ON product(brand_id);
CREATE INDEX IF NOT EXISTS idx_product_status ON product(status);
CREATE INDEX IF NOT EXISTS idx_product_sku ON product(sku);

CREATE TABLE IF NOT EXISTS product_pricing (
    product_id BIGINT PRIMARY KEY REFERENCES product(id) ON DELETE CASCADE,
    original_price NUMERIC(12, 2) NOT NULL,
    selling_price NUMERIC(12, 2) NOT NULL,
    discount_type VARCHAR(20) DEFAULT 'NONE',
    discount_value NUMERIC(12, 2) DEFAULT 0.00,
    final_price NUMERIC(12, 2) NOT NULL,
    discount_percentage NUMERIC(5, 2) DEFAULT 0.00
);

CREATE TABLE IF NOT EXISTS product_inventory (
    product_id BIGINT PRIMARY KEY REFERENCES product(id) ON DELETE CASCADE,
    quantity INT NOT NULL DEFAULT 0,
    low_stock_threshold INT NOT NULL DEFAULT 5,
    is_in_stock BOOLEAN NOT NULL DEFAULT FALSE,
    last_restocked_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS product_specification (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    spec_group VARCHAR(100) DEFAULT 'General',
    spec_key VARCHAR(100) NOT NULL,
    spec_value TEXT NOT NULL,
    display_order INT NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_prod_spec_product_id ON product_specification(product_id);

CREATE TABLE IF NOT EXISTS product_image (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE
);

-- ==========================================
-- 7. INQUIRY (Get in Touch with Us)
-- ==========================================
CREATE TABLE IF NOT EXISTS inquiry (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    email VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    message TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'NEW',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_inquiry_status ON inquiry(status);
CREATE INDEX IF NOT EXISTS idx_inquiry_created_at ON inquiry(created_at);


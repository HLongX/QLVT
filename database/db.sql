-- ============================
-- 2️⃣ BẢNG SẢN PHẨM
-- ============================
DROP TABLE IF EXISTS products CASCADE;

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(50),
    current_stock INTEGER DEFAULT 0,
    threshold INTEGER DEFAULT 0
);

-- Dữ liệu mẫu (để FE autocomplete)
INSERT INTO products (name, unit, current_stock, threshold) VALUES
('Thuốc kháng sinh Amoxicillin', 'Hộp', 30, 100),
('Găng tay y tế', 'Thùng', 5, 50),
('Khẩu trang N95', 'Cái', 200, 300),
('Nước sát khuẩn', 'Chai', 10, 80),
('Bông gạc y tế', 'Bịch', 15, 60);

-- ============================
-- 3️⃣ BẢNG PHIẾU ĐỀ NGHỊ
-- ============================
DROP TABLE IF EXISTS replenishment_requests CASCADE;

CREATE TABLE replenishment_requests (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    note VARCHAR(500),
    requester_id INTEGER,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================
-- 4️⃣ BẢNG CHI TIẾT PHIẾU
-- ============================
DROP TABLE IF EXISTS replenishment_request_items CASCADE;

CREATE TABLE replenishment_request_items (
    id SERIAL PRIMARY KEY,
    request_id INTEGER NOT NULL,
    product_id INTEGER,
    product_name VARCHAR(255),
    quantity INTEGER,
    unit VARCHAR(50),
    source VARCHAR(100),
    CONSTRAINT fk_request FOREIGN KEY (request_id)
        REFERENCES replenishment_requests (id)
        ON DELETE CASCADE
);

-- ============================
-- 5️⃣ KIỂM TRA KẾT QUẢ
-- ============================
SELECT * FROM products;

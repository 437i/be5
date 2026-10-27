CREATE TABLE address (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         country VARCHAR(100) NOT NULL,
                         city VARCHAR(100) NOT NULL,
                         street VARCHAR(255) NOT NULL
);

CREATE TABLE images (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        image BYTEA NOT NULL
);

CREATE TABLE supplier (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          name VARCHAR(255) NOT NULL,
                          address_id UUID REFERENCES address(id) ON DELETE RESTRICT,
                          phone_number VARCHAR(20)
);

CREATE TABLE product (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         name VARCHAR(255) NOT NULL,
                         category VARCHAR(100) NOT NULL,
                         price NUMERIC(12, 2) NOT NULL CHECK (price >= 0),
                         available_stock INTEGER NOT NULL DEFAULT 0 CHECK (available_stock >= 0),
                         last_update_date DATE,
                         supplier_id UUID REFERENCES supplier(id) ON DELETE SET NULL,
                         image_id UUID REFERENCES images(id) ON DELETE SET NULL
);

CREATE TABLE client (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        client_name VARCHAR(100) NOT NULL,
                        client_surname VARCHAR(100) NOT NULL,
                        birthday DATE NOT NULL,
                        gender VARCHAR(1) NOT NULL CHECK (gender IN ('M', 'F')),
                        registration_date DATE NOT NULL DEFAULT CURRENT_DATE,
                        address_id UUID REFERENCES address(id) ON DELETE RESTRICT
);

CREATE INDEX idx_client_name_surname ON client (client_name, client_surname);
CREATE INDEX idx_product_category ON product (category);
CREATE INDEX idx_product_supplier ON product (supplier_id);
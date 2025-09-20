-- ==========================================
-- DATABASE: applications_db
-- ==========================================

-- ==========================================
-- CREACIÓN DE TABLA LOAN STATUS
-- ==========================================
CREATE TABLE IF NOT EXISTS loan_status (
    id_status SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- ==========================================
-- CREACIÓN DE TABLA LOAN TYPE
-- ==========================================
CREATE TABLE IF NOT EXISTS loan_type (
    id_loan_type SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    min_amount DECIMAL(12,2) NOT NULL,
    max_amount DECIMAL(12,2) NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL,
    automatic_validation BOOLEAN DEFAULT false
);

-- ==========================================
-- CREACIÓN DE TABLA LOAN APPLICATION
-- ==========================================
CREATE TABLE IF NOT EXISTS loan_application (
    id_application SERIAL PRIMARY KEY,
    amount DECIMAL(12,2) NOT NULL,
    term INT NOT NULL,
    interest_rate DECIMAL(12,2) NOT NULL,
    email VARCHAR(150) NOT NULL,
    id_status INT NOT NULL,
    id_loan_type INT NOT NULL,
    CONSTRAINT fk_status FOREIGN KEY (id_status) REFERENCES loan_status (id_status),
    CONSTRAINT fk_loan_type FOREIGN KEY (id_loan_type) REFERENCES loan_type (id_loan_type)
);

-- ==========================================
-- ÍNDICES ADICIONALES
-- ==========================================
CREATE INDEX IF NOT EXISTS idx_loan_application_email ON loan_application (email);
CREATE INDEX IF NOT EXISTS idx_loan_application_status ON loan_application (id_status);
CREATE INDEX IF NOT EXISTS idx_loan_application_type ON loan_application (id_loan_type);
CREATE INDEX IF NOT EXISTS idx_loan_type_id ON loan_type (id_loan_type);
CREATE INDEX IF NOT EXISTS idx_loan_status_id ON loan_status (id_status);

-- ==========================================
-- INSERTS INICIALES
-- ==========================================
INSERT INTO loan_status (name, description) VALUES 
('PENDING', 'Application is pending review'),
('APPROVED', 'Application has been approved'),
('REJECTED', 'Application has been rejected'),
('UNDER_REVIEW', 'Application is under review'),
('CANCEL', 'Application is cancel')
ON CONFLICT (name) DO NOTHING;

INSERT INTO loan_type (name, min_amount, max_amount, interest_rate, automatic_validation) VALUES 
('PERSONAL', 1000.00, 50000.00, 12, true),
('MORTGAGE', 50000.00, 500000.00, 8, false),
('CAR', 5000.00, 100000.00, 10, false),
('EDUCATION', 2000.00, 30000.00, 6, false)
ON CONFLICT (name) DO NOTHING;

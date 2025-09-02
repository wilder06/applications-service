-- Table: loan_status
CREATE TABLE loan_status (
                             id_status SERIAL PRIMARY KEY,
                             name VARCHAR(100) NOT NULL,
                             description VARCHAR(255)
);

-- Table: loan_type
CREATE TABLE loan_type (
                           id_loan_type SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           min_amount DECIMAL(12,2) NOT NULL,
                           max_amount DECIMAL(12,2) NOT NULL,
                           interest_rate DECIMAL(5,2) NOT NULL,
                           automatic_validation BOOLEAN DEFAULT false
);

-- Table: loan_request
CREATE TABLE loan_application (
                              id_application SERIAL PRIMARY KEY,
                              amount DECIMAL(12,2) NOT NULL,
                              term INT NOT NULL,
                              email VARCHAR(150) NOT NULL,
                              id_status INT NOT NULL,
                              id_loan_type INT NOT NULL,
                              CONSTRAINT fk_status FOREIGN KEY (id_status) REFERENCES loan_status (id_status),
                              CONSTRAINT fk_loan_type FOREIGN KEY (id_loan_type) REFERENCES loan_type (id_loan_type)
);


estados → loan_status

tipo_prestamo → loan_type

solicitud → loan_request

id_estado → id_status

id_tipo_prestamo → id_loan_type

monto → amount

plazo → term

nombre → name

descripcion → description

monto_minimo → min_amount

monto_maximo → max_amount

tasa_interes → interest_rate

validacion_automatica → automatic_validation

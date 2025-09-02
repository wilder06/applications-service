-- ======================================
-- Datos iniciales para tabla loan_status
-- ======================================
INSERT INTO loan_status (name, description) VALUES
                                                ('PENDING', 'Pendiente de revisión'),
                                                ('APPROVED', 'La solicitud fue aprobada'),
                                                ('REJECTED', 'La solicitud fue rechazada'),
                                                ('CANCELLED', 'El cliente canceló la solicitud'),
                                                ('DISBURSED', 'El préstamo ha sido desembolsado');

-- ======================================
-- Datos iniciales para tabla loan_type
-- ======================================
INSERT INTO loan_type (name, min_amount, max_amount, interest_rate, automatic_validation) VALUES
                                                                                              ('Personal', 500.00, 20000.00, 12.50, true),
                                                                                              ('Hipotecario', 10000.00, 500000.00, 7.25, false),
                                                                                              ('Vehicular', 5000.00, 100000.00, 9.50, true),
                                                                                              ('Educativo', 1000.00, 50000.00, 5.75, false),
                                                                                              ('Consumo Rápido', 100.00, 5000.00, 15.00, true);
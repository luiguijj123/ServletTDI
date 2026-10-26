CREATE TABLE IF NOT EXISTS creditos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fecha DATE NOT NULL,
    moneda CHAR(3) NOT NULL CHECK (moneda IN ('PEN', 'USD')),
    monto DECIMAL(15,2) NOT NULL CHECK (monto >= 0),
    periodo INT NOT NULL CHECK (periodo > 0),
    cuota DECIMAL(15,2) NOT NULL CHECK (cuota >= 0),
    tea DECIMAL(15,2) NOT NULL CHECK (tea >= 0),
    vencimiento DATE NOT NULL
);

-- Tabla independiente: no contiene credito_id ni llaves foráneas.
CREATE TABLE IF NOT EXISTS pagos_tarjeta (
    id BIGINT PRIMARY KEY,
    fecha DATE NOT NULL,
    mes VARCHAR(20) NOT NULL,
    tarjeta VARCHAR(30) NOT NULL,
    transaccion VARCHAR(120) NOT NULL,
    gastos DECIMAL(15,2) NULL CHECK (gastos >= 0),
    pago DECIMAL(15,2) NULL CHECK (pago >= 0)
);
INSERT INTO pagos_tarjeta (id, fecha, mes, tarjeta, transaccion, gastos, pago)
SELECT 1, '2021-01-01', 'enero', 'VISA', 'Peluquería', 1000.00, NULL
WHERE NOT EXISTS (SELECT 1 FROM pagos_tarjeta WHERE id = 1);
INSERT INTO pagos_tarjeta (id, fecha, mes, tarjeta, transaccion, gastos, pago)
SELECT 2, '2021-01-02', 'enero', 'VISA', 'TV plasma (1 de 6)', 2500.00, NULL
WHERE NOT EXISTS (SELECT 1 FROM pagos_tarjeta WHERE id = 2);
INSERT INTO pagos_tarjeta (id, fecha, mes, tarjeta, transaccion, gastos, pago)
SELECT 3, '2021-01-03', 'enero', 'MASTERCARD', 'Vajilla Kit', 2500.00, NULL
WHERE NOT EXISTS (SELECT 1 FROM pagos_tarjeta WHERE id = 3);
INSERT INTO pagos_tarjeta (id, fecha, mes, tarjeta, transaccion, gastos, pago)
SELECT 4, '2021-01-04', 'enero', 'MASTERCARD', 'YouTube Premium', 170.00, NULL
WHERE NOT EXISTS (SELECT 1 FROM pagos_tarjeta WHERE id = 4);
INSERT INTO pagos_tarjeta (id, fecha, mes, tarjeta, transaccion, gastos, pago)
SELECT 5, '2021-01-05', 'enero', 'VISA', 'Pasaje Londres', 25000.00, NULL
WHERE NOT EXISTS (SELECT 1 FROM pagos_tarjeta WHERE id = 5);
INSERT INTO pagos_tarjeta (id, fecha, mes, tarjeta, transaccion, gastos, pago)
SELECT 6, '2021-01-06', 'enero', 'MASTERCARD', 'Pago Mastercard', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM pagos_tarjeta WHERE id = 6);
INSERT INTO pagos_tarjeta (id, fecha, mes, tarjeta, transaccion, gastos, pago)
SELECT 7, '2021-02-07', 'febrero', 'VISA', 'Teatro', 150.00, NULL
WHERE NOT EXISTS (SELECT 1 FROM pagos_tarjeta WHERE id = 7);
INSERT INTO pagos_tarjeta (id, fecha, mes, tarjeta, transaccion, gastos, pago)
SELECT 8, '2021-02-08', 'febrero', 'VISA', 'Pago VISA', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM pagos_tarjeta WHERE id = 8);
INSERT INTO pagos_tarjeta (id, fecha, mes, tarjeta, transaccion, gastos, pago)
SELECT 9, '2021-02-09', 'febrero', 'MASTERCARD', 'Cine', 23.00, NULL
WHERE NOT EXISTS (SELECT 1 FROM pagos_tarjeta WHERE id = 9);
INSERT INTO pagos_tarjeta (id, fecha, mes, tarjeta, transaccion, gastos, pago)
SELECT 10, '2021-02-10', 'febrero', 'MASTERCARD', 'Libros x 5', 1400.00, NULL
WHERE NOT EXISTS (SELECT 1 FROM pagos_tarjeta WHERE id = 10);

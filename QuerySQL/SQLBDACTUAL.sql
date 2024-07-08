-- Crear la base de datos si no existe
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ProyectoBaseDatos')
BEGIN
    CREATE DATABASE ProyectoBaseDatos;
END
GO

-- Usar la base de datos
USE ProyectoBaseDatos;
GO

CREATE TABLE Area (
    idArea INT NOT NULL PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL
);

CREATE TABLE Caja (
    idCaja INT NOT NULL PRIMARY KEY,
    Area_idArea INT NOT NULL,
    monto FLOAT NOT NULL,
    topeMovimiento FLOAT NOT NULL,
    CONSTRAINT FK_Caja_Area FOREIGN KEY (Area_idArea) REFERENCES Area(idArea)
);

CREATE TABLE Cajero (
    idCajero INT NOT NULL PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL,
    apellido VARCHAR(45) NOT NULL,
    email VARCHAR(45) NOT NULL,
    telefono VARCHAR(45) NOT NULL
);

CREATE TABLE EmpresaReceptora (
    idEmpresaReceptora INT NOT NULL PRIMARY KEY,
    RUC VARCHAR(45) NOT NULL,
    nombre VARCHAR(45) NOT NULL,
    tipo VARCHAR(45) NOT NULL
);

CREATE TABLE Estado (
    idEstado INT NOT NULL PRIMARY KEY,
    reporteEstado VARCHAR(45) NOT NULL
);

CREATE TABLE Movimiento (
    idMovimiento INT NOT NULL PRIMARY KEY,
    tipoMovimiento VARCHAR(45) NOT NULL
);

CREATE TABLE Documento (
    idDocumento INT NOT NULL PRIMARY KEY,
    Caja_idCaja INT NOT NULL,
    Cajero_idCajero INT NOT NULL,
    EmpresaReceptora_idEmpresaReceptora INT NOT NULL,
    Movimiento_idMotivo INT NOT NULL,
    tipoDocumento VARCHAR(45) NOT NULL,
    descripcion VARCHAR(45) NOT NULL,
    monto FLOAT NOT NULL,
    CONSTRAINT FK_Documento_Caja FOREIGN KEY (Caja_idCaja) REFERENCES Caja(idCaja),
    CONSTRAINT FK_Documento_Cajero FOREIGN KEY (Cajero_idCajero) REFERENCES Cajero(idCajero),
    CONSTRAINT FK_Documento_EmpresaReceptora FOREIGN KEY (EmpresaReceptora_idEmpresaReceptora) REFERENCES EmpresaReceptora(idEmpresaReceptora),
    CONSTRAINT FK_Documento_Movimiento FOREIGN KEY (Movimiento_idMotivo) REFERENCES Movimiento(idMovimiento)
);

CREATE TABLE Transaccion (
    idTransaccion INT NOT NULL PRIMARY KEY,
    Documento_idDocumento INT NOT NULL,
    Estado_idEstado INT NOT NULL,
    fechaTransaccion DATE NOT NULL,
    tipoTransaccion VARCHAR(45) NOT NULL,
    descripcion VARCHAR(255),
    CONSTRAINT FK_Transaccion_Documento FOREIGN KEY (Documento_idDocumento) REFERENCES Documento(idDocumento),
    CONSTRAINT FK_Transaccion_Estado FOREIGN KEY (Estado_idEstado) REFERENCES Estado(idEstado)
);

-- Populate Area table
INSERT INTO Area (idArea, nombre)
VALUES
    (1, 'Administración'),
    (2, 'Recursos Humanos'),
    (3, 'Finanzas'),
    (4, 'Ventas'),
    (5, 'Producción'),
    (6, 'Servicio al Cliente'),
    (7, 'Desarrollo de Producto'),
    (8, 'Investigación y Desarrollo'),
    (9, 'Marketing'),
    (10, 'Logística');

-- Populate Caja table
INSERT INTO Caja (idCaja, Area_idArea, monto, topeMovimiento)
VALUES
    (1, 1, 10000.00, 5000.00),
    (2, 2, 15000.00, 6000.00),
    (3, 3, 8000.00, 4000.00),
    (4, 4, 20000.00, 10000.00),
    (5, 5, 12000.00, 7000.00),
    (6, 6, 30000.00, 15000.00),
    (7, 7, 6000.00, 3000.00),
    (8, 8, 25000.00, 12000.00),
    (9, 9, 18000.00, 9000.00),
    (10, 10, 5000.00, 2500.00);

-- Populate Cajero table
INSERT INTO Cajero (idCajero, nombre, apellido, email, telefono)
VALUES
    (1, 'Juan', 'Gómez', 'juangomez@example.com', '123-456-7890'),
    (2, 'María', 'López', 'marialopez@example.com', '987-654-3210'),
    (3, 'Carlos', 'Martínez', 'carlosmartinez@example.com', '555-123-4567'),
    (4, 'Ana', 'Rodríguez', 'anarodriguez@example.com', '789-321-6540'),
    (5, 'Pedro', 'Sánchez', 'pedrosanchez@example.com', '456-789-0123'),
    (6, 'Laura', 'García', 'lauragarcia@example.com', '321-654-9870'),
    (7, 'Diego', 'Pérez', 'diegoperez@example.com', '666-999-8888'),
    (8, 'Sofía', 'Díaz', 'sofiadiaz@example.com', '222-333-4444'),
    (9, 'Javier', 'Fernández', 'javierfernandez@example.com', '777-888-9999'),
    (10, 'Elena', 'Vargas', 'elenavargas@example.com', '111-222-3333');

-- Populate EmpresaReceptora table with well-known companies
INSERT INTO EmpresaReceptora (idEmpresaReceptora, RUC, nombre, tipo)
VALUES
    (1, '12345678901', 'Apple Inc.', 'Tecnología'),
    (2, '23456789012', 'Microsoft Corporation', 'Tecnología'),
    (3, '34567890123', 'Amazon.com Inc.', 'Comercio electrónico'),
    (4, '45678901234', 'Alphabet Inc. (Google)', 'Tecnología'),
    (5, '56789012345', 'Tesla, Inc.', 'Automotriz'),
    (6, '67890123456', 'Facebook, Inc.', 'Redes sociales'),
    (7, '78901234567', 'Samsung Electronics Co., Ltd.', 'Electrónica'),
    (8, '89012345678', 'Toyota Motor Corporation', 'Automotriz'),
    (9, '90123456789', 'Walmart Inc.', 'Retail'),
    (10, '01234567890', 'Johnson & Johnson', 'Farmacéutica');

-- Populate Estado table
INSERT INTO Estado (idEstado, reporteEstado)
VALUES
    (1, 'Pendiente'),
    (2, 'En proceso'),
    (3, 'Aprobado'),
    (4, 'Rechazado'),
    (5, 'Cancelado'),
    (6, 'En revisión'),
    (7, 'Finalizado'),
    (8, 'Suspendido'),
    (9, 'Archivado'),
    (10, 'En espera');

-- Populate Movimiento table
INSERT INTO Movimiento (idMovimiento, tipoMovimiento)
VALUES
    (1, 'Depósito'),
    (2, 'Retiro'),
    (3, 'Transferencia'),
    (4, 'Pago'),
    (5, 'Compra'),
    (6, 'Venta'),
    (7, 'Devolución'),
    (8, 'Ajuste'),
    (9, 'Reversión'),
    (10, 'Reembolso');


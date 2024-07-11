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
    idArea INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL
);

CREATE TABLE Caja (
    idCaja INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Area_idArea INT NOT NULL,
    monto FLOAT NOT NULL,
    topeMovimiento FLOAT NOT NULL,
    CONSTRAINT FK_Caja_Area FOREIGN KEY (Area_idArea) REFERENCES Area(idArea)
);

CREATE TABLE Cajero (
    idCajero INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL,
    apellido VARCHAR(45) NOT NULL,
    email VARCHAR(45) NOT NULL,
    telefono VARCHAR(45) NOT NULL
);

CREATE TABLE EmpresaReceptora (
    RUC VARCHAR(45) NOT NULL PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL,
    tipo VARCHAR(45) NOT NULL
);

CREATE TABLE Estado (
    idEstado INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    reporteEstado VARCHAR(45) NOT NULL
);

CREATE TABLE Movimiento (
    idMovimiento INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    tipoMovimiento VARCHAR(45) NOT NULL
);

CREATE TABLE Documento (
    idDocumento INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Caja_idCaja INT NOT NULL,
    Cajero_idCajero INT NOT NULL,
    EmpresaReceptora_RUC VARCHAR(45) NOT NULL,
    Movimiento_idMovimiento INT NOT NULL,
    descripcion VARCHAR(45) NOT NULL,
    monto FLOAT NOT NULL,
    CONSTRAINT FK_Documento_Caja FOREIGN KEY (Caja_idCaja) REFERENCES Caja(idCaja),
    CONSTRAINT FK_Documento_Cajero FOREIGN KEY (Cajero_idCajero) REFERENCES Cajero(idCajero),
    CONSTRAINT FK_Documento_EmpresaReceptora FOREIGN KEY (EmpresaReceptora_RUC) REFERENCES EmpresaReceptora(RUC),
    CONSTRAINT FK_Documento_Movimiento FOREIGN KEY (Movimiento_idMovimiento) REFERENCES Movimiento(idMovimiento)
);

CREATE TABLE Transaccion (
    idTransaccion INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Documento_idDocumento INT NOT NULL,
    Estado_idEstado INT NOT NULL,
    fechaTransaccion DATE NOT NULL,
    tipoTransaccion VARCHAR(45) NOT NULL,
    descripcion VARCHAR(255),
    CONSTRAINT FK_Transaccion_Documento FOREIGN KEY (Documento_idDocumento) REFERENCES Documento(idDocumento),
    CONSTRAINT FK_Transaccion_Estado FOREIGN KEY (Estado_idEstado) REFERENCES Estado(idEstado)
);

-- Inserci�n de datos
INSERT INTO Area (nombre)
VALUES
    ('Administraci�n'),
    ('Recursos Humanos'),
    ('Finanzas'),
    ('Ventas'),
    ('Producci�n'),
    ('Servicio al Cliente'),
    ('Desarrollo de Producto'),
    ('Investigaci�n y Desarrollo'),
    ('Marketing'),
    ('Log�stica'),
    ('IT'),
    ('Compras'),
    ('Legales'),
    ('Calidad'),
    ('Almac�n');

INSERT INTO Caja (Area_idArea, monto, topeMovimiento)
VALUES
    (1, 10000.00, 5000.00),
    (2, 15000.00, 6000.00),
    (3, 8000.00, 4000.00),
    (4, 20000.00, 10000.00),
    (5, 12000.00, 7000.00),
    (6, 30000.00, 15000.00),
    (7, 6000.00, 3000.00),
    (8, 25000.00, 12000.00),
    (9, 18000.00, 9000.00),
    (10, 5000.00, 2500.00),
    (11, 7000.00, 3500.00),
    (12, 16000.00, 8000.00),
    (13, 9000.00, 4500.00),
    (14, 22000.00, 11000.00),
    (15, 13000.00, 6500.00);

INSERT INTO Cajero (nombre, apellido, email, telefono)
VALUES
    ('Juan', 'G�mez', 'juangomez@example.com', '123-456-7890'),
    ('Mar�a', 'L�pez', 'marialopez@example.com', '987-654-3210'),
    ('Carlos', 'Mart�nez', 'carlosmartinez@example.com', '555-123-4567'),
    ('Ana', 'Rodr�guez', 'anarodriguez@example.com', '789-321-6540'),
    ('Pedro', 'S�nchez', 'pedrosanchez@example.com', '456-789-0123'),
    ('Laura', 'Garc�a', 'lauragarcia@example.com', '321-654-9870'),
    ('Diego', 'P�rez', 'diegoperez@example.com', '666-999-8888'),
    ('Sof�a', 'D�az', 'sofiadiaz@example.com', '222-333-4444'),
    ('Javier', 'Fern�ndez', 'javierfernandez@example.com', '777-888-9999'),
    ('Elena', 'Vargas', 'elenavargas@example.com', '111-222-3333'),
    ('Luis', 'Hern�ndez', 'luishernandez@example.com', '444-555-6666'),
    ('Marta', 'Ramos', 'martaramos@example.com', '555-666-7777'),
    ('Antonio', 'Ruiz', 'antonioruiz@example.com', '666-777-8888'),
    ('Claudia', 'Mendoza', 'claudiamendoza@example.com', '777-888-9990'),
    ('Miguel', 'Castro', 'miguelcastro@example.com', '888-999-0001');

INSERT INTO EmpresaReceptora (RUC, nombre, tipo)
VALUES
    ('12345678901', 'Apple Inc.', 'Tecnolog�a'),
    ('23456789012', 'Microsoft Corporation', 'Tecnolog�a'),
    ('34567890123', 'Amazon.com Inc.', 'Comercio electr�nico'),
    ('45678901234', 'Alphabet Inc. (Google)', 'Tecnolog�a'),
    ('56789012345', 'Tesla, Inc.', 'Automotriz'),
    ('67890123456', 'Facebook, Inc.', 'Redes sociales'),
    ('78901234567', 'Samsung Electronics Co., Ltd.', 'Electr�nica'),
    ('89012345678', 'Toyota Motor Corporation', 'Automotriz'),
    ('90123456789', 'Walmart Inc.', 'Retail'),
    ('01234567890', 'Johnson & Johnson', 'Farmac�utica'),
    ('11223344556', 'IBM', 'Tecnolog�a'),
    ('22334455667', 'Sony', 'Electr�nica'),
    ('33445566778', 'Intel', 'Tecnolog�a'),
    ('44556677889', 'HP', 'Tecnolog�a'),
    ('55667788990', 'Cisco', 'Redes');

INSERT INTO Estado (reporteEstado)
VALUES
    ('Pendiente'),
    ('En proceso'),
    ('Aprobado'),
    ('Rechazado'),
    ('Cancelado'),
    ('En revisi�n'),
    ('Finalizado'),
    ('Suspendido'),
    ('Archivado'),
    ('En espera'),
    ('Anulado'),
    ('Reprogramado'),
    ('Parcialmente aprobado'),
    ('En evaluaci�n'),
    ('Completado');

INSERT INTO Movimiento (tipoMovimiento) 
VALUES 
('Boleta'),
('Factura'),
('Recibo'),
('Nota de cr�dito'),
('Cheque'),
('Transferencia'),
('Dep�sito'),
('Retiro'),
('Abono'),
('Cargo'),
('Comprobante'),
('Voucher');

INSERT INTO Documento (Caja_idCaja, Cajero_idCajero, EmpresaReceptora_RUC, Movimiento_idMovimiento,  descripcion, monto)
VALUES
    (1, 1, '12345678901', 1, 'Pago por servicios', 1500.00),
    (2, 2, '23456789012', 2, 'Compra de materiales', 2000.00),
    (3, 3, '34567890123', 3,'Devoluci�n de productos', 500.00),
    (4, 4, '45678901234', 4, 'Compra de equipos', 3000.00),
    (5, 5, '56789012345', 5, 'Transferencia de fondos', 2500.00),
    (6, 6, '67890123456', 6, 'Pago de servicios', 3500.00),
    (7, 7, '78901234567', 7, 'Compra de suministros', 4500.00),
    (8, 8, '89012345678', 8, 'Devoluci�n de fondos', 5500.00),
    (9, 9, '90123456789', 9, 'Pago de deuda', 6500.00),
    (10, 10, '01234567890', 10, 'Transferencia interbancaria', 7500.00),
    (11, 11, '11223344556', 11, 'Pago por servicios', 1500.00),
    (12, 12, '22334455667', 12, 'Compra de materiales', 2000.00),
    (13, 13, '33445566778', 10, 'Devoluci�n de productos', 500.00),
    (14, 14, '44556677889', 5, 'Compra de equipos', 3000.00),
    (15, 15, '55667788990', 7, 'Transferencia de fondos', 2500.00);

INSERT INTO Transaccion (Documento_idDocumento, Estado_idEstado, fechaTransaccion, tipoTransaccion, descripcion)
VALUES
    (1, 1, '2024-07-01', 'Dep�sito', 'Dep�sito de efectivo'),
    (2, 2, '2024-07-02', 'Retiro', 'Retiro de fondos'),
    (3, 3, '2024-07-03', 'Pago', 'Pago de factura'),
    (4, 4, '2024-07-04', 'Compra', 'Compra de suministros'),
    (5, 5, '2024-07-05', 'Venta', 'Venta de productos'),
    (6, 6, '2024-07-06', 'Dep�sito', 'Dep�sito en cuenta'),
    (7, 7, '2024-07-07', 'Retiro', 'Retiro de efectivo'),
    (8, 8, '2024-07-08', 'Pago', 'Pago de servicios'),
    (9, 9, '2024-07-09', 'Compra', 'Compra de activos'),
    (10, 10, '2024-07-10', 'Venta', 'Venta de bienes'),
    (11, 1, '2024-07-01', 'Dep�sito', 'Dep�sito de efectivo'),
    (12, 2, '2024-07-02', 'Retiro', 'Retiro de fondos'),
    (13, 3, '2024-07-03', 'Pago', 'Pago de factura'),
    (14, 4, '2024-07-04', 'Compra', 'Compra de suministros'),
    (15, 5, '2024-07-05', 'Venta', 'Venta de productos');

-- Otorgar todos los permisos requeridos al usuario 'usuario'
GRANT ALTER, CONTROL, DELETE, INSERT, REFERENCES, SELECT, TAKE OWNERSHIP, UNMASK, UPDATE, VIEW CHANGE TRACKING, VIEW DEFINITION
ON dbo.Area TO usuario;

GRANT ALTER, CONTROL, DELETE, INSERT, REFERENCES, SELECT, TAKE OWNERSHIP, UNMASK, UPDATE, VIEW CHANGE TRACKING, VIEW DEFINITION
ON dbo.Caja TO usuario;

GRANT ALTER, CONTROL, DELETE, INSERT, REFERENCES, SELECT, TAKE OWNERSHIP, UNMASK, UPDATE, VIEW CHANGE TRACKING, VIEW DEFINITION
ON dbo.Cajero TO usuario;

GRANT ALTER, CONTROL, DELETE, INSERT, REFERENCES, SELECT, TAKE OWNERSHIP, UNMASK, UPDATE, VIEW CHANGE TRACKING, VIEW DEFINITION
ON dbo.EmpresaReceptora TO usuario;

GRANT ALTER, CONTROL, DELETE, INSERT, REFERENCES, SELECT, TAKE OWNERSHIP, UNMASK, UPDATE, VIEW CHANGE TRACKING, VIEW DEFINITION
ON dbo.Estado TO usuario;

GRANT ALTER, CONTROL, DELETE, INSERT, REFERENCES, SELECT, TAKE OWNERSHIP, UNMASK, UPDATE, VIEW CHANGE TRACKING, VIEW DEFINITION
ON dbo.Movimiento TO usuario;

GRANT ALTER, CONTROL, DELETE, INSERT, REFERENCES, SELECT, TAKE OWNERSHIP, UNMASK, UPDATE, VIEW CHANGE TRACKING, VIEW DEFINITION
ON dbo.Documento TO usuario;

GRANT ALTER, CONTROL, DELETE, INSERT, REFERENCES, SELECT, TAKE OWNERSHIP, UNMASK, UPDATE, VIEW CHANGE TRACKING, VIEW DEFINITION
ON dbo.Transaccion TO usuario;

-- Otorgar permisos en el esquema dbo
GRANT ALTER, CONTROL, DELETE, INSERT, REFERENCES, SELECT, TAKE OWNERSHIP, UNMASK, UPDATE, VIEW CHANGE TRACKING, VIEW DEFINITION
ON SCHEMA::dbo TO usuario;
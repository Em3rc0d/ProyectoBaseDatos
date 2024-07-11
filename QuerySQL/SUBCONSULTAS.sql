-- Obtener total de montos movidos por cada cajero en una fecha espec�fica
SELECT 
    Cajero.nombre,
    SUM(Documento.monto) AS TotalMontoMovido
FROM Documento
JOIN Cajero ON Documento.Cajero_idCajero = Cajero.idCajero
JOIN Transaccion ON Documento.idDocumento = Transaccion.Documento_idDocumento
WHERE Transaccion.fechaTransaccion = '2024-07-10' -- Reemplazar con la fecha deseada
GROUP BY Cajero.nombre;

-- Obetener el n�mero de documentos pendientes por cada �rea
SELECT 
    Area.nombre,
    COUNT(Documento.idDocumento) AS DocumentosPendientes
FROM Area
LEFT JOIN Caja ON Area.idArea = Caja.Area_idArea
LEFT JOIN Documento ON Caja.idCaja = Documento.Caja_idCaja
LEFT JOIN Transaccion ON Documento.idDocumento = Transaccion.Documento_idDocumento
LEFT JOIN Estado ON Transaccion.Estado_idEstado = Estado.idEstado
WHERE Estado.reporteEstado = 'Pendiente'
GROUP BY Area.nombre;

-- Obtener el cajero con el mayor monto movido por �rea
SELECT 
    Area.nombre AS Area,
    Cajero.nombre AS CajeroNombre,
    MAX(Documento.monto) AS MaxMontoMovido
FROM Area
JOIN Caja ON Area.idArea = Caja.Area_idArea
JOIN Documento ON Caja.idCaja = Documento.Caja_idCaja
JOIN Cajero ON Documento.Cajero_idCajero = Cajero.idCajero
GROUP BY Area.nombre, Cajero.nombre;

-- Obtener el monto promedio movido por cada caja:
SELECT 
    Caja.idCaja,
    AVG(Documento.monto) AS PromedioMontoMovido
FROM Caja
LEFT JOIN Documento ON Caja.idCaja = Documento.Caja_idCaja
GROUP BY Caja.idCaja;

-- Obtener los documentos cuyo monto es mayor a 1000
SELECT 
    Documento.descripcion,
    Documento.monto
FROM Documento
WHERE Documento.monto > 1000
ORDER BY Documento.monto DESC;

-- Subconsulta correlacionada 1: Obtener el cajero con el mayor monto movido por cada �rea
SELECT 
    Area.nombre,
    (SELECT TOP 1 Cajero.nombre
     FROM Documento
     JOIN Cajero ON Documento.Cajero_idCajero = Cajero.idCajero
     JOIN Caja ON Documento.Caja_idCaja = Caja.idCaja
     WHERE Caja.Area_idArea = Area.idArea
     ORDER BY Documento.monto DESC) AS CajeroConMaxMonto
FROM Area;

-- Subconsulta correlacionada 2: Obtener los cajeros cuyos montos totales movidos son mayores al promedio de los montos totales movidos por todos los cajeros
SELECT 
    Cajero.nombre,
    SUM(Documento.monto) AS TotalMontoMovido
FROM 
    Documento
JOIN 
    Cajero ON Documento.Cajero_idCajero = Cajero.idCajero
GROUP BY 
    Cajero.nombre
HAVING 
    SUM(Documento.monto) > (
        SELECT 
            AVG(SumaMontos.TotalMonto)
        FROM (
            SELECT 
                SUM(Documento.monto) AS TotalMonto
            FROM 
                Documento
            GROUP BY 
                Documento.Cajero_idCajero
        ) AS SumaMontos
    );

-- Subconsulta correlacionada 3: obtener los documentos cuyo monto sea mayor al promedio de todos los montos de documentos
SELECT 
    D.idDocumento, 
    D.monto, 
    (SELECT AVG(monto) FROM Documento) AS PromedioMonto 
FROM 
    Documento D 
WHERE 
    D.monto > (SELECT AVG(monto) FROM Documento);

-- Subconsulta correlacionada 4: Obtener los documentos cuyo monto es mayor al promedio de los montos por �rea
SELECT 
    D.idDocumento, 
    D.descripcion, 
    D.monto, 
    A.nombre AS AreaNombre,
    (SELECT AVG(D2.monto)
     FROM Documento D2
     JOIN Caja CA2 ON D2.Caja_idCaja = CA2.idCaja
     WHERE CA2.Area_idArea = CA.Area_idArea) AS PromedioMonto
FROM 
    Documento D
JOIN 
    Caja CA ON D.Caja_idCaja = CA.idCaja
JOIN 
    Area A ON CA.Area_idArea = A.idArea
WHERE 
    D.monto > (SELECT AVG(D2.monto)
               FROM Documento D2
               JOIN Caja CA2 ON D2.Caja_idCaja = CA2.idCaja
               WHERE CA2.Area_idArea = CA.Area_idArea);


-- FUNCIONES
-- Funcion para hallar el monto total movido por un cajero
CREATE FUNCTION dbo.fn_TotalMontoPorCajero (@idCajero INT)
RETURNS FLOAT
AS
BEGIN
    DECLARE @total FLOAT;
    SELECT @total = SUM(Documento.monto)
    FROM Documento
    WHERE Documento.Cajero_idCajero = @idCajero;
    RETURN @total;
END;
-- Ejemplo de uso
DECLARE @idCajero INT = 3;
DECLARE @total FLOAT;

SET @total = dbo.fn_TotalMontoPorCajero(@idCajero);
SELECT @total AS TotalMontoMovido;


-- Funci�n para Obtener el Nombre del Cajero por ID
CREATE FUNCTION dbo.fn_NombreCajero (@idCajero INT)
RETURNS VARCHAR(90)
AS
BEGIN
    DECLARE @nombre VARCHAR(90);
    SELECT @nombre = CONCAT(nombre, ' ', apellido)
    FROM Cajero
    WHERE idCajero = @idCajero;
    RETURN @nombre;
END;
-- Ejemplo de uso
DECLARE @idCajero INT = 2;
DECLARE @nombre VARCHAR(90);

SET @nombre = dbo.fn_NombreCajero(@idCajero);
SELECT @nombre AS NombreCajero;


-- PROCEDIMIENTOS
-- Procedimiento para eliminar una Caja
CREATE PROCEDURE spEliminarCaja
    @idCaja INT
AS
BEGIN
    -- Iniciar una transacci�n
    BEGIN TRANSACTION;

    -- Manejo de errores
    BEGIN TRY
        -- Eliminar las transacciones relacionadas en la tabla Transaccion
        DELETE FROM Transaccion
        WHERE Documento_idDocumento IN (
            SELECT idDocumento
            FROM Documento
            WHERE Caja_idCaja = @idCaja
        );

        -- Eliminar los documentos relacionados en la tabla Documento
        DELETE FROM Documento
        WHERE Caja_idCaja = @idCaja;

        -- Eliminar el objeto de la tabla Caja
        DELETE FROM Caja
        WHERE idCaja = @idCaja;

        -- Confirmar la transacci�n
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- En caso de error, deshacer la transacci�n
        ROLLBACK TRANSACTION;
        -- Mostrar el mensaje de error
        THROW;
    END CATCH
END;
GO
-- Ejemplo de uso
EXEC spEliminarCaja @idCaja = 14;


-- Procedimiento eliminar Empresa Receptora
CREATE PROCEDURE spEliminarEmpresaReceptora
    @RUC VARCHAR(45)
AS
BEGIN
    -- Iniciar una transacci�n
    BEGIN TRANSACTION;

    -- Manejo de errores
    BEGIN TRY
        -- Eliminar las transacciones relacionadas en la tabla Transaccion
        DELETE FROM Transaccion
        WHERE Documento_idDocumento IN (
            SELECT idDocumento
            FROM Documento
            WHERE EmpresaReceptora_RUC = @RUC
        );

        -- Eliminar los documentos relacionados en la tabla Documento
        DELETE FROM Documento
        WHERE EmpresaReceptora_RUC = @RUC;

        -- Eliminar el objeto de la tabla EmpresaReceptora
        DELETE FROM EmpresaReceptora
        WHERE RUC = @RUC;

        -- Confirmar la transacci�n
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- En caso de error, deshacer la transacci�n
        ROLLBACK TRANSACTION;
        -- Mostrar el mensaje de error
        THROW;
    END CATCH
END;
GO
--Ejemplo de uso
EXEC spEliminarEmpresaReceptora @RUC = 90123456789;

CREATE PROCEDURE EliminarDocumento
    @idDocumento INT
AS
BEGIN
    -- Eliminar las transacciones relacionadas
    DELETE FROM Transaccion
    WHERE Documento_idDocumento = @idDocumento;

    -- Eliminar el documento
    DELETE FROM Documento
    WHERE idDocumento = @idDocumento;
END;
GO
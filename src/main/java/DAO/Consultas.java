package DAO;

public class Consultas {

    // Consulta 1: Total de Montos Movidos por Cajero en una Fecha Específica
    public static String totalMontoPorCajeroEnFecha(String fecha) {
        String consulta = "SELECT \n" + 
                          "    Cajero.nombre,\n" + 
                          "    SUM(Documento.monto) AS TotalMontoMovido\n" + 
                          "FROM Documento\n" + 
                          "JOIN Cajero ON Documento.Cajero_idCajero = Cajero.idCajero\n" + 
                          "JOIN Transaccion ON Documento.idDocumento = Transaccion.Documento_idDocumento\n" + 
                          "WHERE Transaccion.fechaTransaccion = '" + fecha + "'\n" + 
                          "GROUP BY Cajero.nombre;";
        return consulta;
    }

    // Consulta 2: Documentos Pendientes por Área
    public static String documentosPendientesPorArea() {
        String consulta = "SELECT \n" + 
                          "    Area.nombre,\n" + 
                          "    COUNT(Documento.idDocumento) AS DocumentosPendientes\n" + 
                          "FROM Area\n" + 
                          "LEFT JOIN Caja ON Area.idArea = Caja.Area_idArea\n" + 
                          "LEFT JOIN Documento ON Caja.idCaja = Documento.Caja_idCaja\n" + 
                          "LEFT JOIN Transaccion ON Documento.idDocumento = Transaccion.Documento_idDocumento\n" + 
                          "LEFT JOIN Estado ON Transaccion.Estado_idEstado = Estado.idEstado\n" + 
                          "WHERE Estado.reporteEstado = 'Pendiente'\n" + 
                          "GROUP BY Area.nombre;";
        return consulta;
    }

    // Consulta 3: Cajeros con Mayor Monto Movido por Área
    public static String cajerosConMayorMontoPorArea() {
        String consulta = "SELECT \n" + 
                          "    Area.nombre,\n" + 
                          "    Cajero.nombre AS CajeroNombre,\n" + 
                          "    MAX(Documento.monto) AS MaxMontoMovido\n" + 
                          "FROM Area\n" + 
                          "JOIN Caja ON Area.idArea = Caja.Area_idArea\n" + 
                          "JOIN Documento ON Caja.idCaja = Documento.Caja_idCaja\n" + 
                          "JOIN Cajero ON Documento.Cajero_idCajero = Cajero.idCajero\n" + 
                          "GROUP BY Area.nombre, Cajero.nombre;";
        return consulta;
    }

    // Consulta 4: Monto Promedio Movido por Caja
    public static String montoPromedioPorCaja() {
        String consulta = "SELECT \n" + 
                          "    Caja.idCaja,\n" + 
                          "    AVG(Documento.monto) AS PromedioMontoMovido\n" + 
                          "FROM Caja\n" + 
                          "LEFT JOIN Documento ON Caja.idCaja = Documento.Caja_idCaja\n" + 
                          "GROUP BY Caja.idCaja;";
        return consulta;
    }

    // Consulta 5: Documentos por Tipo y Monto Mayor a $1000
    public static String documentosPorTipoYMontoMayorA() {
        String consulta = "SELECT \n" + 
                          "    Documento.descripcion,\n" + 
                          "    Documento.monto\n" + 
                          "FROM Documento\n" + 
                          "WHERE Documento.monto > 1000\n" + 
                          "ORDER BY Documento.monto DESC;";
        return consulta;
    }

    // Subconsulta Correlacionada 1: Cajeros con Mayor Monto Movido por Área
    public static String cajerosConMayorMontoPorAreaCorrelacionada() {
        String consulta = "SELECT \n" + 
                          "    Area.nombre,\n" + 
                          "    (SELECT TOP 1 Cajero.nombre\n" + 
                          "     FROM Documento\n" + 
                          "     JOIN Cajero ON Documento.Cajero_idCajero = Cajero.idCajero\n" + 
                          "     JOIN Caja ON Documento.Caja_idCaja = Caja.idCaja\n" + 
                          "     WHERE Caja.Area_idArea = Area.idArea\n" + 
                          "     ORDER BY Documento.monto DESC) AS CajeroConMaxMonto\n" + 
                          "FROM Area;";
        return consulta;
    }
    
    public static String cajerosConMontoMayorAlPromedio() {
        String consulta = "SELECT \n" + 
                          "    Cajero.nombre,\n" + 
                          "    SUM(Documento.monto) AS TotalMontoMovido\n" + 
                          "FROM Documento\n" + 
                          "JOIN Cajero ON Documento.Cajero_idCajero = Cajero.idCajero\n" + 
                          "GROUP BY Cajero.nombre\n" + 
                          "HAVING SUM(Documento.monto) > (\n" + 
                          "    SELECT AVG(SumaMontos.TotalMonto)\n" + 
                          "    FROM (\n" + 
                          "        SELECT SUM(Documento.monto) AS TotalMonto\n" + 
                          "        FROM Documento\n" + 
                          "        GROUP BY Documento.Cajero_idCajero\n" + 
                          "    ) AS SumaMontos\n" + 
                          ");";
        return consulta;
    }
    
    // Subconsulta Correlacionada 2: Documentos con Monto Superior al Promedio
    public static String documentosConMontoMayorAlPromedio() {
        String consulta = "SELECT D.idDocumento, D.monto, "
                + "(SELECT AVG(monto) FROM Documento) AS PromedioMonto "
                + "FROM Documento D "
                + "WHERE D.monto > (SELECT AVG(monto) FROM Documento);";
        return consulta;
    }

    public static String documentosConMontoMayorAlPromedioPorCategoria() {
        String consulta = "SELECT D.idDocumento, D.monto, "
                + "AVG(D.monto) OVER(PARTITION BY D.Categoria) AS PromedioPorCategoria "
                + "FROM Documento D "
                + "WHERE D.monto > (SELECT AVG(monto) "
                + "FROM Documento "
                + "WHERE Documento.Categoria = D.Categoria);";
        return consulta;
    }
    
    public static String totalMontoPorCajero(int idCajero) {
        return "DECLARE @idCajero INT = " + idCajero + ";\n" +
               "DECLARE @total FLOAT;\n" +
               "SET @total = dbo.fn_TotalMontoPorCajero(@idCajero);\n" +
               "SELECT @total AS TotalMontoMovido;";
    }

    // Función para obtener el nombre del cajero por ID
    public static String nombreCajero(int idCajero) {
        return "DECLARE @idCajero INT = " + idCajero + ";\n" +
               "DECLARE @nombre VARCHAR(90);\n" +
               "SET @nombre = dbo.fn_NombreCajero(@idCajero);\n" +
               "SELECT @nombre AS NombreCajero;";
    }
    
}

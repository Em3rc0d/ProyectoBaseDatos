package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
                          "    Documento.tipoDocumento,\n" + 
                          "    Documento.descripcion,\n" + 
                          "    Documento.monto\n" + 
                          "FROM Documento\n" + 
                          "WHERE Documento.monto > 1000\n" + 
                          "ORDER BY Documento.tipoDocumento, Documento.monto DESC;";
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

}

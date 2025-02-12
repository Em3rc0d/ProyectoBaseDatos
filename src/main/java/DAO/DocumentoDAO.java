package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Entidades.Documento;

public class DocumentoDAO {
    
    private Connection conexion;

    public DocumentoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public void insertar(Documento documento) throws SQLException {
        String sql = "INSERT INTO Documento (Caja_idCaja, Cajero_idCajero, EmpresaReceptora_RUC, Movimiento_idMovimiento, descripcion, monto) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {
            pst.setInt(1, documento.getIdCaja());
            pst.setInt(2, documento.getIdCajero());
            pst.setString(3, documento.getIdEmpresa());
            pst.setInt(4, documento.getIdMotivo());
            pst.setString(5, "null");
            pst.setFloat(6, documento.getMonto());
            pst.executeUpdate();
        }
    }

    public void actualizar(Documento documento) throws SQLException {
        String sql = "UPDATE Documento SET Caja_idCaja = ?, Cajero_idCajero = ?, EmpresaReceptora_RUC = ?, Movimiento_idMovimiento = ?, monto = ? WHERE idDocumento = ?";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {
            pst.setInt(1, documento.getIdCaja());
            pst.setInt(2, documento.getIdCajero());
            pst.setString(3, documento.getIdEmpresa());
            pst.setInt(4, documento.getIdMotivo());
            pst.setFloat(5, documento.getMonto());
            pst.setInt(6, documento.getIdDocumento());
            pst.executeUpdate();
        }
    }

    public void eliminar(int idDocumento) throws SQLException {
        String sql = "EXEC EliminarDocumento @idDocumento = ?";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {
            pst.setInt(1, idDocumento);
            pst.executeUpdate();
        }
    }

    public void eliminarDocumentosDeUnaCaja(int idCaja) throws SQLException {
        String sql = "DELETE FROM Documento WHERE Caja_idCaja = ?";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {
            pst.setInt(1, idCaja);
            pst.executeUpdate();
        }
    }

    public List<Documento> obtenerDocumentos() throws SQLException {
        String sql = "SELECT * FROM Documento";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            List<Documento> documentos = new ArrayList<>();
            while (rs.next()) {
                Documento documento = new Documento(rs.getInt("Caja_idCaja"), rs.getInt("Cajero_idCajero"), rs.getString("EmpresaReceptora_RUC"), rs.getInt("Movimiento_idMovimiento"), rs.getFloat("monto"));
                documento.setIdDocumento(rs.getInt("idDocumento"));
                documentos.add(documento);
            }
            return documentos;
        }
    }

    public Documento obtenerDocumento(int idCaja) throws SQLException {
        String sql = "SELECT * FROM Documento WHERE idDocumento = ?";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {
            pst.setInt(1, idCaja);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Documento(rs.getInt("Caja_idCaja"), rs.getInt("Cajero_idCajero"), rs.getString("EmpresaReceptora_RUC"), rs.getInt("Movimiento_idMovimiento"), rs.getFloat("monto"));
                }
                return null;
            }
        }
    }

}

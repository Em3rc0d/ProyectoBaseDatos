package DAO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import Entidades.Transaccion;

public class TransaccionDAO {
    private Connection conexion;

    public TransaccionDAO(Connection con) {
        this.conexion = con;
    }   

    public void insertar(Transaccion transaccion) {
        try {
            String sql = "INSERT INTO Transaccion (Documento_idDocumento, Estado_idEstado, fechaTransaccion, tipoTransaccion, descripcion) VALUES (?, ?, ?, ?, ?)";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setInt(1, transaccion.getDocumento_idDocumento());
            pst.setInt(2, transaccion.getEstado_idEstado());
            pst.setDate(3, transaccion.getFechaTransaccion());
            pst.setString(4, transaccion.getTipoTransaccion());
            pst.setString(5, transaccion.getDescripcion());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Transaccion transaccion) {   
        try {   
            String sql = "UPDATE Transaccion SET Documento_idDocumento = ?, Estado_idEstado = ?, fechaTransaccion = ?, tipoTransaccion = ?, descripcion = ? WHERE idTransaccion = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setInt(1, transaccion.getDocumento_idDocumento());
            pst.setInt(2, transaccion.getEstado_idEstado());
            pst.setDate(3, transaccion.getFechaTransaccion());
            pst.setString(4, transaccion.getTipoTransaccion());
            pst.setString(5, transaccion.getDescripcion());
            pst.setInt(6, transaccion.getIdTransaccion());
            pst.executeUpdate();
        } catch (Exception e) { 
            e.printStackTrace();                    
        }
    }   

    public void eliminar(Transaccion transaccion) {   
        try {
            String sql = "DELETE FROM Transaccion WHERE idTransaccion = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setInt(1, transaccion.getIdTransaccion());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   

    public Transaccion obtener(int idTransaccion) {
        Transaccion transaccion = null;
        try {
            String sql = "SELECT * FROM Transaccion WHERE idTransaccion = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setInt(1, idTransaccion);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {      
                transaccion = new Transaccion(rs.getInt("Documento_idDocumento"),
                        rs.getInt("Estado_idEstado"),
                        rs.getDate("fechaTransaccion"),
                        rs.getString("tipoTransaccion"),
                        rs.getString("descripcion"));
                transaccion.setIdTransaccion(rs.getInt("idTransaccion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transaccion;
    }

    public Transaccion obtener(String tipoTransaccion) {
        Transaccion transaccion = null;
        try {
            String sql = "SELECT * FROM Transaccion WHERE tipoTransaccion = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, tipoTransaccion);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                transaccion = new Transaccion(rs.getInt("Documento_idDocumento"),
                        rs.getInt("Estado_idEstado"),
                        rs.getDate("fechaTransaccion"),
                        rs.getString("tipoTransaccion"),
                        rs.getString("descripcion"));
                transaccion.setIdTransaccion(rs.getInt("idTransaccion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transaccion;
    }

    public Transaccion obtenerUltimaTransaccion() {
        Transaccion transaccion = null;
        try {
            String sql = "SELECT * FROM Transaccion ORDER BY idTransaccion DESC LIMIT 1";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                transaccion = new Transaccion(rs.getInt("Documento_idDocumento"),
                        rs.getInt("Estado_idEstado"),
                        rs.getDate("fechaTransaccion"),
                        rs.getString("tipoTransaccion"),
                        rs.getString("descripcion"));
                transaccion.setIdTransaccion(rs.getInt("idTransaccion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transaccion;
    }

    public List<Transaccion> obtenerTodasTransacciones() {
        List<Transaccion> transacciones = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Transaccion";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Transaccion transaccion = new Transaccion(rs.getInt("Documento_idDocumento"),
                        rs.getInt("Estado_idEstado"),
                        rs.getDate("fechaTransaccion"),
                        rs.getString("tipoTransaccion"),
                        rs.getString("descripcion"));
                transaccion.setIdTransaccion(rs.getInt("idTransaccion"));
                transacciones.add(transaccion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transacciones;   
    }
}

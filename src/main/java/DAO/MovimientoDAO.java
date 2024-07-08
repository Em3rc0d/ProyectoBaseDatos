package DAO;

import java.sql.Connection;

import Entidades.Movimiento;

public class MovimientoDAO {
    private Connection conexion;

    public MovimientoDAO(Connection con) {
        this.conexion = con;
    }

    public void insertar(Movimiento movimiento) {   
        try {
            String sql = "INSERT INTO Movimiento (tipoMovimiento) VALUES (?)";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, movimiento.getTipoMovimiento());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Movimiento movimiento) {
        try {
            String sql = "UPDATE Movimiento SET tipoMovimiento = ? WHERE idMovimiento = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, movimiento.getTipoMovimiento());
            pst.setInt(2, movimiento.getIdMovimiento());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(Movimiento movimiento) {   
        try {
            String sql = "DELETE FROM Movimiento WHERE idMovimiento = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setInt(1, movimiento.getIdMovimiento());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Movimiento buscar(int idMovimiento) {
        Movimiento movimiento = null;
        try {
            String sql = "SELECT * FROM Movimiento WHERE idMovimiento = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setInt(1, idMovimiento);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                movimiento = new Movimiento(rs.getString("tipoMovimiento"));
                movimiento.setIdMovimiento(rs.getInt("idMovimiento"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movimiento;
    }

    public Movimiento buscar(String tipoMovimiento) {
        Movimiento movimiento = null;
        try {
            String sql = "SELECT * FROM Movimiento WHERE tipoMovimiento = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, tipoMovimiento);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                movimiento = new Movimiento(rs.getString("tipoMovimiento"));
                movimiento.setIdMovimiento(rs.getInt("idMovimiento"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movimiento;
    }   

}

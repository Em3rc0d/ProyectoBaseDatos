package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Entidades.Caja;

public class CajaDAO {
    private Connection conexion;

    public CajaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public void insertar(Caja caja) throws SQLException {
        String sql = "INSERT INTO Caja (idArea, monto, topeMovimiento) VALUES (?, ?, ?)";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {
            pst.setInt(1, caja.getIdArea());
            pst.setDouble(2, caja.getMonto());
            pst.setDouble(3, caja.getTopeMovimiento());
            pst.executeUpdate();
        }
    }

    public void actualizar(Caja caja) throws SQLException {
        String sql = "UPDATE Caja SET idArea = ?, monto = ?, topeMovimiento = ? WHERE idCaja = ?";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {
            pst.setInt(1, caja.getIdArea());
            pst.setDouble(2, caja.getMonto());
            pst.setDouble(3, caja.getTopeMovimiento());
            pst.setInt(4, caja.getIdCaja());
            pst.executeUpdate();
        }
    }

    public void eliminar(int idCaja) throws SQLException {
        String sql = "DELETE FROM Caja WHERE idCaja = ?";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {
            pst.setInt(1, idCaja);
            pst.executeUpdate();
        }
    }

    public List<Caja> obtenerCajas() throws SQLException {
        String sql = "SELECT * FROM Caja";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            List<Caja> cajas = new ArrayList<>();
            while (rs.next()) {
                Caja caja = new Caja(rs.getInt("Area_idArea"), rs.getDouble("monto"), rs.getDouble("topeMovimiento"));
                cajas.add(caja);
            }
            return cajas;
        }
    }

    public Caja obtenerCaja(int idCaja) throws SQLException {
        String sql = "SELECT * FROM Caja WHERE id = ?";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {
            pst.setInt(1, idCaja);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Caja(rs.getInt("Area_idArea"), rs.getDouble("monto"), rs.getDouble("topeMovimiento"));
                }
                return null;
            }
        }
    }

}

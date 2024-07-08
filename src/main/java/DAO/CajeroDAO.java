package DAO;

import Entidades.Cajero;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CajeroDAO {
    private Connection ConexionBD;

    public CajeroDAO(Connection ConexionBD) {
        this.ConexionBD = ConexionBD;
    }

    public void insertar(Cajero cajero) throws SQLException {

        String sql = "INSERT INTO cajero (idCajero, nombre, apellido, email, telefono) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = this.ConexionBD.prepareStatement(sql)) {
            pst.setInt(1, cajero.getIdCajero());
            pst.setString(2, cajero.getNombre());   
            pst.setString(3, cajero.getApellido());
            pst.setString(4, cajero.getEmail());
            pst.setString(5, cajero.getTelefono());
            pst.executeUpdate();
        }
    }

    public void actualizar(Cajero cajero) throws SQLException {
        String sql = "UPDATE cajero SET nombre = ?, apellido = ?, email = ?, telefono = ? WHERE idCajero = ?";
        try (PreparedStatement pst = this.ConexionBD.prepareStatement(sql)) {
            pst.setString(1, cajero.getNombre());
            pst.setString(2, cajero.getApellido());
            pst.setString(3, cajero.getEmail());
            pst.setString(4, cajero.getTelefono());
            pst.setInt(5, cajero.getIdCajero());
            pst.executeUpdate();
        }
    }

    public void eliminar(Cajero cajero) throws SQLException {
        String sql = "DELETE FROM cajero WHERE idCajero = ?";
        try (PreparedStatement pst = this.ConexionBD.prepareStatement(sql)) {
            pst.setInt(1, cajero.getIdCajero());
            pst.executeUpdate();
        }
    }

    public List<Cajero> obtenerCajeros() throws SQLException {
        String sql = "SELECT * FROM Cajero";
        try (PreparedStatement pst = this.ConexionBD.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            List<Cajero> cajeros = new ArrayList<>();
            while (rs.next()) {
                Cajero cajero = new Cajero(rs.getInt("idCajero"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("email"), rs.getString("telefono"));
                cajeros.add(cajero);
            }
            return cajeros;
        }
    }

    public Cajero obtenerCajero(String idCajero) throws SQLException {
        String sql = "SELECT * FROM Cajero WHERE idCajero = ?";
        try (PreparedStatement pst = this.ConexionBD.prepareStatement(sql)) {
            pst.setString(1, idCajero);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Cajero(rs.getInt("idCajero"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("email"), rs.getString("telefono"));
            } else {
                return null;
            }
        }
    }
}

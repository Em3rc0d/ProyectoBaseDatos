package DAO;

import Entidades.Estado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadoDAO {
    private Connection ConexionBD;

    public EstadoDAO(Connection ConexionBD) {
        this.ConexionBD = ConexionBD;
    }

    public void insertar(Estado estado) throws SQLException {
        String sql = "INSERT INTO estado (idEstado, tipoEstado) VALUES (?, ?)";
        try (PreparedStatement pst = this.ConexionBD.prepareStatement(sql)) {
            pst.setInt(1, estado.getIdEstado());
            pst.setString(2, estado.getReporteEstado());
            pst.executeUpdate();
        }
    }

    public void actualizar(Estado estado) throws SQLException {
        String sql = "UPDATE estado SET tipoEstado = ? WHERE idEstado = ?";
        try (PreparedStatement pst = this.ConexionBD.prepareStatement(sql)) {
            pst.setString(1, estado.getReporteEstado());
            pst.setInt(2, estado.getIdEstado());
            pst.executeUpdate();
        }
    }

    public void eliminar(Estado estado) throws SQLException {
        String sql = "DELETE FROM estado WHERE idEstado = ?";
        try (PreparedStatement pst = this.ConexionBD.prepareStatement(sql)) {
            pst.setInt(1, estado.getIdEstado());
            pst.executeUpdate();
        }
    }

    public List<Estado> obtenerEstados() throws SQLException {
        String sql = "SELECT * FROM Estado";
        try (PreparedStatement pst = this.ConexionBD.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            List<Estado> estados = new ArrayList<>();
            while (rs.next()) {
                Estado estado = new Estado(rs.getInt("idEstado"), rs.getString("reporteEstado"));
                estados.add(estado);
            }
            return estados;
        }
    }

    public Estado obtenerEstado(int idEstado) throws SQLException {
        String sql = "SELECT * FROM estado WHERE idEstado = ?";
        try (PreparedStatement pst = this.ConexionBD.prepareStatement(sql)) {
            pst.setInt(1, idEstado);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Estado(rs.getInt("idEstado"), rs.getString("reporteEstado"));
            } else {
                return null;
            }
        }
    }
}

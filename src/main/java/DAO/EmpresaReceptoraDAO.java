package DAO;

import java.sql.Connection;

import Entidades.EmpresaReceptora;

public class EmpresaReceptoraDAO {
    private Connection conexion;

    public EmpresaReceptoraDAO(Connection con) {
        this.conexion = con;
    }

    public void insertar(EmpresaReceptora empresaReceptora) {   
        try {   
            String sql = "INSERT INTO EmpresaReceptora (RUC, nombre, tipo) VALUES (?, ?, ?)";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, empresaReceptora.getRUC());
            pst.setString(2, empresaReceptora.getNombre());                 
            pst.setString(3, empresaReceptora.getTipo());
            pst.executeUpdate();
        } catch (Exception e) { 
            e.printStackTrace();                    
        }
    }

    public void actualizar(EmpresaReceptora empresaReceptora) {
        try {
            String sql = "UPDATE EmpresaReceptora SET RUC = ?, nombre = ?, tipo = ? WHERE idEmpresaReceptora = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, empresaReceptora.getRUC());
            pst.setString(2, empresaReceptora.getNombre());
            pst.setString(3, empresaReceptora.getTipo());
            pst.setInt(4, empresaReceptora.getIdEmpresaReceptora());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(EmpresaReceptora empresaReceptora) {
        try {
            String sql = "DELETE FROM EmpresaReceptora WHERE idEmpresaReceptora = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setInt(1, empresaReceptora.getIdEmpresaReceptora());
            pst.executeUpdate();    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EmpresaReceptora obtener(int idEmpresaReceptora) {
        EmpresaReceptora empresaReceptora = null;
        try {
            String sql = "SELECT * FROM EmpresaReceptora WHERE idEmpresaReceptora = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setInt(1, idEmpresaReceptora);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                empresaReceptora = new EmpresaReceptora(rs.getString("RUC"), rs.getString("nombre"), rs.getString("tipo"));
                empresaReceptora.setIdEmpresaReceptora(rs.getInt("idEmpresaReceptora"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empresaReceptora;
    }

    public EmpresaReceptora obtener(String RUC) {
        EmpresaReceptora empresaReceptora = null;
        try {
            String sql = "SELECT * FROM EmpresaReceptora WHERE RUC = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, RUC);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                empresaReceptora = new EmpresaReceptora(rs.getString("RUC"), rs.getString("nombre"), rs.getString("tipo"));
                empresaReceptora.setIdEmpresaReceptora(rs.getInt("idEmpresaReceptora"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empresaReceptora;
    }

}

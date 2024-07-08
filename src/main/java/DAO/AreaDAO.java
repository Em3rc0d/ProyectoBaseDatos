package DAO;

import java.sql.Connection;

import Entidades.Area;

public class AreaDAO {
    private Connection conexion;

    public AreaDAO(Connection con) {
        this.conexion = con;
    }

    public void insertar(Area area) {
        try {
            String sql = "INSERT INTO Area (nombre) VALUES (?)";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, area.getNombre());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Area area) {
        try {
            String sql = "UPDATE Area SET nombre = ? WHERE idArea = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, area.getNombre());
            pst.setInt(2, area.getIdArea());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  

    public void eliminar(Area area) {
        try {
            String sql = "DELETE FROM Area WHERE idArea = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setInt(1, area.getIdArea());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
    public Area obtener(int idArea) {
        Area area = null;
        try {
            String sql = "SELECT * FROM Area WHERE idArea = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setInt(1, idArea);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                area = new Area(rs.getString("nombre"));
                area.setIdArea(rs.getInt("idArea"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return area;
    }

    public Area obtener(String nombre) {
        Area area = null;
        try {
            String sql = "SELECT * FROM Area WHERE nombre = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, nombre);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                area = new Area(rs.getString("nombre"));
                area.setIdArea(rs.getInt("idArea"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return area;
    }

    public java.util.List<Area> obtenerTodos() {
        java.util.List<Area> areas = new java.util.ArrayList<Area>();
        try {   
            String sql = "SELECT * FROM Area";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Area area = new Area(rs.getString("nombre"));
                area.setIdArea(rs.getInt("idArea"));
                areas.add(area);
            }
        } catch (Exception e) {
            e.printStackTrace();    
        }
        return areas;
    }
}

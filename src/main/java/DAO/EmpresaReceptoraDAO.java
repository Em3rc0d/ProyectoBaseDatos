package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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
            String sql = "UPDATE EmpresaReceptora SET RUC = ?, nombre = ?, tipo = ? WHERE RUC = ?";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, empresaReceptora.getRUC());
            pst.setString(2, empresaReceptora.getNombre());
            pst.setString(3, empresaReceptora.getTipo());
            pst.setString(4, empresaReceptora.getRUC());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // public void eliminar(EmpresaReceptora empresaReceptora) {
    //     try{
    //         String sql = "DELETE FROM EmpresaReceptora WHERE RUC = ?";
    //         java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
    //         pst.setString(1, empresaReceptora.getRUC());
    //         pst.executeUpdate();
    //     } catch (Exception e) { 
    //         e.printStackTrace();
    //     }
    // }

    public void eliminar(String RUC) {
        String sql = "EXEC spEliminarEmpresaReceptora @RUC = ?";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {
            pst.setString(1, RUC);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empresaReceptora;
    }

    public List<EmpresaReceptora> obtenerEmpresasReceptoras() {
        List<EmpresaReceptora> empresasReceptoras = null;
        try {
            String sql = "SELECT * FROM EmpresaReceptora";
            java.sql.PreparedStatement pst = this.conexion.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();
            empresasReceptoras = new java.util.ArrayList<>();
            while (rs.next()) {
                empresasReceptoras.add(new EmpresaReceptora(rs.getString("RUC"), rs.getString("nombre"), rs.getString("tipo")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empresasReceptoras;
    }
}

// --Procedimiento eliminar Empresa Receptora
// CREATE PROCEDURE spEliminarEmpresaReceptora
//     @RUC VARCHAR(45)
// AS
// BEGIN
//     -- Iniciar una transacción
//     BEGIN TRANSACTION;

//     -- Manejo de errores
//     BEGIN TRY
//         -- Eliminar las transacciones relacionadas en la tabla Transaccion
//         DELETE FROM Transaccion
//         WHERE Documento_idDocumento IN (
//             SELECT idDocumento
//             FROM Documento
//             WHERE EmpresaReceptora_RUC = @RUC
//         );

//         -- Eliminar los documentos relacionados en la tabla Documento
//         DELETE FROM Documento
//         WHERE EmpresaReceptora_RUC = @RUC;

//         -- Eliminar el objeto de la tabla EmpresaReceptora
//         DELETE FROM EmpresaReceptora
//         WHERE RUC = @RUC;

//         -- Confirmar la transacción
//         COMMIT TRANSACTION;
//     END TRY
//     BEGIN CATCH
//         -- En caso de error, deshacer la transacción
//         ROLLBACK TRANSACTION;
//         -- Mostrar el mensaje de error
//         THROW;
//     END CATCH
// END;
// GO

// EXEC spEliminarEmpresaReceptora @RUC = 90123456789;

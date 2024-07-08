package Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionBD {
    Connection connect = null;
    String usuario = "usuario";
    String password = "usersql";
    String bd = "ProyectoBaseDatos";
    String ip = "localhost";
    String port = "1433";

    String cadena = "jdbc:sqlserver://" + ip + ":" + port + ";databaseName=" + bd + ";encrypt=true;trustServerCertificate=true;";

    public Connection establecerConexion() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(cadena, usuario, password);
            System.out.println("Conectado correctamente"); // Mensaje de éxito

        } catch (ClassNotFoundException e) {
            // Error al cargar el DRIVER
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR EL DRIVER: " + e.toString());
            e.printStackTrace(); // Mostrar el rastreo de la pila para depuración
        } catch (SQLException e) {
            // Error de conexión a la base de datos
            JOptionPane.showMessageDialog(null, "ERROR DE CONEXION: " + e.toString());
            e.printStackTrace(); // Mostrar el rastreo de la pila para depuración
        }
        return connect;
    }

    // Método para cerrar la conexión
    public void cerrarConexion() {
        if (connect != null) {
            try {
                connect.close();
                System.out.println("Conexión cerrada correctamente");
            } catch (SQLException e) {
                // Error al cerrar la conexión
                JOptionPane.showMessageDialog(null, "ERROR AL CERRAR LA CONEXIÓN: " + e.toString());
                e.printStackTrace(); // Mostrar el rastreo de la pila para depuración
            }
        }
    }
}


package Paneles;

import DAO.Consultas;
import Conexion.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ConsultasGUI extends JFrame {

    private Connection conexion;
    private JTable tableResultados;
    private DefaultTableModel model;

    public ConsultasGUI(Connection conexion) {
        this.conexion = conexion;
        initComponents();
        configurarVentana();
    }

    private void initComponents() {
        setTitle("Consultas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel para los botones de consulta
        JPanel panelConsultas = new JPanel(new GridLayout(7, 1));
        panelConsultas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botones para cada consulta
        agregarBotonConsulta("Total Monto por Cajero en Fecha", () -> {
            String fecha = JOptionPane.showInputDialog(ConsultasGUI.this, "Ingrese la fecha (YYYY-MM-DD):");
            return Consultas.totalMontoPorCajeroEnFecha(fecha);
        }, panelConsultas);

        agregarBotonConsulta("Documentos Pendientes por Área", Consultas::documentosPendientesPorArea, panelConsultas);
        agregarBotonConsulta("Cajeros con Mayor Monto Movido por Área", Consultas::cajerosConMayorMontoPorArea, panelConsultas);
        agregarBotonConsulta("Monto Promedio Movido por Caja", Consultas::montoPromedioPorCaja, panelConsultas);
        agregarBotonConsulta("Documentos por Tipo y Monto Mayor a $1000", Consultas::documentosPorTipoYMontoMayorA, panelConsultas);
        agregarBotonConsulta("Cajeros con Mayor Monto por Área (Correlacionada)", Consultas::cajerosConMayorMontoPorAreaCorrelacionada, panelConsultas);

        // Configuración de la tabla de resultados
        model = new DefaultTableModel();
        tableResultados = new JTable(model);

        // Agregar la tabla dentro de un JScrollPane para permitir el desplazamiento
        JScrollPane scrollPane = new JScrollPane(tableResultados);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Añadir componentes al frame principal
        add(panelConsultas, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // Configurar el tamaño y visibilidad de la ventana
        JButton btnRetornar = new JButton("Retornar");
        btnRetornar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retornar();
            }
        });
        panelConsultas.add(btnRetornar);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Método para agregar un botón de consulta con un nombre y función específica
    private void agregarBotonConsulta(String nombre, ConsultaFuncional consultaFuncional, JPanel panel) {
        JButton boton = new JButton(nombre);
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarConsulta(consultaFuncional.ejecutar());
            }
        });
        panel.add(boton);
    }

    // Funcionalidad para ejecutar una consulta y mostrar los resultados en la tabla
    private void ejecutarConsulta(String consulta) {
        model.setRowCount(0); // Limpiar filas actuales en la tabla
        model.setColumnCount(0); // Limpiar columnas actuales en la tabla

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(consulta)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Añadir columnas a la tabla
            Vector<String> columnNames = new Vector<>();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }
            model.setColumnIdentifiers(columnNames);

            // Añadir filas a la tabla
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    row.add(rs.getString(columnIndex));
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al ejecutar la consulta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para cerrar esta ventana y mostrar la ventana principal
    private void retornar() {
        Main main = new Main(); // Reemplazar Main con el nombre de tu clase principal
        main.setVisible(true);
        dispose(); // Cierra la ventana actual (ConsultasGUI)
    }
    
    // Interfaz funcional para definir consultas con un retorno de String
    @FunctionalInterface
    private interface ConsultaFuncional {
        String ejecutar();
    }

    // Configuración adicional de la ventana principal
    private void configurarVentana() {
        setSize(800, 600); // Tamaño predeterminado de la ventana
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
    }

    public static void main(String[] args) {
        // Aquí deberías establecer la conexión a tu base de datos
        // Ejemplo:
        ConexionBD conexion = new ConexionBD();
        Connection conn = conexion.establecerConexion();

        // Ejemplo básico para mostrar la GUI
        new ConsultasGUI(conn);
    }
}

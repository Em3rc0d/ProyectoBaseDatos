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

        JPanel panelConsultas = new JPanel(new GridLayout(12, 1));
        panelConsultas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        agregarBotonConsulta("Total Monto por Cajero en Fecha", () -> {
            String fecha = JOptionPane.showInputDialog(ConsultasGUI.this, "Ingrese la fecha (YYYY-MM-DD):");
            return Consultas.totalMontoPorCajeroEnFecha(fecha);
        }, panelConsultas);

        agregarBotonConsulta("Documentos Pendientes por Área", Consultas::documentosPendientesPorArea, panelConsultas);
        agregarBotonConsulta("Cajeros con Mayor Monto Movido por Área", Consultas::cajerosConMayorMontoPorArea, panelConsultas);
        agregarBotonConsulta("Monto Promedio Movido por Caja", Consultas::montoPromedioPorCaja, panelConsultas);
        agregarBotonConsulta("Documentos por Tipo y Monto Mayor a $1000", Consultas::documentosPorTipoYMontoMayorA, panelConsultas);
        agregarBotonConsulta("Cajeros con Mayor Monto por Área (Correlacionada)", Consultas::cajerosConMayorMontoPorAreaCorrelacionada, panelConsultas);
        agregarBotonConsulta("Cajeros con Monto Mayor al Promedio", Consultas::cajerosConMontoMayorAlPromedio, panelConsultas);
        agregarBotonConsulta("Documentos con Monto Superior al Promedio", Consultas::documentosConMontoMayorAlPromedio, panelConsultas);
        agregarBotonConsulta("Documentos con Monto Superior al Promedio por Categoría", Consultas::documentosConMontoMayorAlPromedioPorArea, panelConsultas);

        // Agregar nuevas consultas
        agregarBotonConsulta("Total Monto Movido por Cajero", () -> {
            int idCajero = Integer.parseInt(JOptionPane.showInputDialog(ConsultasGUI.this, "Ingrese el ID del Cajero:"));
            return Consultas.totalMontoPorCajero(idCajero);
        }, panelConsultas);

        agregarBotonConsulta("Nombre del Cajero por ID", () -> {
            int idCajero = Integer.parseInt(JOptionPane.showInputDialog(ConsultasGUI.this, "Ingrese el ID del Cajero:"));
            return Consultas.nombreCajero(idCajero);
        }, panelConsultas);

        model = new DefaultTableModel();
        tableResultados = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(tableResultados);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnRetornar = new JButton("Retornar");
        btnRetornar.addActionListener(e -> retornar());
        panelConsultas.add(btnRetornar);

        add(panelConsultas, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        setSize(1200, 800);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void agregarBotonConsulta(String nombre, ConsultaFuncional consultaFuncional, JPanel panel) {
        JButton boton = new JButton(nombre);
        boton.addActionListener(e -> ejecutarConsulta(consultaFuncional.ejecutar()));
        panel.add(boton);
    }

    private void ejecutarConsulta(String consulta) {
    model.setRowCount(0);
    model.setColumnCount(0);

    System.out.println("Consulta generada: " + consulta);  // Línea añadida para depuración

    try (Statement stmt = conexion.createStatement();
         ResultSet rs = stmt.executeQuery(consulta)) {

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        model.setColumnIdentifiers(columnNames);

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


    private void retornar() {
        Main main = new Main();
        main.setVisible(true);
        dispose();
    }

    @FunctionalInterface
    private interface ConsultaFuncional {
        String ejecutar();
    }

    private void configurarVentana() {
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        ConexionBD conexion = new ConexionBD();
        Connection conn = conexion.establecerConexion();

        new ConsultasGUI(conn);
    }
}

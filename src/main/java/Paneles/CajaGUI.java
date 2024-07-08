package Paneles;

import DAO.CajaDAO;
import Entidades.Caja;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Conexion.ConexionBD;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CajaGUI extends JFrame {
    private CajaDAO cajaDAO;
    private JTextField txtIdCaja, txtIdArea, txtMonto, txtTopeMovimiento;
    private JTable tableCajas;
    private DefaultTableModel model;

    public CajaGUI(Connection conexion) {
        this.cajaDAO = new CajaDAO(conexion);
        initComponents();
        loadData();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Gestión de Cajas");

        JPanel panelForm = new JPanel(new GridLayout(5, 2));

        panelForm.add(new JLabel("ID Caja:"));
        txtIdCaja = new JTextField();
        txtIdCaja.setEditable(false);
        panelForm.add(txtIdCaja);

        panelForm.add(new JLabel("ID Área:"));
        txtIdArea = new JTextField();
        panelForm.add(txtIdArea);

        panelForm.add(new JLabel("Monto:"));
        txtMonto = new JTextField();
        panelForm.add(txtMonto);

        panelForm.add(new JLabel("Tope Movimiento:"));
        txtTopeMovimiento = new JTextField();
        panelForm.add(txtTopeMovimiento);

        JButton btnInsertar = new JButton("Insertar");
        btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarCaja();
            }
        });
        panelForm.add(btnInsertar);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCaja();
            }
        });
        panelForm.add(btnActualizar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCaja();
            }
        });
        panelForm.add(btnEliminar);

        model = new DefaultTableModel();
        model.addColumn("ID Caja");
        model.addColumn("ID Área");
        model.addColumn("Monto");
        model.addColumn("Tope Movimiento");

        tableCajas = new JTable(model);
        add(new JScrollPane(tableCajas), BorderLayout.CENTER);

        JButton btnActualizarTabla = new JButton("Actualizar Tabla");
        btnActualizarTabla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        add(btnActualizarTabla, BorderLayout.SOUTH);

        add(panelForm, BorderLayout.WEST);
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<Caja> cajas = cajaDAO.obtenerCajas();
            for (Caja caja : cajas) {
                model.addRow(new Object[]{
                        caja.getIdCaja(),
                        caja.getIdArea(),
                        caja.getMonto(),
                        caja.getTopeMovimiento()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarCaja() {
        try {
            int idCaja = Integer.parseInt(txtIdCaja.getText());
            int idArea = Integer.parseInt(txtIdArea.getText());
            double monto = Double.parseDouble(txtMonto.getText());
            double topeMovimiento = Double.parseDouble(txtTopeMovimiento.getText());

            Caja caja = new Caja(idCaja, idArea, monto, topeMovimiento);
            cajaDAO.insertar(caja);
            JOptionPane.showMessageDialog(this, "Caja insertada correctamente.");
            loadData();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al insertar la caja.");
        }
    }

    private void actualizarCaja() {
        try {
            int idArea = Integer.parseInt(txtIdArea.getText());
            double monto = Double.parseDouble(txtMonto.getText());
            double topeMovimiento = Double.parseDouble(txtTopeMovimiento.getText());

            Caja caja = new Caja(idArea, monto, topeMovimiento);
            cajaDAO.actualizar(caja);
            JOptionPane.showMessageDialog(this, "Caja actualizada correctamente.");
            loadData();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar la caja.");
        }
    }

    private void eliminarCaja() {
        try {
            int idCaja = Integer.parseInt(txtIdCaja.getText());
            cajaDAO.eliminar(idCaja);
            JOptionPane.showMessageDialog(this, "Caja eliminada correctamente.");
            loadData();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar la caja.");
        }
    }

    public static void main(String[] args) {
        // Aquí deberías establecer la conexión a tu base de datos
        // Ejemplo:
        ConexionBD conexion = new ConexionBD();
        Connection conn = conexion.establecerConexion();

        // Ejemplo básico para mostrar la GUI
        new CajaGUI(conn);
    }
}

package Paneles;

import DAO.CajeroDAO;
import Entidades.Cajero;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SeleccionarCajeroGUI extends JFrame {

    private CajeroDAO cajeroDAO;
    private JTable tableCajeros;
    private DefaultTableModel model;
    private JTextField txtIdCajero; // Este campo de texto se pasará

    public SeleccionarCajeroGUI(Connection conexion, JTextField txtIdCajero) {
        this.cajeroDAO = new CajeroDAO(conexion);
        this.txtIdCajero = txtIdCajero;
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Seleccionar Cajero");

        model = new DefaultTableModel();
        model.addColumn("ID Cajero");
        model.addColumn("Nombre");
        model.addColumn("Apellido");
        model.addColumn("Email");
        model.addColumn("Teléfono");

        tableCajeros = new JTable(model);
        add(new JScrollPane(tableCajeros), BorderLayout.CENTER);

        JPanel panelSouth = new JPanel(new GridLayout(1, 1));

        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarCajero();
            }
        });
        panelSouth.add(btnSeleccionar);

        add(panelSouth, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<Cajero> cajeros = cajeroDAO.obtenerCajeros();
            for (Cajero cajero : cajeros) {
                model.addRow(new Object[]{
                    cajero.getIdCajero(),
                    cajero.getNombre(),
                    cajero.getApellido(),
                    cajero.getEmail(),
                    cajero.getTelefono()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void seleccionarCajero() {
        int selectedRow = tableCajeros.getSelectedRow();
        if (selectedRow != -1) {
            String idCajero = model.getValueAt(selectedRow, 0).toString();
            txtIdCajero.setText(idCajero);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cajero de la tabla.");
        }
    }
}

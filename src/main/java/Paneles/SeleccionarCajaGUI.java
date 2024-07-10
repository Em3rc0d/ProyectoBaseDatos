package Paneles;

import DAO.CajaDAO;
import Entidades.Caja;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SeleccionarCajaGUI extends JFrame {

    private CajaDAO cajaDAO;
    private JTable tableCajas;
    private DefaultTableModel model;
    private JTextField txtIdCaja; // Este campo de texto se pasará

    public SeleccionarCajaGUI(Connection conexion, JTextField txtIdCaja) {
        this.cajaDAO = new CajaDAO(conexion);
        this.txtIdCaja = txtIdCaja;
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Seleccionar Caja");

        model = new DefaultTableModel();
        model.addColumn("ID Caja");
        model.addColumn("ID Área");
        model.addColumn("Monto");
        model.addColumn("Tope Movimiento");

        tableCajas = new JTable(model);
        add(new JScrollPane(tableCajas), BorderLayout.CENTER);

        JPanel panelSouth = new JPanel(new GridLayout(1, 1));

        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarCaja();
            }
        });
        panelSouth.add(btnSeleccionar);

        add(panelSouth, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<Caja> cajas = cajaDAO.obtenerCajas();
            for (Caja caja : cajas) {
                model.addRow(new Object[]{
                    caja.getIdCaja(),
                    caja.getArea_idArea(),
                    caja.getMonto(),
                    caja.getTopeMovimiento()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void seleccionarCaja() {
        int selectedRow = tableCajas.getSelectedRow();
        if (selectedRow != -1) {
            String idCaja = model.getValueAt(selectedRow, 0).toString();
            txtIdCaja.setText(idCaja);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una caja de la tabla.");
        }
    }
}

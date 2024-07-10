package Paneles;

import DAO.MovimientoDAO;
import Entidades.Movimiento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SeleccionarMotivoGUI extends JFrame {

    private MovimientoDAO movimientoDAO;
    private JTable tableMovimientos;
    private DefaultTableModel model;
    private JTextField txtIdMovimiento;

    public SeleccionarMotivoGUI(Connection conexion, JTextField txtIdMovimiento) {
        this.movimientoDAO = new MovimientoDAO(conexion);
        this.txtIdMovimiento = txtIdMovimiento;
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Seleccionar Motivo");

        model = new DefaultTableModel();
        model.addColumn("ID Movimiento");
        model.addColumn("Tipo Movimiento");

        tableMovimientos = new JTable(model);
        add(new JScrollPane(tableMovimientos), BorderLayout.CENTER); 

        JPanel panelSouth = new JPanel(new GridLayout(1, 1));

        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarMovimiento();
            }
        });
        panelSouth.add(btnSeleccionar);

        add(panelSouth, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        List<Movimiento> movimientos = movimientoDAO.obtenerTodos();
        for (Movimiento movimiento : movimientos) {
            model.addRow(new Object[]{movimiento.getIdMovimiento(), movimiento.getTipoMovimiento()});
        }
    }

    private void seleccionarMovimiento() {
        int selectedRow = tableMovimientos.getSelectedRow();
        if (selectedRow != -1) {
            String idMovimiento = model.getValueAt(selectedRow, 0).toString();
            txtIdMovimiento.setText(idMovimiento);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla.");
        }
    }
}


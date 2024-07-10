package Paneles;

import DAO.AreaDAO;
import Entidades.Area;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SeleccionarAreaGUI extends JFrame {

    private AreaDAO areaDAO;
    private JTable tableAreas;
    private DefaultTableModel model;
    private JTextField txtIdArea; // Este campo de texto se pasará desde la clase que llame a esta GUI

    public SeleccionarAreaGUI(Connection conexion, JTextField txtIdArea) {
        this.areaDAO = new AreaDAO(conexion);
        this.txtIdArea = txtIdArea;
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Seleccionar Área");

        model = new DefaultTableModel();
        model.addColumn("ID Área");
        model.addColumn("Nombre");

        tableAreas = new JTable(model);
        add(new JScrollPane(tableAreas), BorderLayout.CENTER);

        JPanel panelSouth = new JPanel(new GridLayout(1, 1));

        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarArea();
            }
        });
        panelSouth.add(btnSeleccionar);

        add(panelSouth, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        List<Area> areas = areaDAO.obtenerTodos();
        for (Area area : areas) {
            model.addRow(new Object[]{
                area.getIdArea(),
                area.getNombre()
            });
        }
    }

    private void seleccionarArea() {
        int selectedRow = tableAreas.getSelectedRow();
        if (selectedRow != -1) {
            String idArea = model.getValueAt(selectedRow, 0).toString();
            txtIdArea.setText(idArea);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un área de la tabla.");
        }
    }
}

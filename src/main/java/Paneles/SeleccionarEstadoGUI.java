package Paneles;

import DAO.EstadoDAO;
import Entidades.Estado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SeleccionarEstadoGUI extends JFrame {

    private EstadoDAO estadoDAO;
    private JTable tableEstados;
    private DefaultTableModel model;
    private JTextField txtIdEstado; // Este campo de texto se pasará desde la clase que llame a esta GUI

    public SeleccionarEstadoGUI(Connection conexion, JTextField txtReporte) throws SQLException {
        this.estadoDAO = new EstadoDAO(conexion);
        this.txtIdEstado = txtReporte;
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() { 
        setTitle("Seleccionar de Estados");
        model = new DefaultTableModel();
        model.addColumn("ID Estado");
        model.addColumn("Reporte Estado");
        tableEstados = new JTable(model);
        add(new JScrollPane(tableEstados), BorderLayout.CENTER);
        JPanel panelSouth = new JPanel(new GridLayout(1, 1));

        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarEstado();
            }
        });
        panelSouth.add(btnSeleccionar);
        add(panelSouth, BorderLayout.SOUTH);
    }


    private void loadData() {
        model.setRowCount(0);
        List<Estado> estados = estadoDAO.obtenerEstados();
        for (Estado estado : estados){
            model.addRow(new Object[] { 
                estado.getIdEstado(), 
                estado.getReporteEstado() 
            });
        }
    }

    private void seleccionarEstado() {
        int selectedRow = tableEstados.getSelectedRow();
        if (selectedRow != -1) {
            String idEstado = model.getValueAt(selectedRow, 0).toString();
            txtIdEstado.setText(idEstado);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un estado de la tabla");
        }
    }
}

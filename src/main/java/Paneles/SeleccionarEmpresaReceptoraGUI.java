package Paneles;

import DAO.EmpresaReceptoraDAO;
import Entidades.EmpresaReceptora;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class SeleccionarEmpresaReceptoraGUI extends JFrame {

    private EmpresaReceptoraDAO empresaReceptoraDAO;
    private JTable tableEmpresas;
    private DefaultTableModel model;
    private JTextField txtIdEmpresa;

    public SeleccionarEmpresaReceptoraGUI(Connection conexion, JTextField txtIdEmpresa) {
        this.empresaReceptoraDAO = new EmpresaReceptoraDAO(conexion);
        this.txtIdEmpresa = txtIdEmpresa;
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Seleccionar Empresa Receptora");

        model = new DefaultTableModel();
        model.addColumn("RUC");
        model.addColumn("Nombre");
        model.addColumn("Tipo");

        tableEmpresas = new JTable(model);
        add(new JScrollPane(tableEmpresas), BorderLayout.CENTER);

        JPanel panelSouth = new JPanel(new GridLayout(1, 1));

        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarEmpresa();
            }
        });
        panelSouth.add(btnSeleccionar);

        add(panelSouth, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        List<EmpresaReceptora> empresas = empresaReceptoraDAO.obtenerEmpresasReceptoras();
        for (EmpresaReceptora empresa : empresas) {
            model.addRow(new Object[]{
                    empresa.getRUC(),
                    empresa.getNombre(),
                    empresa.getTipo()
            });
        }
    }

    private void seleccionarEmpresa() {
        int selectedRow = tableEmpresas.getSelectedRow();
        if (selectedRow != -1) {
            String ruc = model.getValueAt(selectedRow, 0).toString();
            txtIdEmpresa.setText(ruc);
            dispose(); // Cierra la ventana de selección
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una empresa receptora de la tabla.");
        }
    }

    public static void main(String[] args) {
        // Ejemplo de uso
        Connection conexion = null; // Aquí deberías tener tu conexión a la base de datos
        JTextField txtIdEmpresa = new JTextField(10); // Ejemplo de JTextField donde se mostrará el RUC seleccionado
        new SeleccionarEmpresaReceptoraGUI(conexion, txtIdEmpresa);
    }
}

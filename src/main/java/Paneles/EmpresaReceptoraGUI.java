package Paneles;

import Conexion.ConexionBD;
import DAO.EmpresaReceptoraDAO;
import Entidades.EmpresaReceptora;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class EmpresaReceptoraGUI extends JFrame {

    private EmpresaReceptoraDAO empresaReceptoraDAO;
    private JTextField txtRUC, txtNombre, txtTipo;
    private JTable tableEmpresasReceptoras;
    private DefaultTableModel model;
    protected String selectedRUC;

    public EmpresaReceptoraGUI(Connection conexion) {
        this.empresaReceptoraDAO = new EmpresaReceptoraDAO(conexion);
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800); // Ajusta el tamaño de la ventana según sea necesario
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Gestión de Empresas Receptoras");
    
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("RUC:"), gbc);
        txtRUC = new JTextField(15);
        txtRUC.setEditable(false);
        gbc.gridx = 1;
        panelForm.add(txtRUC, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtNombre, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("Tipo:"), gbc);
        txtTipo = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtTipo, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton btnInsertar = new JButton("Insertar");
        btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarEmpresaReceptora();
                limpiarInputs();
            }
        });
        panelForm.add(btnInsertar, gbc);
    
        gbc.gridy = 4;
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarEmpresaReceptora();
                limpiarInputs();
            }
        });
        panelForm.add(btnActualizar, gbc);
    
        gbc.gridy = 5;
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarEmpresaReceptora();
                limpiarInputs();
            }
        });
        panelForm.add(btnEliminar, gbc);
    
        model = new DefaultTableModel();
        model.addColumn("RUC");
        model.addColumn("Nombre");
        model.addColumn("Tipo");
    
        tableEmpresasReceptoras = new JTable(model);
        add(new JScrollPane(tableEmpresasReceptoras), BorderLayout.CENTER);
    
        JPanel panelSouth = new JPanel(new GridLayout(1, 1));

        JButton btnRetornar = new JButton("Retornar");
        btnRetornar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retornar();
            }
        });
        panelSouth.add(btnRetornar);
    
        tableEmpresasReceptoras.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = tableEmpresasReceptoras.getSelectedRow();
                txtRUC.setText(model.getValueAt(selectedRow, 0).toString());
                txtNombre.setText(model.getValueAt(selectedRow, 1).toString());
                txtTipo.setText(model.getValueAt(selectedRow, 2).toString());
                selectedRUC = model.getValueAt(selectedRow, 0).toString();
            }
        });
    
        add(panelSouth, BorderLayout.SOUTH);
        add(panelForm, BorderLayout.WEST);
    }
    

    private void loadData() {
        model.setRowCount(0);
        List<EmpresaReceptora> empresasReceptoras = empresaReceptoraDAO.obtenerEmpresasReceptoras();
        for (EmpresaReceptora empresaReceptora : empresasReceptoras) {
            model.addRow(new Object[]{
                empresaReceptora.getRUC(),
                empresaReceptora.getNombre(),
                empresaReceptora.getTipo()
            });
        }
    }

    private void insertarEmpresaReceptora() {
        String RUC = txtRUC.getText();
        String nombre = txtNombre.getText();
        String tipo = txtTipo.getText();
        EmpresaReceptora empresaReceptora = new EmpresaReceptora(RUC, nombre, tipo);
        empresaReceptoraDAO.insertar(empresaReceptora);
        JOptionPane.showMessageDialog(this, "Empresa Receptora insertada correctamente.");
        loadData();
    }

    private void actualizarEmpresaReceptora() {
        String RUC = txtRUC.getText();
        String nombre = txtNombre.getText();
        String tipo = txtTipo.getText();
        EmpresaReceptora empresaReceptora = new EmpresaReceptora(RUC, nombre, tipo);
        empresaReceptoraDAO.actualizar(empresaReceptora);
        JOptionPane.showMessageDialog(this, "Empresa Receptora actualizada correctamente.");
        loadData();
    }

    private void eliminarEmpresaReceptora() {
        String RUC = txtRUC.getText();
        empresaReceptoraDAO.eliminar(RUC);
        JOptionPane.showMessageDialog(this, "Empresa Receptora eliminada correctamente.");
        loadData();
    }

    private void retornar() {
        new Main().setVisible(true);
        setVisible(false);
    }

    private void limpiarInputs() {
        txtRUC.setText("");
        txtNombre.setText("");
        txtTipo.setText("");
    }

    public static void main(String[] args) {
        // Aquí deberías establecer la conexión a tu base de datos
        // Ejemplo:
        ConexionBD conexion = new ConexionBD();
        Connection conn = conexion.establecerConexion();

        // Ejemplo básico para mostrar la GUI
        new EmpresaReceptoraGUI(conn);
    }
}

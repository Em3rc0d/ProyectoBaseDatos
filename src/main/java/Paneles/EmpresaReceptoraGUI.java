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
import java.sql.SQLException;
import java.util.List;

public class EmpresaReceptoraGUI extends JFrame {
    private EmpresaReceptoraDAO empresaReceptoraDAO;
    private JTextField txtIdEmpresaReceptora, txtRUC, txtNombre, txtTipo;
    private JTable tableEmpresasReceptoras;
    private DefaultTableModel model;

    public EmpresaReceptoraGUI(Connection conexion) {
        this.empresaReceptoraDAO = new EmpresaReceptoraDAO(conexion);
        initComponents();
        loadData();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Gestión de Empresas Receptoras");

        JPanel panelForm = new JPanel(new GridLayout(5, 2));

        panelForm.add(new JLabel("RUC:"));
        txtRUC = new JTextField();
        panelForm.add(txtRUC);

        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Tipo:"));
        txtTipo = new JTextField();
        panelForm.add(txtTipo);

        JButton btnInsertar = new JButton("Insertar");
        btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarEmpresaReceptora();
            }
        });
        panelForm.add(btnInsertar);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarEmpresaReceptora();
            }
        });
        panelForm.add(btnActualizar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarEmpresaReceptora();
            }
        });
        panelForm.add(btnEliminar);

        model = new DefaultTableModel();
        model.addColumn("RUC");
        model.addColumn("Nombre");
        model.addColumn("Tipo");

        tableEmpresasReceptoras = new JTable(model);
        add(new JScrollPane(tableEmpresasReceptoras), BorderLayout.CENTER);

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
        int idEmpresaReceptora = Integer.parseInt(txtIdEmpresaReceptora.getText());
        String RUC = txtRUC.getText();
        String nombre = txtNombre.getText();
        String tipo = txtTipo.getText();
        EmpresaReceptora empresaReceptora = new EmpresaReceptora(RUC, nombre, tipo);
        empresaReceptora.setIdEmpresaReceptora(idEmpresaReceptora);
        empresaReceptoraDAO.actualizar(empresaReceptora);
        JOptionPane.showMessageDialog(this, "Empresa Receptora actualizada correctamente.");
        loadData();
    }

    private void eliminarEmpresaReceptora() {
        int idEmpresaReceptora = Integer.parseInt(txtIdEmpresaReceptora.getText());
        EmpresaReceptora empresaReceptora = new EmpresaReceptora("", "", "");
        empresaReceptora.setIdEmpresaReceptora(idEmpresaReceptora);
        empresaReceptoraDAO.eliminar(empresaReceptora);
        JOptionPane.showMessageDialog(this, "Empresa Receptora eliminada correctamente.");
        loadData();
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

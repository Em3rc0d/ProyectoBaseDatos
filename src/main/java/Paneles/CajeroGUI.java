package Paneles;

import Conexion.ConexionBD;
import DAO.CajeroDAO;
import Entidades.Cajero;

import Conexion.ConexionBD;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CajeroGUI extends JFrame {

    private CajeroDAO cajeroDAO;
    private JTextField txtIdCajero, txtNombre, txtApellido, txtEmail, txtTelefono;
    private JTable tableCajeros;
    private DefaultTableModel model;
    protected int selectedIdCajero;

    public CajeroGUI(Connection conexion) {
        this.cajeroDAO = new CajeroDAO(conexion);
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);// Ajusta el tamaño de la ventana según sea necesario
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Gestión de Cajeros");
    
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("ID Cajero:"), gbc);
        txtIdCajero = new JTextField(15);
        txtIdCajero.setEditable(false);
        gbc.gridx = 1;
        panelForm.add(txtIdCajero, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtNombre, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("Apellido:"), gbc);
        txtApellido = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtApellido, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelForm.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtEmail, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelForm.add(new JLabel("Teléfono:"), gbc);
        txtTelefono = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtTelefono, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JButton btnInsertar = new JButton("Insertar");
        btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarCajero();
                limpiarInputs();
            }
        });
        panelForm.add(btnInsertar, gbc);
    
        gbc.gridy = 6;
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCajero();
                limpiarInputs();
            }
        });
        panelForm.add(btnActualizar, gbc);
    
        gbc.gridy = 7;
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCajero();
                limpiarInputs();
            }
        });
        panelForm.add(btnEliminar, gbc);
    
        model = new DefaultTableModel();
        model.addColumn("ID Cajero");
        model.addColumn("Nombre");
        model.addColumn("Apellido");
        model.addColumn("Email");
        model.addColumn("Teléfono");
    
        tableCajeros = new JTable(model);
        add(new JScrollPane(tableCajeros), BorderLayout.CENTER);
    
        JPanel panelSouth = new JPanel(new GridLayout(1, 1));

    
        JButton btnRetornar = new JButton("Retornar");
        btnRetornar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retornar();
            }
        });
        panelSouth.add(btnRetornar);
    
        tableCajeros.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = tableCajeros.getSelectedRow();
                txtIdCajero.setText(model.getValueAt(selectedRow, 0).toString());
                txtNombre.setText(model.getValueAt(selectedRow, 1).toString());
                txtApellido.setText(model.getValueAt(selectedRow, 2).toString());
                txtEmail.setText(model.getValueAt(selectedRow, 3).toString());
                txtTelefono.setText(model.getValueAt(selectedRow, 4).toString());
                selectedIdCajero = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            }
        });
    
        add(panelSouth, BorderLayout.SOUTH);
        add(panelForm, BorderLayout.WEST);
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

    private void insertarCajero() {
        try {
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String email = txtEmail.getText();
            String telefono = txtTelefono.getText();

            Cajero cajero = new Cajero(nombre, apellido, email, telefono);
            cajeroDAO.insertar(cajero);
            JOptionPane.showMessageDialog(this, "Cajero insertado correctamente.");
            loadData();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al insertar el cajero.");
        }
    }

    private void actualizarCajero() {
        try {
            int idCajero = Integer.parseInt(txtIdCajero.getText());
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String email = txtEmail.getText();
            String telefono = txtTelefono.getText();

            Cajero cajero = new Cajero(idCajero, nombre, apellido, email, telefono);
            cajeroDAO.actualizar(cajero);
            JOptionPane.showMessageDialog(this, "Cajero actualizado correctamente.");
            loadData();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el cajero.");
        }
    }

    private void eliminarCajero() {
        try {
            int idCajero = Integer.parseInt(txtIdCajero.getText());
            cajeroDAO.eliminar(new Cajero(idCajero, "", "", "", ""));
            JOptionPane.showMessageDialog(this, "Cajero eliminado correctamente.");
            loadData();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar el cajero.");
        }
    }

    private void retornar() {
        new Main().setVisible(true);
        setVisible(false);
    }

    private void limpiarInputs() {
        txtIdCajero.setText("");        
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
    }

    public static void main(String[] args) {
        // Aquí deberías establecer la conexión a tu base de datos
        // Ejemplo:
        ConexionBD conexion = new ConexionBD();
        Connection conn = conexion.establecerConexion();

        // Ejemplo básico para mostrar la GUI
        new CajeroGUI(conn);
    }
}

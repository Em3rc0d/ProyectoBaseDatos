package Paneles;

import Conexion.ConexionBD;
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

public class CajeroGUI extends JFrame {

    private CajeroDAO cajeroDAO;
    private JTextField txtIdCajero, txtNombre, txtApellido, txtEmail, txtTelefono;
    private JTable tableCajeros;
    private DefaultTableModel model;

    public CajeroGUI(Connection conexion) {
        this.cajeroDAO = new CajeroDAO(conexion);
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Gestión de Cajeros");

        JPanel panelForm = new JPanel(new GridLayout(6, 2));
        
        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panelForm.add(txtApellido);

        panelForm.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelForm.add(txtEmail);

        panelForm.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelForm.add(txtTelefono);

        JButton btnInsertar = new JButton("Insertar");
        btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarCajero();
            }
        });
        panelForm.add(btnInsertar);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCajero();
            }
        });
        panelForm.add(btnActualizar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCajero();
            }
        });
        panelForm.add(btnEliminar);

        model = new DefaultTableModel();
        model.addColumn("ID Cajero");
        model.addColumn("Nombre");
        model.addColumn("Apellido");
        model.addColumn("Email");
        model.addColumn("Teléfono");

        tableCajeros = new JTable(model);
        add(new JScrollPane(tableCajeros), BorderLayout.CENTER);

        JPanel panelSouth = new JPanel(new GridLayout(2, 1));

        JButton btnActualizarTabla = new JButton("Actualizar Tabla");
        btnActualizarTabla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        panelSouth.add(btnActualizarTabla);

        JButton btnRetornar = new JButton("Retornar");
        btnRetornar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retornar();
            }
        });
        panelSouth.add(btnRetornar);

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
            int idCajero = Integer.parseInt(txtIdCajero.getText());
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String email = txtEmail.getText();
            String telefono = txtTelefono.getText();

            Cajero cajero = new Cajero(idCajero, nombre, apellido, email, telefono);
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

    public static void main(String[] args) {
        // Aquí deberías establecer la conexión a tu base de datos
        // Ejemplo:
        ConexionBD conexion = new ConexionBD();
        Connection conn = conexion.establecerConexion();

        // Ejemplo básico para mostrar la GUI
        new CajeroGUI(conn);
    }
}

package Paneles;

import DAO.CajaDAO;
import Entidades.Caja;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Conexion.ConexionBD;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CajaGUI extends JFrame {

    private CajaDAO cajaDAO;
    private JTextField txtIdCaja, txtIdArea, txtMonto, txtTopeMovimiento;
    private JTable tableCajas;
    private DefaultTableModel model;
    Connection conn;

    public CajaGUI(Connection conexion) {
        this.cajaDAO = new CajaDAO(conexion);
        conn = conexion;
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
    setTitle("Gestión de Cajas");

    JPanel panelForm = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);

    // ID Caja
    gbc.gridx = 0;
    gbc.gridy = 0;
    panelForm.add(new JLabel("ID Caja:"), gbc);

    txtIdCaja = new JTextField();
    txtIdCaja.setEditable(false);
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    panelForm.add(txtIdCaja, gbc);
    gbc.gridwidth = 1; // reset

    // ID Área
    gbc.gridx = 0;
    gbc.gridy = 1;
    panelForm.add(new JLabel("ID Área:"), gbc);

    txtIdArea = new JTextField();
    gbc.gridx = 1;
    gbc.gridy = 1;
    panelForm.add(txtIdArea, gbc);

    JButton btnSeleccionarCaja = new JButton("Seleccionar");
    btnSeleccionarCaja.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            seleccionarArea();
        }
    });
    gbc.gridx = 2;
    gbc.gridy = 1;
    panelForm.add(btnSeleccionarCaja, gbc);

    // Monto
    gbc.gridx = 0;
    gbc.gridy = 2;
    panelForm.add(new JLabel("Monto:"), gbc);

    txtMonto = new JTextField();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    panelForm.add(txtMonto, gbc);
    gbc.gridwidth = 1; // reset

    // Tope Movimiento
    gbc.gridx = 0;
    gbc.gridy = 3;
    panelForm.add(new JLabel("Tope Movimiento:"), gbc);

    txtTopeMovimiento = new JTextField();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    panelForm.add(txtTopeMovimiento, gbc);
    gbc.gridwidth = 1; // reset

    // Botones
    JButton btnInsertar = new JButton("Insertar");
    btnInsertar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            insertarCaja();
        }
    });
    gbc.gridx = 0;
    gbc.gridy = 4;
    panelForm.add(btnInsertar, gbc);

    JButton btnActualizar = new JButton("Actualizar");
    btnActualizar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            actualizarCaja();
        }
    });
    gbc.gridx = 1;
    gbc.gridy = 4;
    panelForm.add(btnActualizar, gbc);

    JButton btnEliminar = new JButton("Eliminar");
    btnEliminar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            eliminarCaja();
        }
    });
    gbc.gridx = 2;
    gbc.gridy = 4;
    panelForm.add(btnEliminar, gbc);

    model = new DefaultTableModel();
    model.addColumn("ID Caja");
    model.addColumn("ID Área");
    model.addColumn("Monto");
    model.addColumn("Tope Movimiento");

    tableCajas = new JTable(model);
    add(new JScrollPane(tableCajas), BorderLayout.CENTER);

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

    private void insertarCaja() {
        try {
            int idArea = Integer.parseInt(txtIdArea.getText());
            double monto = Double.parseDouble(txtMonto.getText());
            double topeMovimiento = Double.parseDouble(txtTopeMovimiento.getText());

            Caja caja = new Caja(idArea, monto, topeMovimiento);
            cajaDAO.insertar(caja);
            JOptionPane.showMessageDialog(this, "Caja insertada correctamente.");
            loadData();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al insertar la caja.");
        }
    }

    private void actualizarCaja() {
        try {
            int idArea = Integer.parseInt(txtIdArea.getText());
            double monto = Double.parseDouble(txtMonto.getText());
            double topeMovimiento = Double.parseDouble(txtTopeMovimiento.getText());

            Caja caja = new Caja(idArea, monto, topeMovimiento);
            cajaDAO.actualizar(caja);
            JOptionPane.showMessageDialog(this, "Caja actualizada correctamente.");
            loadData();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar la caja.");
        }
    }

    private void eliminarCaja() {
        try {
            int idCaja = Integer.parseInt(txtIdCaja.getText());
            cajaDAO.eliminar(idCaja);
            JOptionPane.showMessageDialog(this, "Caja eliminada correctamente.");
            loadData();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar la caja.");
        }
    }
    
        private void retornar() {
        new Main().setVisible(true);
        setVisible(false);
    }

    private void seleccionarArea() {
        new SeleccionarAreaGUI(conn, txtIdArea).setVisible(true);
    }
        
    public static void main(String[] args) {
        // Aquí deberías establecer la conexión a tu base de datos
        // Ejemplo:
        ConexionBD conexion = new ConexionBD();
        Connection conn = conexion.establecerConexion();

        // Ejemplo básico para mostrar la GUI
        new CajaGUI(conn);
    }
}

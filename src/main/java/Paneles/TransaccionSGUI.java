package Paneles;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DAO.TransaccionDAO;
import Entidades.Transaccion;
import javafx.event.ActionEvent;
import Conexion.ConexionBD;

import java.awt.event.ActionListener;
import java.awt.*;

public class TransaccionSGUI extends JFrame {

    private TransaccionDAO transaccionDAO;
    private int idDoc;
    private JTextField txtIdTransaccion, txtIdDocumento, txtIdEstado, txtFechaTransaccion, txtDescripcion;
    private JComboBox<String> cbTipoTransaccion;
    Connection conn;

    public TransaccionSGUI(Connection conexion, int idDocumento) {
        this.transaccionDAO = new TransaccionDAO(conexion);
        conn = conexion;
        this.idDoc = idDocumento;
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Gestión de Transacciones");

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("ID Documento:"), gbc);

        txtIdDocumento = new JTextField(15);
        txtIdDocumento.setText(String.valueOf(idDoc));
        txtIdDocumento.setEditable(false);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelForm.add(txtIdDocumento, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("ID Estado:"), gbc);

        txtIdEstado = new JTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelForm.add(txtIdEstado, gbc);

        JButton btnSeleccionarEstado = new JButton("Seleccionar");
        btnSeleccionarEstado.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    seleccionarEstado();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        panelForm.add(btnSeleccionarEstado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;

        panelForm.add(
                new JLabel("Fecha Transacción (YYYY-MM-DD):"), gbc);

        txtFechaTransaccion = new JTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 1.0;

        panelForm.add(txtFechaTransaccion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;

        panelForm.add(
                new JLabel("Tipo Transacción:"), gbc);

        cbTipoTransaccion = new JComboBox<>(new String[] { "Deposito", "Retiro", "Pago", "Compra", "Venta" });
        gbc.gridx = 1;
        gbc.weightx = 1.0;

        panelForm.add(cbTipoTransaccion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;

        panelForm.add(
                new JLabel("Descripción:"), gbc);

        txtDescripcion = new JTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 1.0;

        panelForm.add(txtDescripcion, gbc);

        // Adding buttons
        gbc.gridx = 0;
        gbc.gridy = 6;
        JButton btnInsertar = new JButton("Insertar");
        btnInsertar.addActionListener(e-> insertarTransaccion());
        panelForm.add(btnInsertar, gbc);

        add(panelForm, BorderLayout.CENTER);
    }

    private void insertarTransaccion() {
        try {
            int idDocumento = this.idDoc;
            int idEstado = Integer.parseInt(txtIdEstado.getText());
            Date fechaTransaccion = Date.valueOf(txtFechaTransaccion.getText());
            String tipoTransaccion = cbTipoTransaccion.getSelectedItem().toString();
            String descripcion = txtDescripcion.getText();
            double monto = Double.parseDouble(obtenerMonto(idDocumento));

            Transaccion transaccion = new Transaccion(idDocumento, idEstado, fechaTransaccion, tipoTransaccion,
                    descripcion);
            transaccionDAO.insertar(transaccion);
            try {
                actualizarMontoCaja(idDocumento, tipoTransaccion, monto);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al actualizar el monto en la caja.");
            }
            JOptionPane.showMessageDialog(this, "Transacción insertada correctamente.");
            new DocumentoGUI(conn).setVisible(true);
            setVisible(false);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al insertar la transacción.");
        }
    }

    private void limpiarInputs() {
        txtIdTransaccion.setText("");
        txtIdDocumento.setText("");
        txtIdEstado.setText("");
        txtFechaTransaccion.setText("");
        cbTipoTransaccion.setSelectedIndex(0);
        txtDescripcion.setText("");
    }

    private void actualizarMontoCaja(int idDocumento, String tipoTransaccion, double montoDocumento)
            throws SQLException {
        double montoCaja = obtenerSaldoCaja(idDocumento);
        double montoActualizado = montoCaja;

        switch (tipoTransaccion) {
            case "Deposito":
                montoActualizado += montoDocumento;
                break;
            case "Retiro":
            case "Pago":
            case "Compra":
                montoActualizado -= montoDocumento;
                break;
            case "Venta":
                montoActualizado += montoDocumento;
                break;
            default:
                break;
        }

        String sql = "UPDATE Caja SET monto = ? WHERE idCaja = ?";
        try (PreparedStatement pst = this.conn.prepareStatement(sql)) {
            pst.setDouble(1, montoActualizado);
            pst.setInt(2, obtenerIdCajaPorDocumento(idDocumento));
            pst.executeUpdate();
        }
    }

    private int obtenerIdCajaPorDocumento(int idDocumento) throws SQLException {
        String sql = "SELECT Caja_idCaja FROM Documento WHERE idDocumento = ?";
        try (PreparedStatement pst = this.conn.prepareStatement(sql)) {
            pst.setInt(1, idDocumento);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Caja_idCaja");
                }
                return 0;
            }
        }
    }

    private double obtenerSaldoCaja(int idDocumento) throws SQLException {
        String sql = "SELECT monto FROM Caja WHERE idCaja = (SELECT Caja_idCaja FROM Documento WHERE idDocumento = ?)";
        try (PreparedStatement pst = this.conn.prepareStatement(sql)) {
            pst.setInt(1, idDocumento);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("monto");
                }
                return 0.0;
            }
        }
    }

    public String obtenerMonto(int idDocumento) {
        String monto = "";
        String sql = "SELECT monto FROM Documento WHERE idDocumento = ?";
        try (PreparedStatement pst = this.conn.prepareStatement(sql)) {
            pst.setInt(1, idDocumento);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    monto = rs.getString("monto");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monto;
    }

    private void seleccionarEstado() throws SQLException {
        new SeleccionarEstadoGUI(conn, txtIdEstado).setVisible(true);
    }


}

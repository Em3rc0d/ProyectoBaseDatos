package Paneles;

import Conexion.ConexionBD;
import DAO.DocumentoDAO;
import DAO.TransaccionDAO;
import Entidades.Documento;
import Entidades.Transaccion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TransaccionGUI extends JFrame {

    private TransaccionDAO transaccionDAO;
    private JTextField txtIdTransaccion, txtIdDocumento, txtIdEstado, txtFechaTransaccion, txtDescripcion;
    private JComboBox<String> cbTipoTransaccion;
    private JTable tableTransacciones;
    private DefaultTableModel model;
    Connection conn;
    protected int selectedIdTransaccion;

    public TransaccionGUI(Connection conexion) {
        this.transaccionDAO = new TransaccionDAO(conexion);
        conn = conexion;
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Gestión de Transacciones");

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels y campos de texto para la entrada de datos

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("ID Transaccion:"), gbc);

        txtIdTransaccion = new JTextField();
        txtIdTransaccion.setEditable(false);
        gbc.gridx = 1;
        panelForm.add(txtIdTransaccion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("ID Documento:"), gbc);

        txtIdDocumento = new JTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelForm.add(txtIdDocumento, gbc);

        JButton btnSeleccionarDocumento = new JButton("Seleccionar");
        btnSeleccionarDocumento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarDocumento();
            }
        });
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        panelForm.add(btnSeleccionarDocumento, gbc);

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
            public void actionPerformed(ActionEvent e) {
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
        panelForm.add(new JLabel("Fecha Transacción (YYYY-MM-DD):"), gbc);

        txtFechaTransaccion = new JTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelForm.add(txtFechaTransaccion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panelForm.add(new JLabel("Tipo Transacción:"), gbc);

        cbTipoTransaccion = new JComboBox<>(new String[]{"Deposito", "Retiro", "Pago", "Compra", "Venta"});
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelForm.add(cbTipoTransaccion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panelForm.add(new JLabel("Descripción:"), gbc);

        txtDescripcion = new JTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelForm.add(txtDescripcion, gbc);

        // Botones para insertar, actualizar y eliminar
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        // JButton btnInsertar = new JButton("Insertar");
        // btnInsertar.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         insertarTransaccion();
        //         limpiarInputs();
        //     }
        // });
        // panelForm.add(btnInsertar, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTransaccion();
                limpiarInputs();
            }
        });
        panelForm.add(btnActualizar, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarTransaccion();
                limpiarInputs();
            }
        });
        panelForm.add(btnEliminar, gbc);

        // Configuración de la tabla de transacciones
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        model = new DefaultTableModel();
        model.addColumn("ID Transacción");
        model.addColumn("ID Documento");
        model.addColumn("ID Estado");
        model.addColumn("Fecha Transacción");
        model.addColumn("Tipo Transacción");
        model.addColumn("Monto");
        model.addColumn("Descripción");

        tableTransacciones = new JTable(model);
        panelForm.add(new JScrollPane(tableTransacciones), gbc);

        // Panel inferior con botones de acción y retorno
        JPanel panelSouth = new JPanel(new GridLayout(1, 1));

        JButton btnRetornar = new JButton("Retornar");
        btnRetornar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retornar();
            }
        });
        panelSouth.add(btnRetornar);

        tableTransacciones.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = tableTransacciones.getSelectedRow();
                txtIdTransaccion.setText(model.getValueAt(selectedRow, 0).toString());
                txtIdDocumento.setText(model.getValueAt(selectedRow, 1).toString());
                txtIdEstado.setText(model.getValueAt(selectedRow, 2).toString());
                txtFechaTransaccion.setText(model.getValueAt(selectedRow, 3).toString());
                cbTipoTransaccion.setSelectedItem(model.getValueAt(selectedRow, 4).toString());
                txtDescripcion.setText(model.getValueAt(selectedRow, 6).toString());
                selectedIdTransaccion = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            }
        });

        // Agregar paneles al JFrame principal
        add(panelForm, BorderLayout.CENTER);
        add(panelSouth, BorderLayout.SOUTH);

        // Configuraciones finales del JFrame
        setSize(800, 600); // Ajusta el tamaño según tus necesidades
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra la ventana al presionar la X
    }

    private void loadData() {
        model.setRowCount(0);
        List<Transaccion> transacciones = transaccionDAO.obtenerTodasTransacciones();
        for (Transaccion transaccion : transacciones) {
            model.addRow(new Object[]{
                transaccion.getIdTransaccion(),
                transaccion.getDocumento_idDocumento(),
                transaccion.getEstado_idEstado(),
                transaccion.getFechaTransaccion(),
                transaccion.getTipoTransaccion(),
                obtenerMonto(transaccion.getDocumento_idDocumento()),
                transaccion.getDescripcion()
            });
        }
    }

    private void insertarTransaccion() {
        try {
            int idDocumento = Integer.parseInt(txtIdDocumento.getText());
            int idEstado = Integer.parseInt(txtIdEstado.getText());
            Date fechaTransaccion = Date.valueOf(txtFechaTransaccion.getText());
            String tipoTransaccion = cbTipoTransaccion.getSelectedItem().toString();
            String descripcion = txtDescripcion.getText();
            double monto = Double.parseDouble(obtenerMonto(idDocumento));

            Transaccion transaccion = new Transaccion(idDocumento, idEstado, fechaTransaccion, tipoTransaccion, descripcion);
            transaccionDAO.insertar(transaccion);
            try {
                actualizarMontoCaja(idDocumento, tipoTransaccion, monto);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Transacción insertada correctamente.");
            loadData();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al insertar la transacción.");
        }
    }

    private void actualizarTransaccion() {
        try {
            int idTransaccion = Integer.parseInt(txtIdTransaccion.getText());
            int idDocumento = Integer.parseInt(txtIdDocumento.getText());
            int idEstado = Integer.parseInt(txtIdEstado.getText());
            Date fechaTransaccion = Date.valueOf(txtFechaTransaccion.getText());
            String tipoTransaccion = cbTipoTransaccion.getSelectedItem().toString();
            String descripcion = txtDescripcion.getText();

            Transaccion transaccion = new Transaccion(idTransaccion, idDocumento, idEstado, fechaTransaccion, tipoTransaccion, descripcion);
            transaccionDAO.actualizar(transaccion);
            JOptionPane.showMessageDialog(this, "Transacción actualizada correctamente.");
            try {
                actualizarMontoCaja(idDocumento,tipoTransaccion,Double.parseDouble(obtenerMonto(idDocumento)));
            } catch (SQLException e) {
                
                e.printStackTrace();
            }
            loadData();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar la transacción.");
        }
    }

    private void eliminarTransaccion() {
        try {
            int idTransaccion = Integer.parseInt(txtIdTransaccion.getText());
            Transaccion transaccion = new Transaccion(idTransaccion);
            int idDocumento = Integer.parseInt(txtIdDocumento.getText());
            transaccionDAO.eliminar(transaccion);
            try {
                revertirActualizacionCaja(idDocumento);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Transacción eliminada correctamente.");
            loadData();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar la transacción.");
        }
    }
    
    private void retornar() {
        new Main().setVisible(true);
        setVisible(false);
    }

    private void limpiarInputs() {
        txtIdTransaccion.setText("");
        txtIdDocumento.setText("");
        txtIdEstado.setText("");
        txtFechaTransaccion.setText("");
        cbTipoTransaccion.setSelectedIndex(0);
        txtDescripcion.setText("");
    }

    private void seleccionarDocumento(){
        new SeleccionarDocumentoGUI(conn, txtIdDocumento).setVisible(true);
    }

    private void seleccionarEstado() throws SQLException{
        new SeleccionarEstadoGUI(conn, txtIdEstado).setVisible(true);
    }

    private void actualizarMontoCaja(int idDocumento, String tipoTransaccion, double montoDocumento) throws SQLException {
        double montoCaja = obtenerSaldoCaja(idDocumento); // Obtener el saldo actual de la caja
        double montoActualizado = montoCaja;

        switch (tipoTransaccion) {
            case "Deposito":
                montoActualizado += montoDocumento;
                break;
            case "Retiro":
                montoActualizado -= montoDocumento;
                break;
            case "Pago":
                montoActualizado -= montoDocumento;
                break;
            case "Compra":
                montoActualizado -= montoDocumento;
                break;
            case "Venta":
                montoActualizado += montoDocumento;
                break;
            default:
                break;
        }

        if(txtIdEstado.getText().equals("3")){ {
            String sql = "UPDATE Caja SET monto = ? WHERE idCaja = ?";
            try (PreparedStatement pst = this.conn.prepareStatement(sql)) {
                pst.setDouble(1, montoActualizado);
                pst.setInt(2, obtenerIdCajaPorDocumento(idDocumento));
                pst.executeUpdate();
            }}
        }
    }

    private void revertirActualizacionCaja(int idDocumento) throws SQLException {
        // Obtener el monto de la transacción eliminada
        double montoDocumento = Double.parseDouble(obtenerMonto(idDocumento));

        // Obtener el saldo actual de la caja
        double montoCaja = obtenerSaldoCaja(idDocumento);

        // Revertir la actualización del monto en la caja
        double montoActualizado = montoCaja + montoDocumento;

        // Actualizar el monto en la base de datos
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
            pst.setInt(1, Integer.parseInt(txtIdDocumento.getText()));
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

    public static void main(String[] args) {
        // Aquí deberías establecer la conexión a tu base de datos
        // Ejemplo:
        ConexionBD conexion = new ConexionBD();
        Connection conn = conexion.establecerConexion();

        // Ejemplo básico para mostrar la GUI
        new TransaccionGUI(conn);
    }
}

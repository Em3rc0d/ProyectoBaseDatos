package Paneles;

import Conexion.ConexionBD;
import DAO.DocumentoDAO;
import Entidades.Documento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DocumentoGUI extends JFrame {

    private DocumentoDAO documentoDAO;
    private JTextField txtIdDocumento, txtIdCaja, txtIdCajero, txtIdEmpresa, txtIdMovimiento, txtTipoDocumento, txtDescripcion, txtMonto;
    private JTable tableDocumentos;
    private DefaultTableModel model;
    Connection conn;
    protected int selectedIdDocumento;

    public DocumentoGUI(Connection conexion) {
        conn = conexion;
        this.documentoDAO = new DocumentoDAO(conexion);
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Gestión de Documentos");
    
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("ID Documento:"), gbc);
        txtIdDocumento = new JTextField(15);
        txtIdDocumento.setEditable(false);
        gbc.gridx = 1;
        panelForm.add(txtIdDocumento, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("ID Caja:"), gbc);
        txtIdCaja = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtIdCaja, gbc);
    
        JButton btnSeleccionarCaja = new JButton("Seleccionar");
        btnSeleccionarCaja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarCaja();
            }
        });
        gbc.gridx = 2;
        panelForm.add(btnSeleccionarCaja, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("ID Cajero:"), gbc);
        txtIdCajero = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtIdCajero, gbc);
    
        JButton btnSeleccionarCajero = new JButton("Seleccionar");
        btnSeleccionarCajero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarCajero();
            }
        });
        gbc.gridx = 2;
        panelForm.add(btnSeleccionarCajero, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelForm.add(new JLabel("RUC:"), gbc);
        txtIdEmpresa = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtIdEmpresa, gbc);
    
        JButton btnSeleccionarEmpresa = new JButton("Seleccionar");
        btnSeleccionarEmpresa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarRUC();
            }
        });
        gbc.gridx = 2;
        panelForm.add(btnSeleccionarEmpresa, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelForm.add(new JLabel("ID Movimiento:"), gbc);
        txtIdMovimiento = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtIdMovimiento, gbc);

        JButton btnSeleccionarMotivo = new JButton("Seleccionar");
        btnSeleccionarMotivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarMotivo();
            }
        });
        gbc.gridx = 2;
        panelForm.add(btnSeleccionarMotivo, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelForm.add(new JLabel("Tipo Documento:"), gbc);
        txtTipoDocumento = new JTextField(15);
        txtTipoDocumento.setEditable(false);
        gbc.gridx = 1;
        panelForm.add(txtTipoDocumento, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 6;
        panelForm.add(new JLabel("Descripción:"), gbc);
        txtDescripcion = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtDescripcion, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 7;
        panelForm.add(new JLabel("Monto:"), gbc);
        txtMonto = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtMonto, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        JButton btnInsertar = new JButton("Insertar");
        btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarDocumento();
                limpiarInputs();
            }
        });
        panelForm.add(btnInsertar, gbc);
    
        gbc.gridy = 9;
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDocumento();
                limpiarInputs();
            }
        });
        panelForm.add(btnActualizar, gbc);
    
        gbc.gridy = 10;
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarDocumento();
                limpiarInputs();
            }
        });
        panelForm.add(btnEliminar, gbc);
    
        model = new DefaultTableModel();
        model.addColumn("ID Documento");
        model.addColumn("ID Caja");
        model.addColumn("ID Cajero");
        model.addColumn("RUC");
        model.addColumn("ID Movimiento");
        model.addColumn("Tipo Documento");
        model.addColumn("Descripción");
        model.addColumn("Monto");
    
        tableDocumentos = new JTable(model);
        add(new JScrollPane(tableDocumentos), BorderLayout.CENTER);
    
        JPanel panelSouth = new JPanel(new GridLayout(1, 2));
    
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
    
        tableDocumentos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = tableDocumentos.getSelectedRow();
                txtIdDocumento.setText(model.getValueAt(selectedRow, 0).toString());
                txtIdCaja.setText(model.getValueAt(selectedRow, 1).toString());
                txtIdCajero.setText(model.getValueAt(selectedRow, 2).toString());
                txtIdEmpresa.setText(model.getValueAt(selectedRow, 3).toString());
                txtIdMovimiento.setText(model.getValueAt(selectedRow, 4).toString());
                txtTipoDocumento.setText(model.getValueAt(selectedRow, 5).toString());
                txtDescripcion.setText(model.getValueAt(selectedRow, 6).toString());
                txtMonto.setText(model.getValueAt(selectedRow, 7).toString());
                selectedIdDocumento = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            }
        });
    
        add(panelSouth, BorderLayout.SOUTH);
        add(panelForm, BorderLayout.WEST);
    }
    


    private void loadData() {
        model.setRowCount(0);
        try {
            List<Documento> documentos = documentoDAO.obtenerDocumentos();
            for (Documento documento : documentos) {
                model.addRow(new Object[]{
                    documento.getIdDocumento(),
                    documento.getIdCaja(),
                    documento.getIdCajero(),
                    documento.getIdEmpresa(),
                    documento.getIdMotivo(),
                    obtenerTipoMov(documento.getIdMotivo()),
                    documento.getDescripcion(),
                    documento.getMonto()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarDocumento() {
        try {
            int idCaja = Integer.parseInt(txtIdCaja.getText());
            int idCajero = Integer.parseInt(txtIdCajero.getText());
            String RUC = txtIdEmpresa.getText();
            int idMotivo = Integer.parseInt(txtIdMovimiento.getText());
            String descripcion = txtDescripcion.getText();
            float monto = Float.parseFloat(txtMonto.getText());

            Documento documento = new Documento(idCaja, idCajero, RUC, idMotivo, descripcion, monto);
            documentoDAO.insertar(documento);
            JOptionPane.showMessageDialog(this, "Documento insertado correctamente.");
            loadData();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al insertar el documento.");
        }
    }

    private void actualizarDocumento() {
        try {
            int idDocumento = Integer.parseInt(txtIdDocumento.getText());
            int idCaja = Integer.parseInt(txtIdCaja.getText());
            int idCajero = Integer.parseInt(txtIdCajero.getText());
            String RUC = txtIdEmpresa.getText();
            int idMovimiento = Integer.parseInt(txtIdMovimiento.getText());
            String descripcion = txtDescripcion.getText();
            float monto = Float.parseFloat(txtMonto.getText());

            Documento documento = new Documento(idCaja, idCajero, RUC, idMovimiento, descripcion, monto);
            documento.setIdDocumento(idDocumento);
            documentoDAO.actualizar(documento);
            JOptionPane.showMessageDialog(this, "Documento actualizado correctamente.");
            loadData(); // Método para recargar los datos en tu interfaz
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el documento.");
        }
    }

    private void eliminarDocumento() {
        try {
            int idDocumento = Integer.parseInt(txtIdDocumento.getText());
            documentoDAO.eliminar(idDocumento);
            JOptionPane.showMessageDialog(this, "Documento eliminado correctamente.");
            loadData();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar el documento.");
        }
    }
    
    private void retornar() {
        new Main().setVisible(true);
        setVisible(false);
    }

    private void limpiarInputs() {
        txtIdDocumento.setText("");
        txtIdCaja.setText("");
        txtIdCajero.setText("");
        txtIdEmpresa.setText("");
        txtIdMovimiento.setText("");
        txtTipoDocumento.setText("");
        txtDescripcion.setText("");
        txtMonto.setText("");
    }

    private void seleccionarCaja() {
        new SeleccionarCajaGUI(conn, txtIdCaja).setVisible(true);
    }
    
    private void seleccionarCajero() {
        new SeleccionarCajeroGUI(conn, txtIdCajero).setVisible(true);
    }

    private void seleccionarRUC() {
        new SeleccionarEmpresaReceptoraGUI(conn, txtIdEmpresa).setVisible(true);
    }

    private void seleccionarMotivo() {
        new SeleccionarMovimientoGUI(conn, txtIdMovimiento, txtTipoDocumento).setVisible(true);
    }
    
    public String obtenerTipoMov(int Movimiento_idMovimiento) throws SQLException {
        String sql = "SELECT * FROM Movimiento WHERE idMovimiento = ?";
        try (PreparedStatement pst = this.conn.prepareStatement(sql)) {
            pst.setInt(1, Movimiento_idMovimiento);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tipoMovimiento");
                }
                return null;
            }
        }
    }
    public static void main(String[] args) {
        // Aquí deberías establecer la conexión a tu base de datos
        // Ejemplo:
        ConexionBD conexion = new ConexionBD();
        Connection conn = conexion.establecerConexion();

        // Ejemplo básico para mostrar la GUI
        new DocumentoGUI(conn);
    }
}

package Paneles;

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

public class SeleccionarDocumentoGUI extends JFrame {

    private DocumentoDAO documentoDAO;
    private JTable tableDocumentos;
    private DefaultTableModel model;
    private JTextField txtIdDocumento;
    Connection conn;

    public SeleccionarDocumentoGUI(Connection conexion, JTextField txtIdDocumento) {
        this.documentoDAO = new DocumentoDAO(conexion);
        conn = conexion;
        this.txtIdDocumento = txtIdDocumento;
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Seleccionar Documento");

        model = new DefaultTableModel();
        model.addColumn("ID Documento");
        model.addColumn("ID Caja");
        model.addColumn("ID Cajero");
        model.addColumn("ID Empresa");
        model.addColumn("ID Motivo");
        model.addColumn("Tipo Documento");
        model.addColumn("Descripción");
        model.addColumn("Monto");

        tableDocumentos = new JTable(model);
        add(new JScrollPane(tableDocumentos), BorderLayout.CENTER);

        JPanel panelSouth = new JPanel(new GridLayout(1, 1));

        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarDocumento();
            }
        });
        panelSouth.add(btnSeleccionar);

        add(panelSouth, BorderLayout.SOUTH);
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

    private void seleccionarDocumento() {
        int selectedRow = tableDocumentos.getSelectedRow();
        if (selectedRow != -1) {
            String idDocumento = model.getValueAt(selectedRow, 0).toString();
            txtIdDocumento.setText(idDocumento);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un documento de la tabla.");
        }
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
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Ejemplo de uso:
                // Connection conexion = obtenerConexion(); // Método para obtener la conexión a la base de datos
                // JTextField txtIdDocumento = new JTextField();
                // new SeleccionarDocumentoGUI(conexion, txtIdDocumento);
            }
        });
    }
}

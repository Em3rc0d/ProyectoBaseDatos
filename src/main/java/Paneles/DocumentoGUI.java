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
import java.sql.SQLException;
import java.util.List;

public class DocumentoGUI extends JFrame {

    private DocumentoDAO documentoDAO;
    private JTextField txtIdDocumento, txtIdCaja, txtIdCajero, txtIdEmpresa, txtIdMotivo, txtTipoDocumento, txtDescripcion, txtMonto;
    private JTable tableDocumentos;
    private DefaultTableModel model;

    public DocumentoGUI(Connection conexion) {
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
        panelForm.add(new JLabel("ID Caja:"), gbc);
        txtIdCaja = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtIdCaja, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("ID Cajero:"), gbc);
        txtIdCajero = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtIdCajero, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("ID Empresa:"), gbc);
        txtIdEmpresa = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtIdEmpresa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelForm.add(new JLabel("ID Motivo:"), gbc);
        txtIdMotivo = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtIdMotivo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panelForm.add(new JLabel("Tipo Documento:"), gbc);
        txtTipoDocumento = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtTipoDocumento, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panelForm.add(new JLabel("Descripción:"), gbc);
        txtDescripcion = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtDescripcion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panelForm.add(new JLabel("Monto:"), gbc);
        txtMonto = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtMonto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JButton btnInsertar = new JButton("Insertar");
        btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarDocumento();
            }
        });
        panelForm.add(btnInsertar, gbc);

        gbc.gridy = 8;
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDocumento();
            }
        });
        panelForm.add(btnActualizar, gbc);

        gbc.gridy = 9;
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarDocumento();
            }
        });
        panelForm.add(btnEliminar, gbc);

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
                    documento.getTipoDocumento(),
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
            int idMotivo = Integer.parseInt(txtIdMotivo.getText());
            String tipoDocumento = txtTipoDocumento.getText();
            String descripcion = txtDescripcion.getText();
            float monto = Float.parseFloat(txtMonto.getText());

            Documento documento = new Documento(idCaja, idCajero, RUC, idMotivo, tipoDocumento, descripcion, monto);
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
            int idCaja = Integer.parseInt(txtIdCaja.getText());
            int idCajero = Integer.parseInt(txtIdCajero.getText());
            String RUC = txtIdEmpresa.getText();
            int idMotivo = Integer.parseInt(txtIdMotivo.getText());
            String tipoDocumento = txtTipoDocumento.getText();
            String descripcion = txtDescripcion.getText();
            float monto = Float.parseFloat(txtMonto.getText());

            Documento documento = new Documento(idCaja, idCajero, RUC, idMotivo, tipoDocumento, descripcion, monto);
            documentoDAO.actualizar(documento);
            JOptionPane.showMessageDialog(this, "Documento actualizado correctamente.");
            loadData();
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

    public static void main(String[] args) {
        // Aquí deberías establecer la conexión a tu base de datos
        // Ejemplo:
        ConexionBD conexion = new ConexionBD();
        Connection conn = conexion.establecerConexion();

        // Ejemplo básico para mostrar la GUI
        new DocumentoGUI(conn);
    }
}

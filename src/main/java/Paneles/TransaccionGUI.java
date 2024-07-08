package Paneles;

import Conexion.ConexionBD;
import DAO.TransaccionDAO;
import Entidades.Transaccion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class TransaccionGUI extends JFrame {

    private TransaccionDAO transaccionDAO;
    private JTextField txtIdTransaccion, txtIdDocumento, txtIdEstado, txtFechaTransaccion, txtTipoTransaccion, txtDescripcion;
    private JTable tableTransacciones;
    private DefaultTableModel model;

    public TransaccionGUI(Connection conexion) {
        this.transaccionDAO = new TransaccionDAO(conexion);
        initComponents();
        loadData();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Gestión de Transacciones");

        JPanel panelForm = new JPanel(new GridLayout(7, 2));
        
        panelForm.add(new JLabel("ID Documento:"));
        txtIdDocumento = new JTextField();
        panelForm.add(txtIdDocumento);

        panelForm.add(new JLabel("ID Estado:"));
        txtIdEstado = new JTextField();
        panelForm.add(txtIdEstado);

        panelForm.add(new JLabel("Fecha Transacción (YYYY-MM-DD):"));
        txtFechaTransaccion = new JTextField();
        panelForm.add(txtFechaTransaccion);

        panelForm.add(new JLabel("Tipo Transacción:"));
        txtTipoTransaccion = new JTextField();
        panelForm.add(txtTipoTransaccion);

        panelForm.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField();
        panelForm.add(txtDescripcion);

        JButton btnInsertar = new JButton("Insertar");
        btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarTransaccion();
            }
        });
        panelForm.add(btnInsertar);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTransaccion();
            }
        });
        panelForm.add(btnActualizar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarTransaccion();
            }
        });
        panelForm.add(btnEliminar);

        model = new DefaultTableModel();
        model.addColumn("ID Transacción");
        model.addColumn("ID Documento");
        model.addColumn("ID Estado");
        model.addColumn("Fecha Transacción");
        model.addColumn("Tipo Transacción");
        model.addColumn("Descripción");

        tableTransacciones = new JTable(model);
        add(new JScrollPane(tableTransacciones), BorderLayout.CENTER);

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
        List<Transaccion> transacciones = transaccionDAO.obtenerTodasTransacciones();
        for (Transaccion transaccion : transacciones) {
            model.addRow(new Object[]{
                transaccion.getIdTransaccion(),
                transaccion.getDocumento_idDocumento(),
                transaccion.getEstado_idEstado(),
                transaccion.getFechaTransaccion(),
                transaccion.getTipoTransaccion(),
                transaccion.getDescripcion()
            });
        }
    }

    private void insertarTransaccion() {
        try {
            int idDocumento = Integer.parseInt(txtIdDocumento.getText());
            int idEstado = Integer.parseInt(txtIdEstado.getText());
            Date fechaTransaccion = Date.valueOf(txtFechaTransaccion.getText());
            String tipoTransaccion = txtTipoTransaccion.getText();
            String descripcion = txtDescripcion.getText();

            Transaccion transaccion = new Transaccion(idDocumento, idEstado, fechaTransaccion, tipoTransaccion, descripcion);
            transaccionDAO.insertar(transaccion);
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
            String tipoTransaccion = txtTipoTransaccion.getText();
            String descripcion = txtDescripcion.getText();

            Transaccion transaccion = new Transaccion(idDocumento, idEstado, fechaTransaccion, tipoTransaccion, descripcion);
            transaccionDAO.actualizar(transaccion);
            JOptionPane.showMessageDialog(this, "Transacción actualizada correctamente.");
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
            transaccionDAO.eliminar(transaccion);
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

    public static void main(String[] args) {
        // Aquí deberías establecer la conexión a tu base de datos
        // Ejemplo:
        ConexionBD conexion = new ConexionBD();
        Connection conn = conexion.establecerConexion();

        // Ejemplo básico para mostrar la GUI
        new TransaccionGUI(conn);
    }
}

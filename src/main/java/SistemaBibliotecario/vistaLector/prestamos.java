package SistemaBibliotecario.vistaLector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class prestamos extends javax.swing.JPanel {

    public prestamos() {
        initComponents();
        cargarPrestamos();
    }

    public void cargarPrestamos() {
        String dni = SistemaBibliotecario.Modelos.SesionActual.dni;
        if (dni != null) {
            SistemaBibliotecario.Dao.PrestamoDAO prestamoDAO = new SistemaBibliotecario.Dao.PrestamoDAO();
            java.util.List<Object[]> prestamos = prestamoDAO.obtenerPrestamosPorLector(dni);
            DefaultTableModel model = (DefaultTableModel) tblPrestamosActuales.getModel();
            model.setRowCount(0); // Limpiar tabla
            for (Object[] prestamo : prestamos) {
                model.addRow(prestamo);
            }
        }
    }

    private void initComponents() {
        // Main Panel Setup
        setLayout(new java.awt.BorderLayout(10, 10));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(java.awt.Color.WHITE);

        // Title
        jLabel7 = new javax.swing.JLabel("TUS PRESTAMOS ACTUALES");
        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        add(jLabel7, java.awt.BorderLayout.NORTH);

        // Table
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPrestamosActuales = new javax.swing.JTable();

        tblPrestamosActuales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"LIBRO", "FECHA PRESTAMO", "VENCIMIENTO"}
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPrestamosActuales);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        // Bottom panel for buttons
        jPanelButtons = new javax.swing.JPanel();
        jPanelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jPanelButtons.setBackground(java.awt.Color.WHITE);

        btnRenovar = new javax.swing.JButton("RENOVAR");
        btnVerDetalle = new javax.swing.JButton("VER DETALLE");
        
        jPanelButtons.add(btnRenovar);
        jPanelButtons.add(btnVerDetalle);

        add(jPanelButtons, java.awt.BorderLayout.SOUTH);

        // Action Listeners
        btnRenovar.addActionListener(evt -> {
            int selectedRow = tblPrestamosActuales.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un préstamo para renovar.");
                return;
            }

            String tituloLibro = String.valueOf(tblPrestamosActuales.getValueAt(selectedRow, 0));
            String dni = SistemaBibliotecario.Modelos.SesionActual.dni;
            if (dni == null || dni.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontró la sesión del usuario.");
                return;
            }

            SistemaBibliotecario.Dao.DAO_LECTOR_MQ daoLector = new SistemaBibliotecario.Dao.DAO_LECTOR_MQ();
            int idUsuario = daoLector.obtenerIdUsuarioPorDni(dni);
            if (idUsuario == -1) {
                JOptionPane.showMessageDialog(this, "No se pudo identificar al usuario.");
                return;
            }

            boolean renovado = daoLector.renovarPrestamoPorTitulo(idUsuario, tituloLibro);
            if (renovado) {
                JOptionPane.showMessageDialog(this, "Renovación exitosa por 7 días desde hoy.");
                cargarPrestamos();
            } else {
                JOptionPane.showMessageDialog(this, "No es posible renovar. Verifique que el préstamo no haya vencido.");
            }
        });

        btnVerDetalle.addActionListener(evt -> {
            int selectedRow = tblPrestamosActuales.getSelectedRow();
            if (selectedRow >= 0) {
                JOptionPane.showMessageDialog(this, "Funcionalidad de ver detalle no implementada.");
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un préstamo para ver su detalle.");
            }
        });
    }

    // Variables declaration
    private javax.swing.JButton btnRenovar;
    private javax.swing.JButton btnVerDetalle;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPrestamosActuales;
    // End of variables declaration
}

package SistemaBibliotecario.vistaLector;

public class historial extends javax.swing.JPanel {

    public historial() {
        initComponents();
        cargarHistorial();
    }

    private void cargarHistorial() {
        String dni = SistemaBibliotecario.Modelos.SesionActual.dni;
        if (dni != null) {
            SistemaBibliotecario.Dao.PrestamoDAO prestamoDAO = new SistemaBibliotecario.Dao.PrestamoDAO();
            java.util.List<Object[]> historial = prestamoDAO.obtenerHistorialPorLector(dni);
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblHistorial.getModel();
            model.setRowCount(0); // Limpiar tabla
            for (Object[] prestamo : historial) {
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
        jLabel7 = new javax.swing.JLabel("HISTORIAL DE LECTURA");
        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        add(jLabel7, java.awt.BorderLayout.NORTH);

        // Table
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHistorial = new javax.swing.JTable();

        tblHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"LIBRO", "FECHA PRÉSTAMO", "FECHA DEVOLUCIÓN", "ESTADO"}
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblHistorial);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        // Bottom panel for buttons
        jPanelButtons = new javax.swing.JPanel();
        jPanelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jPanelButtons.setBackground(java.awt.Color.WHITE);

        btnEliminar = new javax.swing.JButton("ELIMINAR DEL HISTORIAL");
        jPanelButtons.add(btnEliminar);

        add(jPanelButtons, java.awt.BorderLayout.SOUTH);

        // Action Listener
        btnEliminar.addActionListener(evt -> {
            int selectedRow = tblHistorial.getSelectedRow();
            if (selectedRow >= 0) {
                // Lógica para eliminar del historial
                javax.swing.JOptionPane.showMessageDialog(this, "Funcionalidad de eliminar no implementada.");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.");
            }
        });
    }

    // Variables declaration
    private javax.swing.JButton btnEliminar;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblHistorial;
    // End of variables declaration
}
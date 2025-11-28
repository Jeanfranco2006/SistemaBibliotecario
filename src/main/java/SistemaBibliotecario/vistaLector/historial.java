package SistemaBibliotecario.vistaLector;

import javax.swing.JPanel;

public class historial extends javax.swing.JPanel {

    public historial() {
        initComponents();
        cargarHistorial();
    }

    // Sobrescribir el método cuando se hace visible
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            // Actualizar cada vez que se muestra el panel
            cargarHistorial();
        }
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
            
            // Opcional: mostrar mensaje de cuántos registros se cargaron
            System.out.println("Historial actualizado: " + historial.size() + " registros");
        }
    }
    
    private void actualizarContador() {
        int totalRegistros = tblHistorial.getRowCount();
        lblContador.setText("Total de registros: " + totalRegistros);
    }

    private void initComponents() {
        // Main Panel Setup
        setLayout(new java.awt.BorderLayout(10, 10));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(java.awt.Color.WHITE);

        // Title Panel
        JPanel titlePanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        titlePanel.setBackground(java.awt.Color.WHITE);
        
        // Title
        jLabel7 = new javax.swing.JLabel("HISTORIAL DE LECTURA");
        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titlePanel.add(jLabel7, java.awt.BorderLayout.NORTH);
        
        // Contador
        lblContador = new javax.swing.JLabel("Total de registros: 0");
        lblContador.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblContador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblContador.setForeground(java.awt.Color.GRAY);
        titlePanel.add(lblContador, java.awt.BorderLayout.SOUTH);
        
        add(titlePanel, java.awt.BorderLayout.NORTH);

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
        
        // Mejorar la apariencia de la tabla
        tblHistorial.setRowHeight(25);
        tblHistorial.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 12));
        tblHistorial.getTableHeader().setBackground(new java.awt.Color(240, 240, 240));
        jScrollPane1.setViewportView(tblHistorial);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        // Bottom panel for buttons
        jPanelButtons = new javax.swing.JPanel();
        jPanelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jPanelButtons.setBackground(java.awt.Color.WHITE);

        // Botón Actualizar
        btnActualizar = new javax.swing.JButton("ACTUALIZAR");
        btnActualizar.setBackground(new java.awt.Color(70, 130, 180));
        btnActualizar.setForeground(java.awt.Color.WHITE);
        btnActualizar.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnActualizar.addActionListener(evt -> cargarHistorial());
        jPanelButtons.add(btnActualizar);

        btnEliminar = new javax.swing.JButton("ELIMINAR DEL HISTORIAL");
        btnEliminar.setBackground(new java.awt.Color(220, 53, 69));
        btnEliminar.setForeground(java.awt.Color.WHITE);
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jPanelButtons.add(btnEliminar);

        add(jPanelButtons, java.awt.BorderLayout.SOUTH);

        // Action Listener para eliminar
        btnEliminar.addActionListener(evt -> eliminarDelHistorial());
    }

    private void eliminarDelHistorial() {
        int selectedRow = tblHistorial.getSelectedRow();
        if (selectedRow >= 0) {
            // Confirmar eliminación
            int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
                this, 
                "¿Está seguro de que desea eliminar este registro del historial?", 
                "Confirmar eliminación", 
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            
            if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
                try {
                    // Obtener datos del registro seleccionado
                    String libro = tblHistorial.getValueAt(selectedRow, 0).toString();
                    String fechaPrestamo = tblHistorial.getValueAt(selectedRow, 1).toString();
                    
                    // Obtener DNI del usuario actual
                    String dni = SistemaBibliotecario.Modelos.SesionActual.dni;
                    
                    // Eliminar de la base de datos
                    SistemaBibliotecario.Dao.PrestamoDAO prestamoDAO = new SistemaBibliotecario.Dao.PrestamoDAO();
                    boolean eliminado = prestamoDAO.eliminarDelHistorial(dni, libro, fechaPrestamo);
                    
                    if (eliminado) {
                        // Eliminar de la tabla
                        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblHistorial.getModel();
                        model.removeRow(selectedRow);
                        
                        // Actualizar contador
                        actualizarContador();
                        
                        javax.swing.JOptionPane.showMessageDialog(
                            this, 
                            "Registro eliminado del historial correctamente.", 
                            "Éxito", 
                            javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(
                            this, 
                            "Error al eliminar el registro del historial.", 
                            "Error", 
                            javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                    }
                    
                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(
                        this, 
                        "Error al eliminar el registro: " + e.getMessage(), 
                        "Error", 
                        javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                this, 
                "Seleccione un registro para eliminar.", 
                "Advertencia", 
                javax.swing.JOptionPane.WARNING_MESSAGE
            );
        }
    }
    
    // Método público para actualizar desde otras clases
    public void actualizarHistorial() {
        cargarHistorial();
    }

    // Variables declaration
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lblContador;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblHistorial;
    // End of variables declaration
}
package SistemaBibliotecario.vistaLector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class prestamos extends JPanel {

    private JTable tblPrestamosActuales;
    private JScrollPane jScrollPane1;
    private JButton btnRenovar;
    private JButton btnVerDetalle;
    private JButton btnRefrescar;
    private JLabel lblEstadisticas;
    private JLabel jLabel7;

    public prestamos() {
        initComponents();
        cargarPrestamos();
    }

    // Sobrescribir el método cuando se hace visible
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            // Actualizar cada vez que se muestra el panel
            cargarPrestamos();
        }
    }

    public void cargarPrestamos() {
        String dni = SistemaBibliotecario.Modelos.SesionActual.dni;
        if (dni != null) {
            SistemaBibliotecario.Dao.PrestamoDAO prestamoDAO = new SistemaBibliotecario.Dao.PrestamoDAO();
            java.util.List<Object[]> prestamos = prestamoDAO.obtenerPrestamosPorLector(dni);
            DefaultTableModel model = (DefaultTableModel) tblPrestamosActuales.getModel();
            model.setRowCount(0);
            
            for (Object[] prestamo : prestamos) {
                model.addRow(prestamo);
            }
            
            actualizarEstadisticas(prestamos.size());
        }
    }

    private void initComponents() {
        // Main Panel Setup - Mismo diseño que historial
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Title Panel - Igual que historial
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        
        jLabel7 = new JLabel("MIS PRÉSTAMOS ACTUALES");
        jLabel7.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(jLabel7, BorderLayout.NORTH);
        
        // Panel de estadísticas y botón actualizar
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        statsPanel.setBackground(Color.WHITE);
        
        lblEstadisticas = new JLabel("Total: 0 préstamos activos");
        lblEstadisticas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstadisticas.setForeground(Color.GRAY);
        
        btnRefrescar = new JButton("🔄 ACTUALIZAR");
        btnRefrescar.setBackground(new Color(70, 130, 180));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnRefrescar.addActionListener(e -> cargarPrestamos());
        
        statsPanel.add(lblEstadisticas);
        statsPanel.add(btnRefrescar);
        titlePanel.add(statsPanel, BorderLayout.SOUTH);

        add(titlePanel, BorderLayout.NORTH);

        // Table - Mismo estilo que historial
        jScrollPane1 = new JScrollPane();
        tblPrestamosActuales = new JTable();
        
        tblPrestamosActuales.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"LIBRO", "FECHA PRÉSTAMO", "VENCIMIENTO"}
        ) {
            boolean[] canEdit = new boolean[] { false, false, false };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        
        // Configuración idéntica a historial
        tblPrestamosActuales.setRowHeight(25);
        tblPrestamosActuales.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblPrestamosActuales.setBackground(Color.WHITE);
        tblPrestamosActuales.setSelectionBackground(new Color(220, 230, 241));
        tblPrestamosActuales.setSelectionForeground(Color.BLACK);
        
        // Header idéntico a historial
        tblPrestamosActuales.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblPrestamosActuales.getTableHeader().setBackground(new Color(240, 240, 240));
        tblPrestamosActuales.getTableHeader().setForeground(Color.BLACK);
        
        jScrollPane1.setViewportView(tblPrestamosActuales);
        jScrollPane1.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        add(jScrollPane1, BorderLayout.CENTER);

        // Bottom panel for buttons - Mismo diseño que historial
        JPanel jPanelButtons = new JPanel();
        jPanelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPanelButtons.setBackground(Color.WHITE);

        btnVerDetalle = new JButton("📋 VER DETALLE");
        btnVerDetalle.setBackground(new Color(41, 128, 185));
        btnVerDetalle.setForeground(Color.WHITE);
        btnVerDetalle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVerDetalle.setFocusPainted(false);
        btnVerDetalle.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnVerDetalle.addActionListener(e -> verDetallePrestamo());
        jPanelButtons.add(btnVerDetalle);

        btnRenovar = new JButton("🔄 RENOVAR PRÉSTAMO");
        btnRenovar.setBackground(new Color(39, 174, 96));
        btnRenovar.setForeground(Color.WHITE);
        btnRenovar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRenovar.setFocusPainted(false);
        btnRenovar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnRenovar.addActionListener(e -> renovarPrestamo());
        jPanelButtons.add(btnRenovar);

        add(jPanelButtons, BorderLayout.SOUTH);
    }

    private void actualizarEstadisticas(int totalPrestamos) {
        lblEstadisticas.setText("Total: " + totalPrestamos + " préstamos activos");
    }

    private void renovarPrestamo() {
        int selectedRow = tblPrestamosActuales.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un préstamo para renovar.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tituloLibro = String.valueOf(tblPrestamosActuales.getValueAt(selectedRow, 0));
        String fechaVencimiento = String.valueOf(tblPrestamosActuales.getValueAt(selectedRow, 2));

        if (estaVencido(fechaVencimiento)) {
            JOptionPane.showMessageDialog(this,
                "No se puede renovar un préstamo vencido.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Renovar el préstamo de: " + tituloLibro + "?",
            "Confirmar Renovación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        String dni = SistemaBibliotecario.Modelos.SesionActual.dni;
        if (dni == null) {
            JOptionPane.showMessageDialog(this,
                "Error de sesión.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        SistemaBibliotecario.Dao.DAO_LECTOR_MQ daoLector = new SistemaBibliotecario.Dao.DAO_LECTOR_MQ();
        int idUsuario = daoLector.obtenerIdUsuarioPorDni(dni);
        
        if (idUsuario == -1) {
            JOptionPane.showMessageDialog(this,
                "No se pudo identificar al usuario.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean renovado = daoLector.renovarPrestamoPorTitulo(idUsuario, tituloLibro);
        
        if (renovado) {
            JOptionPane.showMessageDialog(this,
                "✅ Préstamo renovado correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            cargarPrestamos(); // Actualizar la tabla
        } else {
            JOptionPane.showMessageDialog(this,
                "❌ No se pudo renovar el préstamo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDetallePrestamo() {
        int selectedRow = tblPrestamosActuales.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un préstamo para ver detalles.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tituloLibro = String.valueOf(tblPrestamosActuales.getValueAt(selectedRow, 0));
        String fechaPrestamo = String.valueOf(tblPrestamosActuales.getValueAt(selectedRow, 1));
        String fechaVencimiento = String.valueOf(tblPrestamosActuales.getValueAt(selectedRow, 2));

        int diasRestantes = calcularDiasRestantes(fechaVencimiento);
        String estado = diasRestantes < 0 ? "VENCIDO" : diasRestantes == 0 ? "VENCE HOY" : "ACTIVO";
        Color colorEstado = diasRestantes < 0 ? Color.RED : diasRestantes <= 3 ? Color.ORANGE : new Color(39, 174, 96);

        // Diálogo con diseño consistente
        JDialog dialog = new JDialog();
        dialog.setTitle("Detalle del Préstamo");
        dialog.setModal(true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        contentPanel.setBackground(Color.WHITE);

        // Header
        JLabel title = new JLabel("📖 DETALLE DEL PRÉSTAMO");
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        title.setForeground(new Color(19, 38, 76));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        contentPanel.add(title, BorderLayout.NORTH);

        // Panel de información
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Estado
        JPanel estadoPanel = crearFilaInformacion("📊 ESTADO:", estado, colorEstado);
        infoPanel.add(estadoPanel);

        // Libro
        JPanel libroPanel = crearFilaInformacion("📚 LIBRO:", tituloLibro, Color.BLACK);
        infoPanel.add(libroPanel);

        // Fecha préstamo
        JPanel fechaPrestamoPanel = crearFilaInformacion("📅 FECHA PRÉSTAMO:", fechaPrestamo, Color.BLACK);
        infoPanel.add(fechaPrestamoPanel);

        // Fecha vencimiento
        JPanel fechaVencimientoPanel = crearFilaInformacion("⏰ FECHA VENCIMIENTO:", fechaVencimiento, Color.BLACK);
        infoPanel.add(fechaVencimientoPanel);

        // Días restantes
        String textoDias = diasRestantes < 0 ? 
            Math.abs(diasRestantes) + " días de retraso" : 
            diasRestantes + " días restantes";
        Color colorDias = diasRestantes < 0 ? Color.RED : diasRestantes <= 3 ? Color.ORANGE : new Color(39, 174, 96);
        
        JPanel diasPanel = crearFilaInformacion("📆 SITUACIÓN:", textoDias, colorDias);
        infoPanel.add(diasPanel);

        contentPanel.add(infoPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnCerrar = new JButton("✅ CERRAR");
        btnCerrar.setBackground(new Color(19, 38, 76));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnCerrar.addActionListener(e -> dialog.dispose());
        buttonPanel.add(btnCerrar);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private JPanel crearFilaInformacion(String etiqueta, String valor, Color colorValor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Color.BLACK);
        label.setPreferredSize(new Dimension(180, 25));
        
        JLabel value = new JLabel(valor);
        value.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        value.setForeground(colorValor);
        
        panel.add(label, BorderLayout.WEST);
        panel.add(value, BorderLayout.CENTER);
        
        return panel;
    }

    private int calcularDiasRestantes(String fechaVencimiento) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date vencimiento = sdf.parse(fechaVencimiento);
            Date hoy = new Date();
            
            long diferencia = vencimiento.getTime() - hoy.getTime();
            return (int) (diferencia / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            return 0;
        }
    }

    private boolean estaVencido(String fechaVencimiento) {
        return calcularDiasRestantes(fechaVencimiento) < 0;
    }
}
package SistemaBibliotecario.vistaLector;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import SistemaBibliotecario.Dao.DAO_LECTOR_MQ;
import SistemaBibliotecario.Modelos.SesionActual;

public class inicio extends javax.swing.JPanel {

    private DAO_LECTOR_MQ daoLector;

    // Paneles para la navegaciÃ³n
    private JPanel contentPanel;
    private JPanel inicioContentPanel;
    private libros librosPanel;
    private prestamos prestamosPanel;
    private historial historialPanel;

    public inicio() {
        initComponents();
        cargarDatosPrestamos();
        cargarRecomendados();

        // Inicializar los paneles de las otras vistas
        librosPanel = new libros();
        prestamosPanel = new prestamos();
        historialPanel = new historial();

        // Configurar el panel de contenido inicial y mostrarlo
        showPanel(inicioContentPanel);
    }

    private void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void setButtonSelected(javax.swing.JButton selectedButton) {
        // Colores para los estados de los botones
        Color selectedColor = new Color(0, 0, 0); // Negro para el seleccionado
        Color normalColor = new Color(0, 51, 102); // Azul oscuro para el normal

        // Restablecer todos los botones al color normal
        btnInicio.setBackground(normalColor);
        btnLibros.setBackground(normalColor);
        btnMisPrestamos.setBackground(normalColor);
        btnHistorial.setBackground(normalColor);

        // Establecer el color seleccionado para el botÃ³n activo
        selectedButton.setBackground(selectedColor);
    }

    private void cargarDatosPrestamos() {
        daoLector = new DAO_LECTOR_MQ();
        String dniUsuario = SesionActual.dni;
        if (dniUsuario != null && !dniUsuario.isEmpty()) {
            int idUsuario = daoLector.obtenerIdUsuarioPorDni(dniUsuario);
            if (idUsuario != -1) {
                DefaultTableModel model = daoLector.obtenerPrestamosActivos(idUsuario);
                if (tblTusPrestamos != null) {
                    tblTusPrestamos.setModel(model);
                }
            } else {
                if (tblTusPrestamos != null) {
                    tblTusPrestamos.setModel(new DefaultTableModel(null, new String[] { "LIBRO", "FECHA" }));
                }
            }
        } else {
            if (tblTusPrestamos != null) {
                tblTusPrestamos.setModel(new DefaultTableModel(null, new String[] { "LIBRO", "FECHA" }));
            }
        }
    }

    private void cargarRecomendados() {
        SistemaBibliotecario.Dao.LibroDAO libroDAO = new SistemaBibliotecario.Dao.LibroDAO();
        java.util.List<Object[]> recomendados = libroDAO.obtenerRecomendados();
        DefaultTableModel model = (DefaultTableModel) tblRecomendados.getModel();
        model.setRowCount(0); // Limpiar tabla
        for (Object[] libro : recomendados) {
            model.addRow(libro);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        // Usamos el panel 'this' como el contenedor principal con BorderLayout
        this.setLayout(new BorderLayout());
        this.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        // --- PANEL DEL MENÃš LATERAL (OESTE) ---
        jPanel2 = new javax.swing.JPanel();
        jPanel2.setBackground(new java.awt.Color(19, 38, 76));
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 700));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1 = new javax.swing.JLabel();
        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("BiblioSys");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, -1, -1));

        btnInicio = new javax.swing.JButton();
        btnInicio.setBackground(new java.awt.Color(0, 0, 0));
        btnInicio.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        btnInicio.setForeground(new java.awt.Color(255, 255, 255));
        btnInicio.setText("INICIO");
        jPanel2.add(btnInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 99, 240, 80));

        btnLibros = new javax.swing.JButton();
        btnLibros.setBackground(new java.awt.Color(0, 51, 102));
        btnLibros.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        btnLibros.setForeground(new java.awt.Color(255, 255, 255));
        btnLibros.setText("LIBROS");
        jPanel2.add(btnLibros, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 240, 80));

        btnMisPrestamos = new javax.swing.JButton();
        btnMisPrestamos.setBackground(new java.awt.Color(0, 51, 102));
        btnMisPrestamos.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        btnMisPrestamos.setForeground(new java.awt.Color(255, 255, 255));
        btnMisPrestamos.setText("MIS PRESTAMOS");
        jPanel2.add(btnMisPrestamos, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 240, 80));

        btnHistorial = new javax.swing.JButton();
        btnHistorial.setBackground(new java.awt.Color(0, 51, 102));
        btnHistorial.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        btnHistorial.setForeground(new java.awt.Color(255, 255, 255));
        btnHistorial.setText("HISTORIAL");
        jPanel2.add(btnHistorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 240, 80));

        btnSalir = new javax.swing.JButton();
        btnSalir.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        btnSalir.setText("SALIR");
        jPanel2.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 621, 240, 44));

        this.add(jPanel2, BorderLayout.WEST);

        // --- PANEL DE CONTENIDO PRINCIPAL (CENTRO) ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        this.add(centerPanel, BorderLayout.CENTER);

        // --- BARRA SUPERIOR ---
        jPanel3 = new javax.swing.JPanel();
        jPanel3.setBackground(new java.awt.Color(19, 38, 76));
        jPanel3.setPreferredSize(new java.awt.Dimension(900, 30));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2 = new javax.swing.JLabel();
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Inicio");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 8, -1, -1));
        centerPanel.add(jPanel3, BorderLayout.NORTH);

        // --- PANEL INTERCAMBIABLE ---
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        centerPanel.add(contentPanel, BorderLayout.CENTER);

        // --- CONTENIDO DE LA PANTALLA DE INICIO ---
        inicioContentPanel = new JPanel();
        inicioContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4 = new javax.swing.JLabel();
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo.jpeg"))); // NOI18N
        inicioContentPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 40, 110, 80));

        jLabel6 = new javax.swing.JLabel();
        jLabel6.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel6.setText("BIENVENIDO LUIS");
        inicioContentPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 40, -1, -1));

        jLabel7 = new javax.swing.JLabel();
        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel7.setText("TU ACTIVIDAD RECIENTE");
        inicioContentPanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        // Panel "Tus Prestamos"
        jPanel4 = new javax.swing.JPanel();
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5 = new javax.swing.JLabel();
        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel5.setText("TUS PRESTAMOS");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, -1));

        btnSolicitarRenovacion = new javax.swing.JButton();
        btnSolicitarRenovacion.setBackground(new java.awt.Color(19, 38, 76));
        btnSolicitarRenovacion.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        btnSolicitarRenovacion.setForeground(new java.awt.Color(255, 255, 255));
        btnSolicitarRenovacion.setText("SOLICITAR RENOVACION");
        jPanel4.add(btnSolicitarRenovacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 280, 50));

        jScrollPane1 = new javax.swing.JScrollPane();
        tblTusPrestamos = new javax.swing.JTable();
        tblTusPrestamos.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {}, new String[] { "LIBRO", "FECHA" }));
        jScrollPane1.setViewportView(tblTusPrestamos);
        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 340, 150));
        inicioContentPanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 380, 320));

        // Panel "Recomendados"
        jPanel5 = new javax.swing.JPanel();
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9 = new javax.swing.JLabel();
        jLabel9.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel9.setText("RECOMENDADOS PARA TI");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, -1, -1));

        btnVerLibros = new javax.swing.JButton();
        btnVerLibros.setBackground(new java.awt.Color(19, 38, 76));
        btnVerLibros.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        btnVerLibros.setForeground(new java.awt.Color(255, 255, 255));
        btnVerLibros.setText("VER MAS LIBROS");
        jPanel5.add(btnVerLibros, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 240, 200, 50));

        jScrollPane2 = new javax.swing.JScrollPane();
        tblRecomendados = new javax.swing.JTable();
        tblRecomendados.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {}, new String[] { "LIBRO", "CATEGORIA" }));
        jScrollPane2.setViewportView(tblRecomendados);
        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 370, 150));
        inicioContentPanel.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, 400, 320));

        // --- ASIGNACIÃ“N DE ACCIONES ---
        btnInicio.addActionListener(evt -> {
            cargarDatosPrestamos();
            cargarRecomendados();
            showPanel(inicioContentPanel);
            jLabel2.setText("Inicio"); // Cambiar tÃ­tulo de la barra superior
            setButtonSelected(btnInicio);
        });
        btnLibros.addActionListener(evt -> {
            showPanel(librosPanel);
            jLabel2.setText("Libros");
            setButtonSelected(btnLibros);
        });
        btnMisPrestamos.addActionListener(evt -> {
            prestamosPanel.cargarPrestamos();
            showPanel(prestamosPanel);
            jLabel2.setText("Mis Préstamos");
            setButtonSelected(btnMisPrestamos);
        });
        btnHistorial.addActionListener(evt -> {
            showPanel(historialPanel);
            jLabel2.setText("Historial");
            setButtonSelected(btnHistorial);
        });

        // El botÃ³n "Ver mÃ¡s libros" de la pantalla de inicio tambiÃ©n debe navegar a
        // la secciÃ³n de libros
        btnVerLibros.addActionListener(evt -> {
            showPanel(librosPanel);
            jLabel2.setText("Libros");
            setButtonSelected(btnLibros);
        });

        // Solicitar renovacion desde la pantalla de inicio
        btnSolicitarRenovacion.addActionListener(evt -> {
            int selectedRow = tblTusPrestamos.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un prestamo para renovar.");
                return;
            }

            String tituloLibro = String.valueOf(tblTusPrestamos.getValueAt(selectedRow, 0));
            String dniUsuario = SesionActual.dni;
            if (dniUsuario == null || dniUsuario.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontro la sesion del usuario.");
                return;
            }

            int idUsuario = daoLector.obtenerIdUsuarioPorDni(dniUsuario);
            if (idUsuario == -1) {
                JOptionPane.showMessageDialog(this, "No se pudo identificar al usuario.");
                return;
            }

            boolean renovado = daoLector.renovarPrestamoPorTitulo(idUsuario, tituloLibro);
            if (renovado) {
                JOptionPane.showMessageDialog(this, "Renovacion exitosa por 7 dias desde hoy.");
                cargarDatosPrestamos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No es posible renovar. Verifique que el prestamo no haya vencido.");
            }
        });

        btnSalir.addActionListener(evt -> {
            // 1. Crear y mostrar la ventana de Login nuevamente
            SistemaBibliotecario.VistaLogin.login loginWindow = new SistemaBibliotecario.VistaLogin.login();
            loginWindow.setVisible(true);

            // 2. Obtener la ventana que contiene este panel y cerrarla
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Declaraciones de variables">
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnLibros;
    private javax.swing.JButton btnMisPrestamos;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSolicitarRenovacion;
    private javax.swing.JButton btnVerLibros;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblRecomendados;
    private javax.swing.JTable tblTusPrestamos;
    // </editor-fold>
}

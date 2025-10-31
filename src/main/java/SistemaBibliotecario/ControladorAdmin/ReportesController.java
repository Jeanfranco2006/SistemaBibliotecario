/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SistemaBibliotecario.ControladorAdmin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import SistemaBibliotecario.Dao.ReporteDAO;
import SistemaBibliotecario.Modelos.SesionActual;
import SistemaBibliotecario.VistaAdmin.bibliotecarios;
import SistemaBibliotecario.VistaAdmin.inicio;
import SistemaBibliotecario.VistaAdmin.reportes;
import SistemaBibliotecario.VistaAdmin.usuarios;
import SistemaBibliotecario.VistaLogin.login;

/**
 *
 * @author User
 */
public class ReportesController {
    
    private ReporteDAO reporteDAO;
    private reportes vista;
    
    public ReportesController(reportes vista) {
        this.vista = vista;
        this.reporteDAO = new ReporteDAO();
        inicializarControlador();
    }

    private void inicializarControlador() {
        configurarVista();
        inicializarComboboxes();
    }
    
    private void configurarVista() {
        establecerNombreBibliotecario();
    }
    
    private void inicializarComboboxes() {
        inicializarComboboxEstados();
        inicializarComboboxCategorias();
    }

    // ========== MÉTODOS DE INICIALIZACIÓN ==========
    
    private void inicializarComboboxCategorias() {
        try {
            vista.getCbxCategoria().removeAllItems();
            vista.getCbxCategoria().addItem("Todas");
            
            List<String> categorias = reporteDAO.obtenerTodasLasCategorias();
            
            for (String categoria : categorias) {
                vista.getCbxCategoria().addItem(categoria);
            }
            
            vista.getCbxCategoria().setSelectedIndex(0);
            
        } catch (Exception e) {
            mostrarError("Error al cargar categorías: " + e.getMessage());
        }
    }
    
    private void inicializarComboboxEstados() {
        vista.getCbxEstadoPrestamos().removeAllItems();
        vista.getCbxEstadoPrestamos().addItem("Todos");
        vista.getCbxEstadoPrestamos().addItem("Activo");
        vista.getCbxEstadoPrestamos().addItem("Devuelto");
        vista.getCbxEstadoPrestamos().addItem("Vencido");
        vista.getCbxEstadoPrestamos().setSelectedIndex(0);
    }
    
    private void establecerNombreBibliotecario() {
        if (SesionActual.nombre != null && !SesionActual.nombre.isEmpty()) {
            vista.getLblNombreBibliotecario().setText(" " + SesionActual.nombre);
        } else {
            vista.getLblNombreBibliotecario().setText("Bienvenido: Bibliotecario");
        }
    }

    // ========== MÉTODOS PRINCIPALES ==========
    
    public void generarReportePrestamos() {
        // Validar fechas
        if (vista.getJdcDesdePrestamos().getDate() == null || vista.getJdcHastaPrestamos().getDate() == null) {
            mostrarAdvertencia("Por favor, seleccione ambas fechas (desde y hasta).");
            return;
        }
        
        Date fechaDesde = vista.getJdcDesdePrestamos().getDate();
        Date fechaHasta = vista.getJdcHastaPrestamos().getDate();
        
        // Validar rango de fechas
        if (fechaDesde.after(fechaHasta)) {
            mostrarError("La fecha 'Desde' no puede ser mayor que la fecha 'Hasta'.");
            return;
        }
        
        String estado = "";
        if (vista.getCbxEstadoPrestamos().getSelectedItem() != null) {
            estado = vista.getCbxEstadoPrestamos().getSelectedItem().toString().toLowerCase();
        }
        
        if (estado.equals("todos")) {
            estado = null;
        }
        
        generarReporteExcelPrestamos(fechaDesde, fechaHasta, estado);
    }
    
    public void generarReporteLibros() {
        try {
            // Validar fechas
            if (vista.getJdcDesdeLibros().getDate() == null || vista.getJdcHastaLibro().getDate() == null) {
                mostrarAdvertencia("Por favor, seleccione ambas fechas para el reporte de libros.");
                return;
            }
            
            Date fechaDesde = vista.getJdcDesdeLibros().getDate();
            Date fechaHasta = vista.getJdcHastaLibro().getDate();
            
            // Validar rango de fechas
            if (fechaDesde.after(fechaHasta)) {
                mostrarError("La fecha 'Desde' no puede ser mayor que la fecha 'Hasta'.");
                return;
            }
            
            // Validar que se haya seleccionado un tipo de reporte
            if (!vista.getJrbtnPopulares().isSelected() && !vista.getJrtbnMenosPrestados().isSelected()) {
                mostrarAdvertencia("Por favor, seleccione un tipo de reporte (Más populares o Menos prestados).");
                return;
            }
            
            // Obtener la categoría seleccionada
            String categoria = null;
            if (vista.getCbxCategoria().getSelectedItem() != null && !vista.getCbxCategoria().getSelectedItem().toString().equals("Todas")) {
                categoria = vista.getCbxCategoria().getSelectedItem().toString();
            }
            
            boolean esPopular = vista.getJrbtnPopulares().isSelected();
            
            // Seleccionar ubicación para guardar
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar reporte de libros");
            
            String nombreArchivo = esPopular ? "reporte_libros_populares.xlsx" : "reporte_libros_menos_prestados.xlsx";
            fileChooser.setSelectedFile(new java.io.File(nombreArchivo));
            
            int userSelection = fileChooser.showSaveDialog(vista);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                String rutaArchivo = fileToSave.getAbsolutePath();
                
                if (!rutaArchivo.toLowerCase().endsWith(".xlsx")) {
                    rutaArchivo += ".xlsx";
                }
                
                boolean exito = reporteDAO.generarReporteLibrosExcel(fechaDesde, fechaHasta, esPopular, categoria, rutaArchivo);
                
                String tipoReporte = esPopular ? "Libros Más Populares" : "Libros Menos Prestados";
                String infoCategoria = categoria != null ? "Categoría: " + categoria : "Todas las categorías";
                
                if (exito) {
                    mostrarExito("Reporte de " + tipoReporte + " generado exitosamente!\n" +
                                "📍 Ubicación: " + rutaArchivo + "\n" +
                                "📅 Período: " + new SimpleDateFormat("dd/MM/yyyy").format(fechaDesde) + 
                                " a " + new SimpleDateFormat("dd/MM/yyyy").format(fechaHasta) + "\n" +
                                "📚 " + infoCategoria);
                } else {
                    mostrarError("Error al generar el reporte de libros.");
                }
            }
            
        } catch (Exception e) {
            mostrarError("Error al generar reporte de libros: " + e.getMessage());
        }
    }
    
    public void generarReporteUsuarios() {
        try {
            // Validar fechas
            if (vista.getJdcDesdeusuarios().getDate() == null || vista.getJdcHastausuarios().getDate() == null) {
                mostrarAdvertencia("Por favor, seleccione ambas fechas para el reporte de usuarios.");
                return;
            }
            
            Date fechaDesde = vista.getJdcDesdeusuarios().getDate();
            Date fechaHasta = vista.getJdcHastausuarios().getDate();
            
            // Validar rango de fechas
            if (fechaDesde.after(fechaHasta)) {
                mostrarError("La fecha 'Desde' no puede ser mayor que la fecha 'Hasta'.");
                return;
            }
            
            // Opciones de reporte
            String[] opcionesReporte = {"Todos los Usuarios Registrados", "Estadísticas de Préstamos (Solo Lectores)"};
            int tipoReporte = JOptionPane.showOptionDialog(vista,
                "¿Qué tipo de reporte de usuarios deseas generar?",
                "Seleccionar Reporte de Usuarios",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesReporte,
                opcionesReporte[0]);
            
            if (tipoReporte == -1) return;
            
            String rolFiltro = "todos";
            
            // Si selecciona "Todos los usuarios", preguntar por filtro de rol
            if (tipoReporte == 0) {
                String[] opcionesRol = {"Todos los roles", "Solo Lectores", "Solo Bibliotecarios", "Solo Administradores"};
                int rolSeleccionado = JOptionPane.showOptionDialog(vista,
                    "¿Qué rol deseas incluir en el reporte?",
                    "Filtrar por Rol",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesRol,
                    opcionesRol[0]);
                
                switch (rolSeleccionado) {
                    case 0: rolFiltro = "todos"; break;
                    case 1: rolFiltro = "lector"; break;
                    case 2: rolFiltro = "bibliotecario"; break;
                    case 3: rolFiltro = "administrador"; break;
                    default: rolFiltro = "todos";
                }
            }
            
            // Seleccionar ubicación para guardar
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar reporte de usuarios");
            
            String nombreArchivo = tipoReporte == 0 ? "reporte_usuarios_registrados.xlsx" : "reporte_estadisticas_usuarios.xlsx";
            fileChooser.setSelectedFile(new java.io.File(nombreArchivo));
            
            int userSelection = fileChooser.showSaveDialog(vista);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                String rutaArchivo = fileToSave.getAbsolutePath();
                
                if (!rutaArchivo.toLowerCase().endsWith(".xlsx")) {
                    rutaArchivo += ".xlsx";
                }
                
                boolean exito = reporteDAO.generarReporteUsuariosExcel(fechaDesde, fechaHasta, tipoReporte + 1, rolFiltro, rutaArchivo);
                
                String tipoReporteStr = tipoReporte == 0 ? "Usuarios Registrados" : "Estadísticas de Préstamos por Usuario";
                String infoRol = tipoReporte == 0 ? "Rol: " + (rolFiltro.equals("todos") ? "Todos" : rolFiltro) : "(Solo lectores)";
                
                if (exito) {
                    mostrarExito("Reporte de " + tipoReporteStr + " generado exitosamente!\n" +
                                "📍 Ubicación: " + rutaArchivo + "\n" +
                                "📅 Período: " + new SimpleDateFormat("dd/MM/yyyy").format(fechaDesde) + 
                                " a " + new SimpleDateFormat("dd/MM/yyyy").format(fechaHasta) + "\n" +
                                "👥 " + infoRol);
                } else {
                    mostrarError("Error al generar el reporte de usuarios.");
                }
            }
            
        } catch (Exception e) {
            mostrarError("Error al generar reporte de usuarios: " + e.getMessage());
        }
    }

    // ========== MÉTODOS AUXILIARES ==========
    
    private void generarReporteExcelPrestamos(Date fechaDesde, Date fechaHasta, String estado) {
        try {
            // Seleccionar ubicación para guardar
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar reporte de préstamos");
            fileChooser.setSelectedFile(new java.io.File("reporte_prestamos.xlsx"));
            
            int userSelection = fileChooser.showSaveDialog(vista);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                String rutaArchivo = fileToSave.getAbsolutePath();
                
                if (!rutaArchivo.toLowerCase().endsWith(".xlsx")) {
                    rutaArchivo += ".xlsx";
                }
                
                boolean exito = reporteDAO.generarReportePrestamosExcelTotal(fechaDesde, fechaHasta, estado, rutaArchivo);
                
                String estadoMensaje = (estado == null) ? "Todos los estados" : estado;
                
                if (exito) {
                    mostrarExito("Reporte generado exitosamente!\n" +
                                "📍 Ubicación: " + rutaArchivo + "\n" +
                                "📅 Período: " + new SimpleDateFormat("dd/MM/yyyy").format(fechaDesde) + 
                                " a " + new SimpleDateFormat("dd/MM/yyyy").format(fechaHasta) + "\n" +
                                "📊 Estado: " + estadoMensaje);
                } else {
                    mostrarError("Error al generar el reporte.");
                }
            }
        } catch (Exception e) {
            mostrarError("Error al generar Excel: " + e.getMessage());
        }
    }

    // ========== MÉTODOS DE NAVEGACIÓN ==========
    
    public void navegarAInicio() {
        JFrame frame = new JFrame("Inicio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new inicio());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        cerrarVentanaActual();
    }

    public void navegarABibliotecarios() {
        JFrame frame = new JFrame("Gestión de Bibliotecarios");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new bibliotecarios());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        cerrarVentanaActual();
    }
    
    public void navegarAUsuarios() {
        JFrame frame = new JFrame("Gestión de Usuarios");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new usuarios());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        cerrarVentanaActual();
    }
    
    public void cerrarSesion() {
        login view = new login();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        cerrarVentanaActual();
    }
    
    private void cerrarVentanaActual() {
        SwingUtilities.getWindowAncestor(vista).dispose();
    }

    // ========== MÉTODOS DE MENSAJES ==========
    
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(vista, "✅ " + mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, "❌ " + mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(vista, "⚠️ " + mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}
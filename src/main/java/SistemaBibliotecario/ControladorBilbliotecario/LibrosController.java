/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SistemaBibliotecario.ControladorBilbliotecario;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import SistemaBibliotecario.Dao.LibroDAO;
import SistemaBibliotecario.Modelos.Categoria;
import SistemaBibliotecario.Modelos.SesionActual;
import SistemaBibliotecario.VistaLogin.login;
import SistemaBibliotecario.vistaBlibliotecario.inicio;
import SistemaBibliotecario.vistaBlibliotecario.libros;
import SistemaBibliotecario.vistaBlibliotecario.prestamos;
import SistemaBibliotecario.vistaBlibliotecario.reportes;
import SistemaBibliotecario.vistaBlibliotecario.usuarios;

/**
 *
 * @author User
 */
public class LibrosController {
    
    private LibroDAO libroDAO;
    private libros vista;
    
    public LibrosController(libros vista) {
        this.vista = vista;
        this.libroDAO = new LibroDAO();
        inicializarControlador();
    }

    private void inicializarControlador() {
        configurarVista();
        cargarDatosIniciales();
    }
    
    private void configurarVista() {
        establecerNombreBibliotecario();
    }
    
    private void cargarDatosIniciales() {
        cargarCategorias();
        cargarLibros();
        actualizarEstadisticas();
    }

    // ========== MÉTODOS DE INICIALIZACIÓN ==========
    
    private void cargarCategorias() {
        try {
            vista.getCbxCategoria().removeAllItems();
            List<Object[]> categoriasData = libroDAO.obtenerCategorias();
            
            for (Object[] categoriaData : categoriasData) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria((Integer) categoriaData[0]);
                categoria.setNombre((String) categoriaData[1]);
                
                vista.getCbxCategoria().addItem(categoria);
            }
        } catch (Exception e) {
            mostrarError("Error al cargar categorías: " + e.getMessage());
        }
    }
    
    private void cargarLibros() {
        try {
            DefaultTableModel model = (DefaultTableModel) vista.getTblLibros().getModel();
            model.setRowCount(0);
            
            List<Object[]> libros = libroDAO.listarLibros();
            for (Object[] libro : libros) {
                model.addRow(libro);
            }
        } catch (Exception e) {
            mostrarError("Error al cargar libros: " + e.getMessage());
        }
    }
    
    private void actualizarEstadisticas() {
        int totalEjemplares = libroDAO.contarLibros();
        vista.getJbltotalLibros().setText(String.valueOf(totalEjemplares));
    }
    
    private void establecerNombreBibliotecario() {
        if (SesionActual.nombre != null && !SesionActual.nombre.isEmpty()) {
            vista.getLblNombreBibliotecario().setText(" " + SesionActual.nombre);
        } else {
            vista.getLblNombreBibliotecario().setText("Bienvenido: Bibliotecario");
        }
    }

    // ========== MÉTODOS PRINCIPALES ==========
    
    public void crearCategoria() {
        String nombreCategoria = JOptionPane.showInputDialog(vista, 
            "Ingrese el nombre de la nueva categoría:",
            "Crear Nueva Categoría",
            JOptionPane.QUESTION_MESSAGE);
        
        if (nombreCategoria != null && !nombreCategoria.trim().isEmpty()) {
            nombreCategoria = nombreCategoria.trim();
            
            if (libroDAO.agregarCategoria(nombreCategoria)) {
                mostrarExito("Categoría '" + nombreCategoria + "' creada correctamente.");
                cargarCategorias();
            } else {
                mostrarError("Error al crear la categoría.");
            }
        } else if (nombreCategoria != null) {
            mostrarAdvertencia("El nombre de la categoría no puede estar vacío.");
        }
    }
    
    public void agregarLibro() {
        String isbn = vista.getTxtIsbn().getText().trim();
        String titulo = vista.getTxtTitulo().getText().trim();
        String autor = vista.getTxtAutor().getText().trim();
        String stockStr = vista.getTxtStock().getText().trim();
        String anioStr = vista.getTxtAñoPublicacion().getText().trim();
        
        // Validar campos vacíos
        if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty() || stockStr.isEmpty() || anioStr.isEmpty()) {
            mostrarAdvertencia("Por favor, complete todos los campos.");
            return;
        }
        
        // Validar que se haya seleccionado una categoría
        if (vista.getCbxCategoria().getSelectedItem() == null) {
            mostrarAdvertencia("Por favor, seleccione una categoría.");
            return;
        }
        
        try {
            int stock = Integer.parseInt(stockStr);
            int anioPublicacion = Integer.parseInt(anioStr);
            
            // Validar año
            if (anioPublicacion < 1000 || anioPublicacion > 2024) {
                mostrarAdvertencia("Por favor, ingrese un año válido.");
                return;
            }
            
            // Validar stock
            if (stock < 0) {
                mostrarAdvertencia("El stock no puede ser negativo.");
                return;
            }
            
            // Obtener categoría seleccionada
            Categoria categoriaSeleccionada = (Categoria) vista.getCbxCategoria().getSelectedItem();
            int idCategoria = categoriaSeleccionada.getIdCategoria();
            
            // Agregar libro
            if (libroDAO.agregarLibro(isbn, titulo, stock, autor, anioPublicacion, idCategoria)) {
                mostrarExito("Libro agregado correctamente.");
                limpiarCampos();
                cargarLibros();
                actualizarEstadisticas();
            } else {
                mostrarError("Error al agregar el libro.");
            }
            
        } catch (NumberFormatException e) {
            mostrarError("Stock y Año deben ser números válidos.");
        }
    }
    
    public void actualizarLibro() {
        String isbn = vista.getTxtIsbn().getText().trim();
        String titulo = vista.getTxtTitulo().getText().trim();
        String autor = vista.getTxtAutor().getText().trim();
        String stockStr = vista.getTxtStock().getText().trim();
        String anioStr = vista.getTxtAñoPublicacion().getText().trim();
        
        // Validar que haya un libro seleccionado
        if (isbn.isEmpty()) {
            mostrarAdvertencia("No hay libro seleccionado para actualizar.");
            return;
        }
        
        // Validar campos vacíos
        if (titulo.isEmpty() || autor.isEmpty() || stockStr.isEmpty() || anioStr.isEmpty()) {
            mostrarAdvertencia("Por favor, complete todos los campos.");
            return;
        }
        
        // Validar que se haya seleccionado una categoría
        if (vista.getCbxCategoria().getSelectedItem() == null) {
            mostrarAdvertencia("Por favor, seleccione una categoría.");
            return;
        }
        
        try {
            int stock = Integer.parseInt(stockStr);
            int anioPublicacion = Integer.parseInt(anioStr);
            
            // Validar año
            if (anioPublicacion < 1000 || anioPublicacion > 2024) {
                mostrarAdvertencia("Por favor, ingrese un año válido.");
                return;
            }
            
            // Validar stock
            if (stock < 0) {
                mostrarAdvertencia("El stock no puede ser negativo.");
                return;
            }
            
            // Obtener categoría seleccionada
            Categoria categoriaSeleccionada = (Categoria) vista.getCbxCategoria().getSelectedItem();
            int idCategoria = categoriaSeleccionada.getIdCategoria();
            
            // Actualizar libro
            if (libroDAO.actualizarLibro(isbn, titulo, stock, autor, anioPublicacion, idCategoria)) {
                mostrarExito("Libro actualizado correctamente.");
                limpiarCampos();
                cargarLibros();
                actualizarEstadisticas();
            } else {
                mostrarError("Error al actualizar el libro.");
            }
            
        } catch (NumberFormatException e) {
            mostrarError("Stock y Año deben ser números válidos.");
        }
    }
    
    public void eliminarLibro() {
        String isbn = vista.getTxtIsbn().getText().trim();
        
        if (isbn.isEmpty()) {
            mostrarAdvertencia("No hay libro seleccionado para eliminar.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(vista, 
            "¿Está seguro de eliminar el libro con ISBN: " + isbn + "?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (libroDAO.eliminarLibro(isbn)) {
                mostrarExito("Libro eliminado correctamente.");
                limpiarCampos();
                cargarLibros();
                actualizarEstadisticas();
            } else {
                mostrarError("Error al eliminar el libro.");
            }
        }
    }
    
    public void buscarLibro() {
        String isbn = vista.getTxtBuscar().getText().trim();
        
        if (isbn.isEmpty()) {
            mostrarAdvertencia("Por favor, ingrese un ISBN para buscar.");
            return;
        }
        
        Object[] libro = libroDAO.buscarLibroPorISBN(isbn);
        
        if (libro != null) {
            // Llenar los campos con los datos del libro encontrado
            vista.getTxtIsbn().setText((String) libro[0]);
            vista.getTxtTitulo().setText((String) libro[1]);
            vista.getTxtAutor().setText((String) libro[2]);
            vista.getTxtStock().setText(String.valueOf(libro[3]));
            vista.getTxtAñoPublicacion().setText(String.valueOf(libro[4]));
            
            // Seleccionar la categoría correcta en el combobox
            int idCategoria = (Integer) libro[5];
            boolean categoriaEncontrada = false;
            
            for (int i = 0; i < vista.getCbxCategoria().getItemCount(); i++) {
                Categoria item = vista.getCbxCategoria().getItemAt(i);
                if (item.getIdCategoria() == idCategoria) {
                    vista.getCbxCategoria().setSelectedIndex(i);
                    categoriaEncontrada = true;
                    break;
                }
            }
            
            if (!categoriaEncontrada) {
                mostrarAdvertencia("Categoría no encontrada en la lista.");
            }
            
            mostrarExito("Libro encontrado.");
        } else {
            mostrarError("No se encontró un libro con ese ISBN.");
        }
    }
    
    public void limpiarCampos() {
        vista.getTxtIsbn().setText("");
        vista.getTxtTitulo().setText("");
        vista.getTxtAutor().setText("");
        vista.getTxtStock().setText("");
        vista.getTxtAñoPublicacion().setText("");
        vista.getTxtBuscar().setText("");
        if (vista.getCbxCategoria().getItemCount() > 0) {
            vista.getCbxCategoria().setSelectedIndex(0);
        }
        vista.getTblLibros().clearSelection();
    }
    
    public void cargarDatosDesdeTabla() {
        int selectedRow = vista.getTblLibros().getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) vista.getTblLibros().getModel();
            
            // Obtener datos de la fila seleccionada
            String isbn = model.getValueAt(selectedRow, 0).toString();
            String titulo = model.getValueAt(selectedRow, 1).toString();
            String autor = model.getValueAt(selectedRow, 3).toString();
            String categoriaNombre = model.getValueAt(selectedRow, 2).toString();
            String stock = model.getValueAt(selectedRow, 4).toString();
            
            // Llenar los campos con los datos del libro seleccionado
            vista.getTxtIsbn().setText(isbn);
            vista.getTxtTitulo().setText(titulo);
            vista.getTxtAutor().setText(autor);
            vista.getTxtStock().setText(stock);
            
            // Buscar y seleccionar la categoría correspondiente
            for (int i = 0; i < vista.getCbxCategoria().getItemCount(); i++) {
                Categoria categoria = vista.getCbxCategoria().getItemAt(i);
                if (categoria.getNombre().equals(categoriaNombre)) {
                    vista.getCbxCategoria().setSelectedIndex(i);
                    break;
                }
            }
            
            // Buscar el año de publicación
            buscarAnioPublicacion(isbn);
        }
    }
    
    private void buscarAnioPublicacion(String isbn) {
        Object[] libro = libroDAO.buscarLibroPorISBN(isbn);
        if (libro != null && libro.length > 4) {
            vista.getTxtAñoPublicacion().setText(String.valueOf(libro[4]));
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

    public void navegarAUsuarios() {
        JFrame frame = new JFrame("Gestión de Usuarios");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new usuarios());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        cerrarVentanaActual();
    }
    
    public void navegarAPrestamos() {
        JFrame frame = new JFrame("Gestión de Préstamos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new prestamos());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        cerrarVentanaActual();
    }
    
    public void navegarAReportes() {
        JFrame frame = new JFrame("Gestión de Reportes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new reportes());
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
package SistemaBibliotecario.Controladores;

import SistemaBibliotecario.Dao.LectorDAO;
import SistemaBibliotecario.Modelos.SesionActual;
import SistemaBibliotecario.VistaAdmin.usuarios;
import SistemaBibliotecario.VistaAdmin.bibliotecarios;
import SistemaBibliotecario.VistaAdmin.inicio;
import SistemaBibliotecario.VistaAdmin.reportes;
import SistemaBibliotecario.VistaLogin.login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UsuariosController {
    private usuarios vista;
    private LectorDAO lectorDAO;

    public UsuariosController(usuarios vista) {
        this.vista = vista;
        this.lectorDAO = new LectorDAO();
        inicializarControlador();
    }

    private void inicializarControlador() {
        cargarListeners();
        inicializarVista();
    }

    private void cargarListeners() {
        // Navegación entre paneles
        vista.getBtnBibliotecarios().addActionListener(e -> navegarABibliotecarios());
        vista.getBtnInicio().addActionListener(e -> navegarAInicio());
        vista.getBtnReportes().addActionListener(e -> navegarAReportes());
        vista.getBtnSalir().addActionListener(e -> cerrarSesion());

        // Funcionalidades específicas de usuarios
        vista.getBtnBuscar().addActionListener(e -> buscarLectores());
        vista.getTblUsuarios().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarUsuarioDeTabla();
            }
        });
    }

    private void inicializarVista() {
        cargarLectores();
        mostrarTotalUsuarios();
        configurarNombreBibliotecario();
        configurarTabla();
    }

    private void configurarNombreBibliotecario() {
        if (SesionActual.nombre != null && !SesionActual.nombre.isEmpty()) {
            vista.getLblNombreBibliotecario().setText(" " + SesionActual.nombre);
        } else {
            vista.getLblNombreBibliotecario().setText("Bienvenido: Bibliotecario");
        }
    }

    // Métodos de navegación
    private void navegarABibliotecarios() {
        JFrame frame = new JFrame("Gestión de Bibliotecarios");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new bibliotecarios());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        cerrarVentanaActual();
    }

    private void navegarAInicio() {
        JFrame frame = new JFrame("Inicio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new inicio());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        cerrarVentanaActual();
    }

    private void navegarAReportes() {
        JFrame frame = new JFrame("Gestión de Reportes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new reportes());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        cerrarVentanaActual();
    }

    private void cerrarSesion() {
        login view = new login();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        cerrarVentanaActual();
    }

    private void cerrarVentanaActual() {
        SwingUtilities.getWindowAncestor(vista).dispose();
    }

    // Métodos de funcionalidad de usuarios
    public void cargarLectores() {
        try {
            DefaultTableModel model = vista.getModeloTabla();
            model.setRowCount(0);

            List<Object[]> lectores = lectorDAO.listarLector();

            if (lectores.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "No hay usuarios lectores registrados.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Object[] lector : lectores) {
                    Object[] filaMostrar = new Object[5];
                    filaMostrar[0] = lector[1]; // Nombre
                    filaMostrar[1] = lector[2]; // Apellidos
                    filaMostrar[2] = lector[3]; // Email
                    filaMostrar[3] = lector[4]; // Fecha Creación
                    filaMostrar[4] = lector[5]; // Último Acceso

                    model.addRow(filaMostrar);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar usuarios: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void mostrarTotalUsuarios() {
        try {
            List<Object[]> lectores = lectorDAO.listarLector();
            int total = lectores.size();
            vista.getTxtTotalUsuarios().setText(String.valueOf(total));
        } catch (Exception e) {
            vista.getTxtTotalUsuarios().setText("0");
            System.err.println("Error al contar usuarios: " + e.getMessage());
        }
    }

    private void buscarLectores() {
        String criterio = vista.getTxtBusqueda().getText().trim();

        if (criterio.isEmpty()) {
            cargarLectores();
            return;
        }

        try {
            DefaultTableModel model = vista.getModeloTabla();
            model.setRowCount(0);

            List<Object[]> lectores = lectorDAO.buscarLectorPorDniEmailNombre(criterio);

            if (lectores.isEmpty()) {
                JOptionPane.showMessageDialog(vista,
                        "No se encontraron usuarios con: " + criterio,
                        "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Object[] lector : lectores) {
                    Object[] filaMostrar = new Object[5];
                    filaMostrar[0] = lector[1]; // Nombre
                    filaMostrar[1] = lector[2]; // Apellidos
                    filaMostrar[2] = lector[3]; // Email
                    filaMostrar[3] = lector[4]; // Fecha Creación
                    filaMostrar[4] = lector[5]; // Último Acceso

                    model.addRow(filaMostrar);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error en la búsqueda: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarUsuarioDeTabla() {
        try {
            int filaSeleccionada = vista.getTblUsuarios().getSelectedRow();

            if (filaSeleccionada >= 0) {
                String email = vista.getTblUsuarios().getValueAt(filaSeleccionada, 2).toString();
                buscarDatosCompletosPorEmail(email);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al seleccionar usuario: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarDatosCompletosPorEmail(String email) {
        try {
            Object[] usuarioCompleto = lectorDAO.obtenerUsuarioCompletoPorEmail(email);

            if (usuarioCompleto != null) {
                vista.getTxtDni().setText(usuarioCompleto[0].toString());
                vista.getTxtNombre().setText(usuarioCompleto[1].toString());
                vista.getTxtApellidoP().setText(usuarioCompleto[2].toString());
                vista.getTxtApellidoM().setText(usuarioCompleto[3].toString());
                vista.getTxtDireccion().setText(usuarioCompleto[4].toString());
                vista.getTxtTelefono().setText(usuarioCompleto[5].toString());
                vista.getTxtEmail().setText(usuarioCompleto[6].toString());

                setCamposSoloLectura();
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontraron los datos completos del usuario.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar datos del usuario: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setCamposSoloLectura() {
        vista.getTxtDni().setEditable(false);
        vista.getTxtNombre().setEditable(false);
        vista.getTxtApellidoP().setEditable(false);
        vista.getTxtApellidoM().setEditable(false);
        vista.getTxtDireccion().setEditable(false);
        vista.getTxtTelefono().setEditable(false);
        vista.getTxtEmail().setEditable(false);
    }

    private void configurarTabla() {
        DefaultTableModel model = vista.getModeloTabla();
        model.setColumnIdentifiers(new String[] {
                "Nombre",
                "Apellidos",
                "Email",
                "Fecha Creación",
                "Último Acceso"
        });
    }
}
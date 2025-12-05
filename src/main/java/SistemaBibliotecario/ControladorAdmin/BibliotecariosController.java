/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SistemaBibliotecario.ControladorAdmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import SistemaBibliotecario.Conexion.ConexionMySQL;
import SistemaBibliotecario.Dao.BibliotecarioDAO;
import SistemaBibliotecario.Modelos.PasswordUtil;
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
public class BibliotecariosController {

    private BibliotecarioDAO bibliotecarioDAO;
    private bibliotecarios vista;

    public BibliotecariosController(bibliotecarios vista) {
        this.vista = vista;
        this.bibliotecarioDAO = new BibliotecarioDAO();
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
        cargarBibliotecarios();
        mostrarTotalBibliotecarios();
    }

    // ========== MÉTODOS PRINCIPALES ==========

    public void cargarBibliotecarios() {
        DefaultTableModel model = (DefaultTableModel) vista.getTblBibliotecarios().getModel();
        model.setRowCount(0);

        List<Object[]> lista = bibliotecarioDAO.listarBibliotecarios();

        for (Object[] bibliotecario : lista) {
            Object[] filaMostrar = new Object[5];
            filaMostrar[0] = bibliotecario[1]; // Nombre
            filaMostrar[1] = bibliotecario[2]; // Apellidos
            filaMostrar[2] = bibliotecario[3]; // Email
            filaMostrar[3] = bibliotecario[4]; // Fecha Ingreso
            filaMostrar[4] = bibliotecario[5]; // Último Acceso

            model.addRow(filaMostrar);
        }
    }

    public void mostrarTotalBibliotecarios() {
        String sql = "SELECT COUNT(*) FROM usuario WHERE rol = 'bibliotecario'";
        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt(1);
                vista.getLblTotalBibliotecarios().setText(String.valueOf(total));
                vista.getLblTotalBibliotecarios().setHorizontalAlignment(JLabel.CENTER);
            }
        } catch (SQLException e) {
            mostrarError("Error al contar bibliotecarios: " + e.getMessage());
        }
    }

    public void agregarBibliotecario() {
        if (!validarCamposObligatorios()) {
            mostrarAdvertencia("Por favor, complete todos los campos.");
            return;
        }

        Connection conn = null;
        PreparedStatement psPersona = null;
        PreparedStatement psUsuario = null;
        ResultSet rs = null;

        try {
            conn = ConexionMySQL.getInstancia().getConexion();
            conn.setAutoCommit(false);

            // Insertar persona
            String sqlPersona = "INSERT INTO persona (dni, nombre, apellido_p, apellido_m, direccion, telefono, email) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            psPersona = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            psPersona.setString(1, vista.getTxtDni().getText().trim());
            psPersona.setString(2, vista.getTxtNombre().getText().trim());
            psPersona.setString(3, vista.getTxtApellidoP().getText().trim());
            psPersona.setString(4, vista.getTxtApellidoM().getText().trim());
            psPersona.setString(5, vista.getTxtDireccion().getText().trim());
            psPersona.setString(6, vista.getTxtTelefono().getText().trim());
            psPersona.setString(7, vista.getTxtEmail().getText().trim());
            psPersona.executeUpdate();

            // Obtener id_persona generado
            rs = psPersona.getGeneratedKeys();
            int idPersona = 0;
            if (rs.next()) {
                idPersona = rs.getInt(1);
            }

            // Insertar usuario CON CONTRASEÑA ENCRIPTADA
            String sqlUsuario = "INSERT INTO usuario (id_persona, contrasena, rol) VALUES (?, ?, ?)";
            psUsuario = conn.prepareStatement(sqlUsuario);
            psUsuario.setInt(1, idPersona);

            // 👉 ENCRIPTAR LA CONTRASEÑA ANTES DE GUARDAR
            String contrasenaPlana = vista.getTxtContrasena().getText().trim();
            String contrasenaHash = PasswordUtil.hashPassword(contrasenaPlana);
            psUsuario.setString(2, contrasenaHash); // Guardar el HASH

            psUsuario.setString(3, "bibliotecario");
            psUsuario.executeUpdate();

            conn.commit();
            mostrarExito("Bibliotecario agregado correctamente.");

            // Actualizar vista
            limpiarCampos();
            cargarBibliotecarios();
            mostrarTotalBibliotecarios();

        } catch (SQLException e) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            mostrarError("Error al agregar bibliotecario: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, psPersona, psUsuario, conn);
        }
    }

    public void actualizarBibliotecario() {
        int fila = vista.getTblBibliotecarios().getSelectedRow();
        if (fila < 0) {
            mostrarAdvertencia("Seleccione un bibliotecario de la tabla para actualizar.");
            return;
        }

        // 1️⃣ Capturamos la posible nueva contraseña
        // (Asegúrate de que 'getTxtContrasena' sea el nombre correcto de tu campo en la
        // vista)
        String nuevaContrasena = vista.getTxtContrasena().getText().trim();

        try (Connection conn = ConexionMySQL.getInstancia().getConexion()) {

            // 2️⃣ Construcción dinámica del SQL
            // Empezamos con la parte básica
            StringBuilder sql = new StringBuilder("UPDATE persona p " +
                    "JOIN usuario u ON p.id_persona = u.id_persona " +
                    "SET p.nombre=?, p.apellido_p=?, p.apellido_m=?, p.email=?");

            // Si hay contraseña nueva, agregamos esa columna al SET
            if (!nuevaContrasena.isEmpty()) {
                sql.append(", u.contrasena=?");
            }

            // Terminamos con el WHERE
            sql.append(" WHERE p.nombre=? AND p.apellido_p=? AND p.apellido_m=?");

            PreparedStatement ps = conn.prepareStatement(sql.toString());

            // 3️⃣ Asignación de parámetros usando un contador (para no perder el orden)
            int index = 1;

            ps.setString(index++, vista.getTxtNombre().getText().trim());
            ps.setString(index++, vista.getTxtApellidoP().getText().trim());
            ps.setString(index++, vista.getTxtApellidoM().getText().trim());
            ps.setString(index++, vista.getTxtEmail().getText().trim());

            // Si hay contraseña, la encriptamos y la asignamos
            if (!nuevaContrasena.isEmpty()) {
                // AQUÍ USAMOS TU CLASE PasswordUtil
                String hash = SistemaBibliotecario.Modelos.PasswordUtil.hashPassword(nuevaContrasena);
                ps.setString(index++, hash);
            }

            // Parámetros del WHERE (Identificadores originales obtenidos de la tabla)
            ps.setString(index++, vista.getTblBibliotecarios().getValueAt(fila, 0).toString());

            // Lógica para separar apellidos (la mantuve igual a tu código original)
            String[] apellidos = vista.getTblBibliotecarios().getValueAt(fila, 1).toString().split(" ");
            ps.setString(index++, apellidos[0]); // Apellido Paterno original
            ps.setString(index++, apellidos.length >= 2 ? apellidos[1] : ""); // Apellido Materno original

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarExito("Datos actualizados correctamente.");
                cargarBibliotecarios();
                // Opcional: Limpiar el campo de contraseña después de guardar
                vista.getTxtContrasena().setText("");
            } else {
                mostrarAdvertencia("No se encontró el registro a actualizar.");
            }

        } catch (SQLException e) {
            mostrarError("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarBibliotecario() {
        int fila = vista.getTblBibliotecarios().getSelectedRow();
        if (fila == -1) {
            mostrarAdvertencia("Selecciona un bibliotecario para eliminar.");
            return;
        }

        String email = vista.getTblBibliotecarios().getValueAt(fila, 2).toString();

        int confirmar = JOptionPane.showConfirmDialog(
                null,
                "¿Estás seguro de eliminar al bibliotecario con email: " + email + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            try (Connection conn = ConexionMySQL.getInstancia().getConexion()) {
                conn.setAutoCommit(false);

                // Eliminar usuario primero
                String sqlUsuario = "DELETE FROM usuario WHERE id_persona = (SELECT id_persona FROM persona WHERE email = ?)";
                PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario);
                psUsuario.setString(1, email);
                psUsuario.executeUpdate();

                // Luego eliminar persona
                String sqlPersona = "DELETE FROM persona WHERE email = ?";
                PreparedStatement psPersona = conn.prepareStatement(sqlPersona);
                psPersona.setString(1, email);
                psPersona.executeUpdate();

                conn.commit();
                mostrarExito("Bibliotecario eliminado correctamente.");

                // Actualizar vista
                cargarBibliotecarios();
                mostrarTotalBibliotecarios();
                limpiarCampos();

            } catch (SQLException e) {
                mostrarError("Error al eliminar: " + e.getMessage());
            }
        }
    }

    public void buscarBibliotecarios() {
        String criterio = vista.getTxtBuscar().getText().trim();

        if (criterio.isEmpty()) {
            cargarBibliotecarios();
            return;
        }

        try {
            DefaultTableModel model = (DefaultTableModel) vista.getTblBibliotecarios().getModel();
            model.setRowCount(0);

            List<Object[]> bibliotecarios = bibliotecarioDAO.buscarBibliotecarioPorDniEmailNombre(criterio);

            if (bibliotecarios.isEmpty()) {
                mostrarInformacion("No se encontraron bibliotecarios con: " + criterio);
            } else {
                for (Object[] bibliotecario : bibliotecarios) {
                    Object[] filaMostrar = new Object[5];
                    filaMostrar[0] = bibliotecario[1]; // Nombre
                    filaMostrar[1] = bibliotecario[2]; // Apellidos
                    filaMostrar[2] = bibliotecario[3]; // Email
                    filaMostrar[3] = bibliotecario[4]; // Fecha Ingreso
                    filaMostrar[4] = bibliotecario[5]; // Último Acceso

                    model.addRow(filaMostrar);
                }

                mostrarInformacion("Se encontraron " + bibliotecarios.size() + " bibliotecario(s)");
            }

        } catch (Exception e) {
            mostrarError("Error en la búsqueda: " + e.getMessage());
        }
    }

    public void cargarDatosDesdeTabla() {
        int fila = vista.getTblBibliotecarios().getSelectedRow();
        if (fila >= 0) {
            String email = vista.getTblBibliotecarios().getValueAt(fila, 2).toString();

            try (Connection conn = ConexionMySQL.getInstancia().getConexion()) {
                String sql = """
                            SELECT p.dni, p.nombre, p.apellido_p, p.apellido_m,
                                   p.direccion, p.telefono, p.email
                            FROM persona p
                            JOIN usuario u ON p.id_persona = u.id_persona
                            WHERE p.email = ?
                        """;

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    vista.getTxtDni().setText(rs.getString("dni"));
                    vista.getTxtNombre().setText(rs.getString("nombre"));
                    vista.getTxtApellidoP().setText(rs.getString("apellido_p"));
                    vista.getTxtApellidoM().setText(rs.getString("apellido_m"));
                    vista.getTxtDireccion().setText(rs.getString("direccion"));
                    vista.getTxtTelefono().setText(rs.getString("telefono"));
                    vista.getTxtEmail().setText(rs.getString("email"));
                    vista.getTxtContrasena().setText(""); // No mostrar contraseña
                }

            } catch (SQLException e) {
                mostrarError("Error al cargar datos: " + e.getMessage());
            }
        }
    }

    public void limpiarCampos() {
        vista.getTxtDni().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtApellidoP().setText("");
        vista.getTxtApellidoM().setText("");
        vista.getTxtDireccion().setText("");
        vista.getTxtTelefono().setText("");
        vista.getTxtEmail().setText("");
        vista.getTxtContrasena().setText("");
        vista.getTxtBuscar().setText("");
        vista.getTblBibliotecarios().clearSelection();
        mostrarInformacion("Campos limpiados correctamente.");
    }

    // ========== MÉTODOS AUXILIARES ==========

    private boolean validarCamposObligatorios() {
        return !vista.getTxtDni().getText().trim().isEmpty() &&
                !vista.getTxtNombre().getText().trim().isEmpty() &&
                !vista.getTxtApellidoP().getText().trim().isEmpty() &&
                !vista.getTxtApellidoM().getText().trim().isEmpty() &&
                !vista.getTxtDireccion().getText().trim().isEmpty() &&
                !vista.getTxtTelefono().getText().trim().isEmpty() &&
                !vista.getTxtEmail().getText().trim().isEmpty() &&
                !vista.getTxtContrasena().getText().trim().isEmpty();
    }

    private void establecerNombreBibliotecario() {
        if (SesionActual.nombre != null && !SesionActual.nombre.isEmpty()) {
            vista.getLblNombreBibliotecario().setText(" " + SesionActual.nombre);
        } else {
            vista.getLblNombreBibliotecario().setText("Bienvenido: Bibliotecario");
        }
    }

    private void cerrarRecursos(ResultSet rs, PreparedStatement ps1, PreparedStatement ps2, Connection conn) {
        try {
            if (rs != null)
                rs.close();
            if (ps1 != null)
                ps1.close();
            if (ps2 != null)
                ps2.close();
            if (conn != null)
                conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
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
        JOptionPane.showMessageDialog(null, "✅ " + mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, "❌ " + mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(null, "⚠️ " + mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarInformacion(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
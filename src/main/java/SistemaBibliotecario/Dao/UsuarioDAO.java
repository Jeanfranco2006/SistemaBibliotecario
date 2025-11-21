package SistemaBibliotecario.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import SistemaBibliotecario.Modelos.PasswordUtil; // Asegúrate de importar

import SistemaBibliotecario.Conexion.ConexionMySQL;
import SistemaBibliotecario.Modelos.Usuario;

public class UsuarioDAO {

    public Usuario validarLogin(String dni, String contrasenaIngresada) {
        Usuario usuario = null;
        Connection conn = ConexionMySQL.getInstancia().getConexion();

        String sql = """
                    SELECT
                        u.id_usuario,
                        u.id_persona,
                        u.contrasena,
                        u.rol,
                        u.fecha_creacion,
                        u.fecha_actualizacion,
                        p.nombre,
                        p.apellido_p,
                        p.apellido_m
                    FROM usuario u
                    INNER JOIN persona p ON u.id_persona = p.id_persona
                    WHERE p.dni = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String passwordHashBD = rs.getString("contrasena");

                // 👉 Verificar contraseña con BCrypt
                if (!PasswordUtil.verifyPassword(contrasenaIngresada, passwordHashBD)) {
                    return null; // ❌ contraseña incorrecta
                }

                // 👉 Si coincide, construimos el usuario
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setIdPersona(rs.getInt("id_persona"));
                usuario.setRol(rs.getString("rol"));
                usuario.setContrasena(passwordHashBD);
                usuario.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                usuario.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));

                String nombreCompleto = rs.getString("nombre") + " " +
                        rs.getString("apellido_p") + " " +
                        rs.getString("apellido_m");

                SistemaBibliotecario.Modelos.SesionActual.nombre = nombreCompleto;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al validar login: " + e.getMessage());
        }

        return usuario;
    }

    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuario (id_persona, contrasena, rol, fecha_creacion) VALUES (?, ?, ?, NOW())";

        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            // 👉 Encriptar ANTES de guardar
            String passwordHash = PasswordUtil.hashPassword(usuario.getContrasena());

            ps.setInt(1, usuario.getIdPersona());
            ps.setString(2, passwordHash); // Guardas el HASH, no la clave real
            ps.setString(3, usuario.getRol());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarUltimoAcceso(String dni) {
        String sql = "UPDATE usuario u " +
                "INNER JOIN persona p ON u.id_persona = p.id_persona " +
                "SET u.ultimo_acceso = NOW() " +
                "WHERE p.dni = ?";

        try (Connection con = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar último acceso: " + e.getMessage());
            return false;
        }
    }

    public java.util.List<Object[]> getUsuariosPorRol(String rol) {
        java.util.List<Object[]> usuarios = new java.util.ArrayList<>();
        String sql = "SELECT p.dni, CONCAT(p.nombre, ' ', p.apellido_p, ' ', p.apellido_m) as nombre_completo, u.rol FROM usuario u JOIN persona p ON u.id_persona = p.id_persona WHERE u.rol = ?";

        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rol);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] usuario = {
                        rs.getString("dni"),
                        rs.getString("nombre_completo"),
                        rs.getString("rol")
                };
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios por rol: " + e.getMessage());
        }
        return usuarios;
    }
}

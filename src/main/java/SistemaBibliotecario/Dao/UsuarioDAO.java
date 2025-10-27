package SistemaBibliotecario.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import SistemaBibliotecario.Conexion.ConexionMySQL;
import SistemaBibliotecario.Modelos.Usuario;

public class UsuarioDAO {

  public Usuario validarLogin(String dni, String contrasena) {
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
        WHERE p.dni = ? AND u.contrasena = ?
    """;

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, dni);
        stmt.setString(2, contrasena);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            usuario = new Usuario();
            usuario.setIdUsuario(rs.getInt("id_usuario"));
            usuario.setIdPersona(rs.getInt("id_persona"));
            usuario.setContrasena(rs.getString("contrasena"));
            usuario.setRol(rs.getString("rol"));
            usuario.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
            usuario.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
            
            // ✅ CONCATENAR NOMBRE COMPLETO
            String nombre = rs.getString("nombre");
            String apellidoP = rs.getString("apellido_p");
            String apellidoM = rs.getString("apellido_m");
            String nombreCompleto = nombre + " " + apellidoP + " " + apellidoM;
            
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

            ps.setInt(1, usuario.getIdPersona());
            ps.setString(2, usuario.getContrasena());
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
}

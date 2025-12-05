/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SistemaBibliotecario.Dao;

import SistemaBibliotecario.Conexion.ConexionMySQL;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author User
 */
public class LectorDAO {

    public List<Object[]> listarLector() {
        List<Object[]> lista = new ArrayList<>();
        String sql = """
                    SELECT
                        p.dni,
                        p.nombre,
                        CONCAT(p.apellido_p, ' ', p.apellido_m) AS apellidos,
                        p.email,
                        u.fecha_creacion AS fecha_ingreso,
                        u.ultimo_acceso
                    FROM persona p
                    INNER JOIN usuario u ON p.id_persona = u.id_persona
                    WHERE u.rol = 'lector';
                """;

        try (Connection con = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[6]; // Ahora 6 columnas
                fila[0] = rs.getString("dni");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("apellidos");
                fila[3] = rs.getString("email");
                fila[4] = rs.getTimestamp("fecha_ingreso");
                fila[5] = rs.getTimestamp("ultimo_acceso");
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar bibliotecarios: " + e.getMessage());
        }

        return lista;
    }

    public List<Object[]> buscarLectorPorDniEmailNombre(String criterio) {
        List<Object[]> lista = new ArrayList<>();
        String sql = """
                SELECT
                    p.dni,
                    p.nombre,
                    CONCAT(p.apellido_p, ' ', p.apellido_m) AS apellidos,
                    p.email,
                    u.fecha_creacion AS fecha_ingreso,
                    u.ultimo_acceso
                FROM persona p
                INNER JOIN usuario u ON p.id_persona = u.id_persona
                WHERE u.rol = 'lector'
                AND (p.dni LIKE ? OR p.email LIKE ? OR p.nombre LIKE ? OR p.apellido_p LIKE ? OR p.apellido_m LIKE ?)
                """;

        try (Connection con = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            String likeCriterio = "%" + criterio + "%";
            ps.setString(1, likeCriterio); // dni
            ps.setString(2, likeCriterio); // email
            ps.setString(3, likeCriterio); // nombre
            ps.setString(4, likeCriterio); // apellido_p
            ps.setString(5, likeCriterio); // apellido_m

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString("dni");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("apellidos");
                fila[3] = rs.getString("email");
                fila[4] = rs.getTimestamp("fecha_ingreso");
                fila[5] = rs.getTimestamp("ultimo_acceso");
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar lectores: " + e.getMessage());
        }

        return lista;
    }

    public Object[] obtenerUsuarioCompletoPorDNI(String dni) {
        String sql = """
                SELECT
                    p.dni,
                    p.nombre,
                    p.apellido_p,
                    p.apellido_m,
                    p.direccion,
                    p.telefono,
                    p.email
                FROM persona p
                INNER JOIN usuario u ON p.id_persona = u.id_persona
                WHERE p.dni = ? AND u.rol = 'lector'
                """;

        try (Connection con = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Object[] usuario = new Object[7];
                usuario[0] = rs.getString("dni");
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("apellido_p");
                usuario[3] = rs.getString("apellido_m");
                usuario[4] = rs.getString("direccion");
                usuario[5] = rs.getString("telefono");
                usuario[6] = rs.getString("email");
                return usuario;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuario completo por DNI: " + e.getMessage());
        }
        return null;
    }

    public Object[] obtenerUsuarioCompletoPorEmail(String email) {
        String sql = """
                SELECT
                    p.dni,
                    p.nombre,
                    p.apellido_p,
                    p.apellido_m,
                    p.direccion,
                    p.telefono,
                    p.email
                FROM persona p
                INNER JOIN usuario u ON p.id_persona = u.id_persona
                WHERE p.email = ? AND u.rol = 'lector'
                """;

        try (Connection con = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Object[] usuario = new Object[7];
                usuario[0] = rs.getString("dni");
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("apellido_p");
                usuario[3] = rs.getString("apellido_m");
                usuario[4] = rs.getString("direccion");
                usuario[5] = rs.getString("telefono");
                usuario[6] = rs.getString("email");
                return usuario;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuario completo por email: " + e.getMessage());
        }
        return null;
    }

    // Agrega estos métodos a tu clase LectorDAO existente:

    /**
     * Actualiza los datos de un lector
     */
    public boolean actualizarLector(String dniOriginal, String dni, String nombre, String apellidoP,
            String apellidoM, String direccion, String telefono, String email,
            String nuevaContrasena) { // 1️⃣ Nuevo parámetro

        Connection con = null;
        PreparedStatement psPersona = null;
        PreparedStatement psUsuario = null;

        try {
            con = ConexionMySQL.getInstancia().getConexion();
            con.setAutoCommit(false); // 🔒 2️⃣ Iniciar Transacción

            // --- PASO 1: Actualizar datos personales (Tabla Persona) ---
            String sqlPersona = """
                    UPDATE persona
                    SET dni = ?, nombre = ?, apellido_p = ?, apellido_m = ?,
                        direccion = ?, telefono = ?, email = ?
                    WHERE dni = ?
                    """;
            // Nota: Quitamos el subquery complejo del WHERE para hacerlo más eficiente
            // dentro de la transacción, ya que validamos antes.

            psPersona = con.prepareStatement(sqlPersona);
            psPersona.setString(1, dni);
            psPersona.setString(2, nombre);
            psPersona.setString(3, apellidoP);
            psPersona.setString(4, apellidoM);
            psPersona.setString(5, direccion);
            psPersona.setString(6, telefono);
            psPersona.setString(7, email);
            psPersona.setString(8, dniOriginal);

            int filasPersona = psPersona.executeUpdate();

            if (filasPersona == 0) {
                con.rollback(); // Si no encontró a la persona, cancelamos todo
                return false;
            }

            // --- PASO 2: Actualizar contraseña (Tabla Usuario) - SOLO SI ES NECESARIO ---
            if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
                // Actualizamos la contraseña del usuario vinculado a ese DNI (ahora usamos el
                // 'dni' nuevo por si cambió)
                String sqlUsuario = """
                        UPDATE usuario u
                        INNER JOIN persona p ON u.id_persona = p.id_persona
                        SET u.contrasena = ?
                        WHERE p.dni = ? AND u.rol = 'lector'
                        """;

                psUsuario = con.prepareStatement(sqlUsuario);
                psUsuario.setString(1, nuevaContrasena); // La contraseña ya viene encriptada desde el botón
                psUsuario.setString(2, dni); // Usamos el NUEVO DNI, ya que el update de persona ya ocurrió arriba

                int filasUsuario = psUsuario.executeUpdate();

                // Opcional: Validar si encontró el usuario, aunque si persona existía, usuario
                // debería existir.
            }

            con.commit(); // ✅ Confirmar todos los cambios
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar lector: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback(); // 🔙 Revertir cambios si algo falló
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            // Cerrar recursos manualmente para evitar fugas de memoria
            try {
                if (psPersona != null)
                    psPersona.close();
                if (psUsuario != null)
                    psUsuario.close();
                if (con != null)
                    con.setAutoCommit(true); // Restaurar auto-commit
                // No cerramos 'con' aquí si usas un pool de conexiones compartido que se cierra
                // fuera,
                // pero si tu método getConexion devuelve una nueva cada vez, descomenta la
                // siguiente línea:
                // if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Elimina un lector por DNI
     */
    public boolean eliminarLector(String dni) {
        Connection conn = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psPersona = null;

        try {
            conn = ConexionMySQL.getInstancia().getConexion();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Primero eliminar el usuario
            String sqlUsuario = """
                    DELETE u FROM usuario u
                    INNER JOIN persona p ON u.id_persona = p.id_persona
                    WHERE p.dni = ? AND u.rol = 'lector'
                    """;
            psUsuario = conn.prepareStatement(sqlUsuario);
            psUsuario.setString(1, dni);
            int filasUsuario = psUsuario.executeUpdate();

            // 2. Luego eliminar la persona
            String sqlPersona = "DELETE FROM persona WHERE dni = ?";
            psPersona = conn.prepareStatement(sqlPersona);
            psPersona.setString(1, dni);
            int filasPersona = psPersona.executeUpdate();

            conn.commit(); // Confirmar transacción
            return filasUsuario > 0 && filasPersona > 0;

        } catch (SQLException e) {
            try {
                if (conn != null)
                    conn.rollback(); // Revertir en caso de error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("❌ Error al eliminar lector: " + e.getMessage());
            return false;
        } finally {
            try {
                if (psUsuario != null)
                    psUsuario.close();
                if (psPersona != null)
                    psPersona.close();
                if (conn != null)
                    conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica si un DNI ya existe (para validaciones)
     */
    public boolean existeDni(String dni) {
        String sql = "SELECT COUNT(*) FROM persona WHERE dni = ?";

        try (Connection con = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar DNI: " + e.getMessage());
        }
        return false;
    }

    /**
     * Verifica si un email ya existe (para validaciones)
     */
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM persona WHERE email = ?";

        try (Connection con = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar email: " + e.getMessage());
        }
        return false;
    }

}

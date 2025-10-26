package SistemaBibliotecario.Dao;

import SistemaBibliotecario.Conexion.ConexionMySQL;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class BibliotecarioDAO {
    
    public List<Object[]> listarBibliotecarios() {
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
            WHERE u.rol = 'bibliotecario';
        """;

        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
            System.err.println("❌ Error al listar bibliotecarios: " + e.getMessage());
        }
        return lista;
    }

    public List<Object[]> buscarBibliotecarioPorDniEmailNombre(String criterio) {
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
            WHERE u.rol = 'bibliotecario' 
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
            System.err.println("❌ Error al buscar bibliotecarios: " + e.getMessage());
        }
        return lista;
    }
}
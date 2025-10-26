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

}

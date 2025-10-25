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
                p.nombre, 
                CONCAT(p.apellido_p, ' ', p.apellido_m) AS apellidos,
                p.email,
                u.fecha_creacion AS fecha_ingreso,
                u.fecha_actualizacion AS ultimo_acceso
            FROM persona p
            INNER JOIN usuario u ON p.id_persona = u.id_persona
            WHERE u.rol = 'lector';
        """;

        try (Connection con = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("nombre");
                fila[1] = rs.getString("apellidos");
                fila[2] = rs.getString("email");
                fila[3] = rs.getTimestamp("fecha_ingreso");
                fila[4] = rs.getTimestamp("ultimo_acceso");
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar bibliotecarios: " + e.getMessage());
        }

        return lista;
    }
}

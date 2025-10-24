/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SistemaBibliotecario.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import SistemaBibliotecario.Conexion.ConexionMySQL;
import SistemaBibliotecario.Modelos.Persona;

/**
 *
 * @author User
 */
public class PersonaDAO {
    

    public int insertar(Persona persona) {
        String sql = "INSERT INTO persona (dni, nombre, apellido_p, apellido_m, direccion, telefono, email, fecha_creacion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        int idGenerado = -1;

        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, persona.getDni());
            ps.setString(2, persona.getNombre());
            ps.setString(3, persona.getApellidoP());
            ps.setString(4, persona.getApellidoM());
            ps.setString(5, persona.getDireccion());
            ps.setString(6, persona.getTelefono());
            ps.setString(7, persona.getEmail());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar persona: " + e.getMessage());
        }

        return idGenerado;
    }

}

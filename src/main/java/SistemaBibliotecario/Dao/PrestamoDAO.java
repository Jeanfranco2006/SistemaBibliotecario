package SistemaBibliotecario.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import SistemaBibliotecario.Conexion.ConexionMySQL;
import SistemaBibliotecario.Modelos.Prestamo;

public class PrestamoDAO {

    public boolean registrarPrestamo(String dniUsuario, String isbnLibro, String dniBibliotecario) {
        Connection conn = null;

        try {
            conn = ConexionMySQL.getInstancia().getConexion();
            conn.setAutoCommit(false); // 🔒 Iniciar transacción

            // === Buscar usuario ===
            PreparedStatement psUser = conn.prepareStatement(
                "SELECT u.id_usuario, p.nombre, p.apellido_p, p.apellido_m FROM usuario u " +
                "INNER JOIN persona p ON u.id_persona = p.id_persona WHERE p.dni = ?");
            psUser.setString(1, dniUsuario);
            ResultSet rsUser = psUser.executeQuery();
            if (!rsUser.next()) {
                JOptionPane.showMessageDialog(null, "⚠️ Usuario no encontrado.");
                return false;
            }
            int idUsuario = rsUser.getInt("id_usuario");
            String nombreUsuario = rsUser.getString("nombre") + " " + rsUser.getString("apellido_p") + " " + rsUser.getString("apellido_m");

            // === Bibliotecario ===
            PreparedStatement psBib = conn.prepareStatement(
                "SELECT u.id_usuario FROM usuario u INNER JOIN persona p ON u.id_persona = p.id_persona WHERE p.dni = ?");
            psBib.setString(1, dniBibliotecario);
            ResultSet rsBib = psBib.executeQuery();
            if (!rsBib.next()) {
                JOptionPane.showMessageDialog(null, "⚠️ Bibliotecario no encontrado.");
                return false;
            }
            int idBibliotecario = rsBib.getInt("id_usuario");

            // === Libro ===
            PreparedStatement psLibro = conn.prepareStatement("SELECT id_libro, titulo, stock FROM libro WHERE isbn = ?");
            psLibro.setString(1, isbnLibro);
            ResultSet rsLibro = psLibro.executeQuery();
            if (!rsLibro.next()) {
                JOptionPane.showMessageDialog(null, "⚠️ Libro no encontrado.");
                return false;
            }
            int idLibro = rsLibro.getInt("id_libro");
            String tituloLibro = rsLibro.getString("titulo");
            int stock = rsLibro.getInt("stock");
            if (stock <= 0) {
                JOptionPane.showMessageDialog(null, "❌ No hay stock disponible del libro.");
                return false;
            }

            // === Insertar en prestamo (con fecha de devolución automática +7 días) ===
            String sqlPrestamo = "INSERT INTO prestamo (id_usuario, id_bibliotecario, fecha_prestamo, fecha_devolucion, estado) " +
                                 "VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 'activo')";
            PreparedStatement psPrestamo = conn.prepareStatement(sqlPrestamo, Statement.RETURN_GENERATED_KEYS);
            psPrestamo.setInt(1, idUsuario);
            psPrestamo.setInt(2, idBibliotecario);
            psPrestamo.executeUpdate();

            ResultSet rsPrestamo = psPrestamo.getGeneratedKeys();
            int idPrestamo = 0;
            if (rsPrestamo.next()) idPrestamo = rsPrestamo.getInt(1);

            // === Detalle del préstamo ===
            String sqlDetalle = "INSERT INTO detalle_prestamo (id_prestamo, id_libro, cantidad) VALUES (?, ?, 1)";
            PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);
            psDetalle.setInt(1, idPrestamo);
            psDetalle.setInt(2, idLibro);
            psDetalle.executeUpdate();

            // === Actualizar stock ===
            PreparedStatement psStock = conn.prepareStatement("UPDATE libro SET stock = stock - 1 WHERE id_libro = ?");
            psStock.setInt(1, idLibro);
            psStock.executeUpdate();

            // Confirmar cambios
            conn.commit();
            JOptionPane.showMessageDialog(null, "✅ Préstamo registrado correctamente:\n" +
                    "Usuario: " + nombreUsuario + "\nLibro: " + tituloLibro + "\nDevolución en 7 días.");

            return true;

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            JOptionPane.showMessageDialog(null, "❌ Error: " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Prestamo> obtenerPrestamosPorBibliotecario(String dniBibliotecario) {
    List<Prestamo> prestamos = new ArrayList<>();
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        String sql = "SELECT " +
                    "p.id_prestamo, " +
                    "per_lector.dni as dni_lector, " +
                    "CONCAT(per_lector.nombre, ' ', per_lector.apellido_p, ' ', per_lector.apellido_m) as nombres_lector, " +
                    "l.titulo as titulo_libro, " +
                    "l.isbn, " +
                    "p.fecha_prestamo, " +
                    "p.fecha_devolucion, " +
                    "p.estado " +
                    "FROM prestamo p " +
                    "INNER JOIN usuario u_lector ON p.id_usuario = u_lector.id_usuario " +
                    "INNER JOIN persona per_lector ON u_lector.id_persona = per_lector.id_persona " +
                    "INNER JOIN usuario u_bibliotecario ON p.id_bibliotecario = u_bibliotecario.id_usuario " +
                    "INNER JOIN persona per_bibliotecario ON u_bibliotecario.id_persona = per_bibliotecario.id_persona " +
                    "INNER JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
                    "INNER JOIN libro l ON dp.id_libro = l.id_libro " +
                    "WHERE per_bibliotecario.dni = ? " +  // ← FILTRO POR DNI DEL BIBLIOTECARIO
                    "ORDER BY p.fecha_prestamo DESC";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, dniBibliotecario);  // ← PARÁMETRO DEL DNI
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Prestamo prestamo = new Prestamo();
            prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
            prestamo.setDniLector(rs.getString("dni_lector"));
            prestamo.setNombresLector(rs.getString("nombres_lector"));
            prestamo.setTituloLibro(rs.getString("titulo_libro"));
            prestamo.setIsbn(rs.getString("isbn"));
            prestamo.setFechaPrestamo(rs.getTimestamp("fecha_prestamo"));
            prestamo.setFechaDevolucion(rs.getTimestamp("fecha_devolucion"));
            prestamo.setEstado(rs.getString("estado"));
            
            prestamos.add(prestamo);
        }
        
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return prestamos;
}
}

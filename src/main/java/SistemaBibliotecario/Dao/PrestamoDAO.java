package SistemaBibliotecario.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        
        System.out.println("=== DEBUG: Buscando préstamos para bibliotecario DNI: " + dniBibliotecario + " ===");
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, dniBibliotecario);
        ResultSet rs = stmt.executeQuery();
        
        int count = 0;
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
            count++;
            
            System.out.println("Encontrado - ID: " + prestamo.getIdPrestamo() + 
                             ", Estado: " + prestamo.getEstado() + 
                             ", DNI Lector: " + prestamo.getDniLector());
        }
        
        System.out.println("Total préstamos encontrados en DAO: " + count);
        
    } catch (Exception e) {
        System.out.println("Error en obtenerPrestamosPorBibliotecario: " + e.getMessage());
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

public boolean registrarDevolucion(int idPrestamo, String dniBibliotecario) {
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        conn.setAutoCommit(false);

        // 1. Verificar el préstamo y calcular días de retraso (SIN FILTRAR POR BIBLIOTECARIO)
        String sqlVerificar = "SELECT p.id_prestamo, dp.id_libro, p.estado, p.fecha_devolucion, " +
                             "GREATEST(0, DATEDIFF(NOW(), p.fecha_devolucion)) as dias_retraso, " +
                             "per_lector.dni as dni_lector, l.titulo, u_lector.id_usuario as id_usuario, " +
                             "CONCAT(per_lector.nombre, ' ', per_lector.apellido_p) as nombre_lector, " +
                             "per_bib.dni as dni_bibliotecario_original " +  // Para mostrar info del bibliotecario original
                             "FROM prestamo p " +
                             "INNER JOIN usuario u_lector ON p.id_usuario = u_lector.id_usuario " +
                             "INNER JOIN persona per_lector ON u_lector.id_persona = per_lector.id_persona " +
                             "INNER JOIN usuario u_bib ON p.id_bibliotecario = u_bib.id_usuario " +
                             "INNER JOIN persona per_bib ON u_bib.id_persona = per_bib.id_persona " +
                             "INNER JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
                             "INNER JOIN libro l ON dp.id_libro = l.id_libro " +
                             "WHERE p.id_prestamo = ? " +  // ← QUITAMOS EL FILTRO POR BIBLIOTECARIO
                             "AND p.estado IN ('activo', 'vencido')";
        
        PreparedStatement psVerificar = conn.prepareStatement(sqlVerificar);
        psVerificar.setInt(1, idPrestamo);
        ResultSet rs = psVerificar.executeQuery();
        
        if (!rs.next()) {
            JOptionPane.showMessageDialog(null, "❌ Préstamo no encontrado o no está en estado activo/vencido.");
            return false;
        }
        
        int idLibro = rs.getInt("id_libro");
        int diasRetraso = rs.getInt("dias_retraso");
        String dniLector = rs.getString("dni_lector");
        String tituloLibro = rs.getString("titulo");
        String nombreLector = rs.getString("nombre_lector");
        int idUsuario = rs.getInt("id_usuario");
        String dniBibliotecarioOriginal = rs.getString("dni_bibliotecario_original");

        // 2. Si hay retraso, registrar multa en la tabla multa
        if (diasRetraso > 0) {
            double montoPorDia = 2.0;
            double montoTotal = diasRetraso * montoPorDia;
            
            // Insertar en la tabla multa
            String sqlMulta = "INSERT INTO multa (id_prestamo, monto, fecha_creacion, fecha_actualizacion) VALUES (?, ?, NOW(), NOW())";
            PreparedStatement psMulta = conn.prepareStatement(sqlMulta);
            psMulta.setInt(1, idPrestamo);
            psMulta.setDouble(2, montoTotal);
            psMulta.executeUpdate();
            
            System.out.println("Multa registrada: " + diasRetraso + " días de retraso, monto: S/ " + montoTotal);
        }

        // 3. Actualizar el estado del préstamo a 'devuelto'
        String sqlActualizarPrestamo = "UPDATE prestamo SET estado = 'devuelto', fecha_actualizacion = NOW() WHERE id_prestamo = ?";
        PreparedStatement psPrestamo = conn.prepareStatement(sqlActualizarPrestamo);
        psPrestamo.setInt(1, idPrestamo);
        psPrestamo.executeUpdate();

        // 4. Incrementar el stock del libro
        String sqlActualizarStock = "UPDATE libro SET stock = stock + 1 WHERE id_libro = ?";
        PreparedStatement psStock = conn.prepareStatement(sqlActualizarStock);
        psStock.setInt(1, idLibro);
        psStock.executeUpdate();

        conn.commit();
        
        // 5. Mostrar mensaje final con información del bibliotecario original
        String mensajeBibliotecario = "";
        if (!dniBibliotecarioOriginal.equals(dniBibliotecario)) {
            mensajeBibliotecario = "\n📝 Nota: Este préstamo fue registrado originalmente por otro bibliotecario.";
        }
        
        if (diasRetraso > 0) {
            JOptionPane.showMessageDialog(null, 
                "✅ Devolución registrada con RETRASO\n" +
                "Lector: " + nombreLector + " (" + dniLector + ")\n" +
                "Libro: " + tituloLibro + "\n" +
                "Días de retraso: " + diasRetraso + "\n" +
                "Multa aplicada: S/ " + (diasRetraso * 2.0) + "\n" +
                "Multa registrada en el sistema." + mensajeBibliotecario);
        } else {
            JOptionPane.showMessageDialog(null, 
                "✅ Devolución registrada correctamente.\n" +
                "Lector: " + nombreLector + " (" + dniLector + ")\n" +
                "Libro: " + tituloLibro + mensajeBibliotecario);
        }
        
        return true;

    } catch (Exception e) {
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException ex) {
            System.err.println("Error en rollback: " + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "❌ Error al registrar devolución: " + e.getMessage());
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

public int obtenerIdPrestamoPorDniEIsbn(String dniLector, String isbn, String dniBibliotecario) {
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        String sql = "SELECT p.id_prestamo " +
                    "FROM prestamo p " +
                    "INNER JOIN usuario u_lector ON p.id_usuario = u_lector.id_usuario " +
                    "INNER JOIN persona per_lector ON u_lector.id_persona = per_lector.id_persona " +
                    "INNER JOIN usuario u_bib ON p.id_bibliotecario = u_bib.id_usuario " +
                    "INNER JOIN persona per_bib ON u_bib.id_persona = per_bib.id_persona " +
                    "INNER JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
                    "INNER JOIN libro l ON dp.id_libro = l.id_libro " +
                    "WHERE per_lector.dni = ? AND l.isbn = ? " +
                    "AND p.estado IN ('activo', 'vencido')";  // ← QUITAMOS EL FILTRO POR BIBLIOTECARIO
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, dniLector);
        stmt.setString(2, isbn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_prestamo");
        }
        return -1;
        
    } catch (Exception e) {
        e.printStackTrace();
        return -1;
    } finally {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public boolean pagarMulta(int idMulta, String dniBibliotecario) {
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        conn.setAutoCommit(false);

        // 1. Verificar que la multa existe y no está pagada
        String sqlVerificar = "SELECT m.id_multa, m.monto, m.id_prestamo, p.id_usuario " +
                             "FROM multa m " +
                             "INNER JOIN prestamo p ON m.id_prestamo = p.id_prestamo " +
                             "WHERE m.id_multa = ? AND m.fecha_pago IS NULL";
        
        PreparedStatement psVerificar = conn.prepareStatement(sqlVerificar);
        psVerificar.setInt(1, idMulta);
        ResultSet rs = psVerificar.executeQuery();
        
        if (!rs.next()) {
            JOptionPane.showMessageDialog(null, "❌ Multa no encontrada o ya pagada.");
            return false;
        }
        
        double monto = rs.getDouble("monto");
        int idPrestamo = rs.getInt("id_prestamo");
        int idUsuario = rs.getInt("id_usuario");

        // 2. Registrar pago de multa (actualizar fecha_pago)
        String sqlPagar = "UPDATE multa SET fecha_pago = NOW(), fecha_actualizacion = NOW() WHERE id_multa = ?";
        PreparedStatement psPagar = conn.prepareStatement(sqlPagar);
        psPagar.setInt(1, idMulta);
        psPagar.executeUpdate();

        // 3. Desbloquear usuario
        String sqlDesbloquear = "UPDATE usuario SET estado = 'activo' WHERE id_usuario = ?";
        PreparedStatement psDesbloquear = conn.prepareStatement(sqlDesbloquear);
        psDesbloquear.setInt(1, idUsuario);
        psDesbloquear.executeUpdate();

        conn.commit();
        
        JOptionPane.showMessageDialog(null, 
            "✅ Multa pagada correctamente.\n" +
            "ID Multa: " + idMulta + "\n" +
            "Monto: S/ " + monto + "\n" +
            "Usuario desbloqueado.");
        return true;

    } catch (Exception e) {
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException ex) {
            System.err.println("Error en rollback: " + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "❌ Error al pagar multa: " + e.getMessage());
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

public List<Map<String, Object>> obtenerMultasPendientes() {
    List<Map<String, Object>> multas = new ArrayList<>();
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        String sql = "SELECT m.id_multa, m.monto, m.fecha_creacion, " +
                    "p.id_prestamo, per.dni, CONCAT(per.nombre, ' ', per.apellido_p) as nombre_usuario, " +
                    "l.titulo as libro " +
                    "FROM multa m " +
                    "INNER JOIN prestamo p ON m.id_prestamo = p.id_prestamo " +
                    "INNER JOIN usuario u ON p.id_usuario = u.id_usuario " +
                    "INNER JOIN persona per ON u.id_persona = per.id_persona " +
                    "INNER JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
                    "INNER JOIN libro l ON dp.id_libro = l.id_libro " +
                    "WHERE m.fecha_pago IS NULL " +
                    "ORDER BY m.fecha_creacion DESC";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Map<String, Object> multa = new HashMap<>();
            multa.put("id_multa", rs.getInt("id_multa"));
            multa.put("monto", rs.getDouble("monto"));
            multa.put("fecha_creacion", rs.getTimestamp("fecha_creacion"));
            multa.put("id_prestamo", rs.getInt("id_prestamo"));
            multa.put("dni", rs.getString("dni"));
            multa.put("nombre_usuario", rs.getString("nombre_usuario"));
            multa.put("libro", rs.getString("libro"));
            multas.add(multa);
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
    return multas;
}
public int verificarRetrasoPrestamo(int idPrestamo) {
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        String sql = "SELECT DATEDIFF(NOW(), fecha_devolucion) as dias_retraso " +
                    "FROM prestamo " +
                    "WHERE id_prestamo = ? " +
                    "AND estado IN ('activo', 'vencido') " +  // ← CAMBIADO
                    "AND NOW() > fecha_devolucion";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idPrestamo);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int retraso = rs.getInt("dias_retraso");
            return Math.max(retraso, 0);
        }
        return 0;
        
    } catch (Exception e) {
        e.printStackTrace();
        return 0;
    } finally {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Método para obtener TODOS los préstamos (sin filtrar por bibliotecario)
public List<Prestamo> obtenerTodosLosPrestamos() {
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
                    "p.estado " +  // Solo los campos que ya tienes en tu clase
                    "FROM prestamo p " +
                    "INNER JOIN usuario u_lector ON p.id_usuario = u_lector.id_usuario " +
                    "INNER JOIN persona per_lector ON u_lector.id_persona = per_lector.id_persona " +
                    "INNER JOIN usuario u_bibliotecario ON p.id_bibliotecario = u_bibliotecario.id_usuario " +
                    "INNER JOIN persona per_bibliotecario ON u_bibliotecario.id_persona = per_bibliotecario.id_persona " +
                    "LEFT JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
                    "LEFT JOIN libro l ON dp.id_libro = l.id_libro " +
                    "ORDER BY p.fecha_prestamo DESC";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
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

// Método para buscar préstamos por DNI, ISBN o estado
public List<Prestamo> buscarPrestamos(String criterio, String valor) {
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
                    "LEFT JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
                    "LEFT JOIN libro l ON dp.id_libro = l.id_libro " +
                    "WHERE ";
        
        // Construir la condición WHERE según el criterio
        switch (criterio) {
            case "dni":
                sql += "per_lector.dni LIKE ?";
                break;
            case "isbn":
                sql += "l.isbn LIKE ?";
                break;
            case "estado":
                sql += "p.estado = ?";
                break;
            default:
                sql += "per_lector.dni LIKE ?";
        }
        
        sql += " ORDER BY p.fecha_prestamo DESC";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        if (criterio.equals("estado")) {
            stmt.setString(1, valor);
        } else {
            stmt.setString(1, "%" + valor + "%");
        }
        
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

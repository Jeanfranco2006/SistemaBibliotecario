
package SistemaBibliotecario.Dao;

import SistemaBibliotecario.Conexion.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class DAO_LECTOR_MQ {

    private ConexionMySQL conexionMySQL;

    public DAO_LECTOR_MQ() {
        // CORRECCIÓN: Usar el método singleton para obtener la instancia
        this.conexionMySQL = ConexionMySQL.getInstancia();
    }

    public int obtenerIdUsuarioPorDni(String dni) {
        int idUsuario = -1; // Valor por defecto si no se encuentra
        String sql = "SELECT u.id_usuario FROM usuario u " +
                     "JOIN persona p ON u.id_persona = p.id_persona " +
                     "WHERE p.dni = ?";

        try (Connection conn = conexionMySQL.getConexion(); // CORRECCIÓN: Usar getConexion()
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idUsuario = rs.getInt("id_usuario");
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener ID de usuario por DNI: " + e.getMessage());
        }
        return idUsuario;
    }

    public DefaultTableModel obtenerPrestamosActivos(int idUsuario) {
        String[] columnNames = {"LIBRO", "FECHA DEVOLUCIÓN"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames);

        String sql = "SELECT l.titulo, p.fecha_devolucion " +
                     "FROM prestamo p " +
                     "JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
                     "JOIN libro l ON dp.id_libro = l.id_libro " +
                     "WHERE p.id_usuario = ? AND p.estado = 'activo'";

        try (Connection conn = conexionMySQL.getConexion(); // CORRECCIÓN: Usar getConexion()
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String tituloLibro = rs.getString("titulo");
                String fechaDevolucion = rs.getString("fecha_devolucion");
                model.addRow(new Object[]{tituloLibro, fechaDevolucion});
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener los préstamos activos: " + e.getMessage());
        }
        return model;
    }

    // Renewal: only if due date is in the future; set new due date = NOW() + 7 days
    public boolean renovarPrestamoPorTitulo(int idUsuario, String tituloLibro) {
        String sqlBuscar =
                "SELECT p.id_prestamo " +
                "FROM prestamo p " +
                "JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
                "JOIN libro l ON dp.id_libro = l.id_libro " +
                "WHERE p.id_usuario = ? AND l.titulo = ? AND p.estado = 'activo' AND p.fecha_devolucion > NOW() " +
                "LIMIT 1";

        String sqlActualizar =
                "UPDATE prestamo SET fecha_devolucion = DATE_ADD(NOW(), INTERVAL 7 DAY) WHERE id_prestamo = ?";

        try (Connection conn = conexionMySQL.getConexion();
             PreparedStatement psBuscar = conn.prepareStatement(sqlBuscar)) {

            psBuscar.setInt(1, idUsuario);
            psBuscar.setString(2, tituloLibro);
            ResultSet rs = psBuscar.executeQuery();
            if (!rs.next()) {
                return false; // no active loan or already expired
            }

            int idPrestamo = rs.getInt("id_prestamo");
            try (PreparedStatement psUpd = conn.prepareStatement(sqlActualizar)) {
                psUpd.setInt(1, idPrestamo);
                int filas = psUpd.executeUpdate();
                return filas > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al renovar prestamo: " + e.getMessage());
            return false;
        }
    }
}

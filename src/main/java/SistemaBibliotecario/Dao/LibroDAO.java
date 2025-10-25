package SistemaBibliotecario.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import SistemaBibliotecario.Conexion.ConexionMySQL;

public class LibroDAO {
    
    public boolean agregarLibro(String isbn, String titulo, int stock, String autor, int anioPublicacion, int idCategoria) {
        String sql = "INSERT INTO libro (isbn, titulo, stock, autor, anio_publicacion, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, isbn);
            ps.setString(2, titulo);
            ps.setInt(3, stock);
            ps.setString(4, autor);
            ps.setInt(5, anioPublicacion);
            ps.setInt(6, idCategoria);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al agregar libro: " + e.getMessage());
            return false;
        }
    }
    
    public List<Object[]> listarLibros() {
        List<Object[]> libros = new ArrayList<>();
        String sql = "SELECT l.isbn, l.titulo, c.nombre as categoria, l.autor, l.stock, l.anio_publicacion " +
                     "FROM libro l LEFT JOIN categoria c ON l.id_categoria = c.id_categoria";
        
        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getString("isbn"),
                    rs.getString("titulo"),
                    rs.getString("categoria"),
                    rs.getString("autor"),
                    rs.getInt("stock"),
                    rs.getInt("anio_publicacion")
                };
                libros.add(fila);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
        }
        
        return libros;
    }
    


    // En LibroDAO.java - agregar este método si no existe
public List<Object[]> obtenerCategorias() {
    List<Object[]> categorias = new ArrayList<>();
    String sql = "SELECT id_categoria, nombre FROM categoria ORDER BY nombre";
    
    try (Connection conn = ConexionMySQL.getInstancia().getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            Object[] fila = {
                rs.getInt("id_categoria"),
                rs.getString("nombre")
            };
            categorias.add(fila);
        }
        
    } catch (SQLException e) {
        System.err.println("Error al obtener categorías: " + e.getMessage());
    }
    
    return categorias;
}

// En LibroDAO.java - agrega estos métodos si no los tienes

public boolean agregarCategoria(String nombreCategoria) {
    String sql = "INSERT INTO categoria (nombre) VALUES (?)";
    
    try (Connection conn = ConexionMySQL.getInstancia().getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, nombreCategoria);
        return ps.executeUpdate() > 0;
        
    } catch (SQLException e) {
        System.err.println("Error al agregar categoría: " + e.getMessage());
        return false;
    }
}

public boolean actualizarLibro(String isbn, String titulo, int stock, String autor, int anioPublicacion, int idCategoria) {
    String sql = "UPDATE libro SET titulo = ?, stock = ?, autor = ?, anio_publicacion = ?, id_categoria = ? WHERE isbn = ?";
    
    try (Connection conn = ConexionMySQL.getInstancia().getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, titulo);
        ps.setInt(2, stock);
        ps.setString(3, autor);
        ps.setInt(4, anioPublicacion);
        ps.setInt(5, idCategoria);
        ps.setString(6, isbn);
        
        return ps.executeUpdate() > 0;
        
    } catch (SQLException e) {
        System.err.println("Error al actualizar libro: " + e.getMessage());
        return false;
    }
}

public boolean eliminarLibro(String isbn) {
    String sql = "DELETE FROM libro WHERE isbn = ?";
    
    try (Connection conn = ConexionMySQL.getInstancia().getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, isbn);
        return ps.executeUpdate() > 0;
        
    } catch (SQLException e) {
        System.err.println("Error al eliminar libro: " + e.getMessage());
        return false;
    }
}

public Object[] buscarLibroPorISBN(String isbn) {
    String sql = "SELECT l.isbn, l.titulo, l.autor, l.stock, l.anio_publicacion, l.id_categoria, c.nombre as categoria " +
                 "FROM libro l LEFT JOIN categoria c ON l.id_categoria = c.id_categoria WHERE l.isbn = ?";
    
    try (Connection conn = ConexionMySQL.getInstancia().getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, isbn);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return new Object[]{
                rs.getString("isbn"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getInt("stock"),
                rs.getInt("anio_publicacion"),
                rs.getInt("id_categoria"),
                rs.getString("categoria")
            };
        }
        
    } catch (SQLException e) {
        System.err.println("Error al buscar libro: " + e.getMessage());
    }
    
    return null;
}


}
package SistemaBibliotecario.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import SistemaBibliotecario.Conexion.ConexionMySQL;
import SistemaBibliotecario.Modelos.Libro;

public class LibroDAO {

    public boolean agregarLibro(String isbn, String titulo, int stock, String autor, int anioPublicacion,
            int idCategoria) {
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

    public boolean actualizarLibro(String isbn, String titulo, int stock, String autor, int anioPublicacion,
            int idCategoria) {
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
        String sql = "SELECT l.isbn, l.titulo, l.autor, l.stock, l.anio_publicacion, l.id_categoria, c.nombre as categoria "
                +
                "FROM libro l LEFT JOIN categoria c ON l.id_categoria = c.id_categoria WHERE l.isbn = ?";

        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Object[] {
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

    public List<Libro> obtenerLibrosDisponibles() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.id_libro, l.isbn, l.titulo, l.stock, l.autor, " +
                "l.anio_publicacion, l.id_categoria, l.fecha_creacion, l.fecha_actualizacion " +
                "FROM libro l WHERE l.stock > 0 ORDER BY l.titulo";

        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setIdLibro(rs.getInt("id_libro"));
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setStock(rs.getInt("stock"));
                libro.setAutor(rs.getString("autor"));
                libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
                libro.setIdCategoria(rs.getInt("id_categoria"));
                libro.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                libro.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));

                libros.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libros disponibles: " + e.getMessage());
            e.printStackTrace();
        }
        return libros;
    }

    public List<Object[]> obtenerLibrosDisponiblesConCategoria() {
        List<Object[]> libros = new ArrayList<>();
        String sql = "SELECT l.isbn, l.titulo, l.autor, l.stock, l.anio_publicacion, c.nombre as categoria " +
                "FROM libro l LEFT JOIN categoria c ON l.id_categoria = c.id_categoria " +
                "WHERE l.stock > 0 ORDER BY l.titulo";

        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = {
                        rs.getString("isbn"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("anio_publicacion"),
                        rs.getInt("stock"),
                        rs.getString("categoria") // Nombre real de la categoría
                };
                libros.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libros disponibles: " + e.getMessage());
            e.printStackTrace();
        }
        return libros;
    }

    public int contarLectoresActivos() {
        int total = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionMySQL.getInstancia().getConexion();

            // Consulta: cuenta los usuarios con rol lector que tienen préstamos activos
            String sql = """
                        SELECT COUNT(DISTINCT u.id_usuario) AS total
                        FROM usuario u
                        INNER JOIN prestamo p ON u.id_usuario = p.id_usuario
                        WHERE u.rol = 'lector' and u.ultimo_acceso is not null
                    """;

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al contar lectores activos: " + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return total;
    }

    // Método que devuelve el número de préstamos vigentes (activos o vencidos)
    public int contarPrestamosVigentes() {
        int total = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionMySQL.getInstancia().getConexion();

            // Consulta: cuenta los préstamos con estado activo o vencido
            String sql = """
                        SELECT COUNT(*) AS total
                        FROM prestamo
                        WHERE estado IN ('activo', 'vencido')
                    """;

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al contar préstamos vigentes: " + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return total;
    }

    public int contarLibros() {
        int total = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionMySQL.getInstancia().getConexion();
            String sql = "SELECT SUM(stock) AS total FROM libro"; // ✅ suma todas las existencias
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar libros: " + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return total;
    }

    public int contarLibrosPorISBN() {
        int total = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionMySQL.getInstancia().getConexion();
            String sql = "SELECT COUNT(DISTINCT isbn) AS total FROM libro";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar libros por ISBN: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return total;
    }

    public List<Libro> getLibrosPaginados(int pagina, int tamanoPagina) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT id_libro, titulo, autor FROM libro ORDER BY titulo LIMIT ? OFFSET ?";
        int offset = (pagina - 1) * tamanoPagina;

        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tamanoPagina);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setIdLibro(rs.getInt("id_libro"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libros.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libros paginados: " + e.getMessage());
        }
        return libros;
    }

    public Libro obtenerLibroPorId(int idLibro) {
        String sql = "SELECT * FROM libro WHERE id_libro = ?";
        Libro libro = null;

        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                libro = new Libro();
                libro.setIdLibro(rs.getInt("id_libro"));
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setStock(rs.getInt("stock"));
                libro.setAutor(rs.getString("autor"));
                libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
                libro.setIdCategoria(rs.getInt("id_categoria"));
                libro.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                libro.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));

                // Calcular disponibles
                String sqlPrestamos = "SELECT COUNT(*) FROM detalle_prestamo dp JOIN prestamo p ON dp.id_prestamo = p.id_prestamo WHERE dp.id_libro = ? AND p.estado IN ('activo', 'vencido')";
                try (PreparedStatement psPrestamos = conn.prepareStatement(sqlPrestamos)) {
                    psPrestamos.setInt(1, idLibro);
                    ResultSet rsPrestamos = psPrestamos.executeQuery();
                    if (rsPrestamos.next()) {
                        int prestados = rsPrestamos.getInt(1);
                        libro.setDisponibles(libro.getStock() - prestados);
                    } else {
                        libro.setDisponibles(libro.getStock());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libro por ID: " + e.getMessage());
        }
        return libro;
    }

    public List<Libro> buscarLibros(String textoBusqueda, int pagina, int tamanoPagina) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT id_libro, titulo, autor FROM libro WHERE titulo LIKE ? OR autor LIKE ? ORDER BY titulo LIMIT ? OFFSET ?";
        int offset = (pagina - 1) * tamanoPagina;

        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + textoBusqueda + "%");
            ps.setString(2, "%" + textoBusqueda + "%");
            ps.setInt(3, tamanoPagina);
            ps.setInt(4, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setIdLibro(rs.getInt("id_libro"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libros.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar libros: " + e.getMessage());
        }
        return libros;
    }

    public int contarLibros(String textoBusqueda) {
        String sql = "SELECT COUNT(*) FROM libro WHERE titulo LIKE ? OR autor LIKE ?";
        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + textoBusqueda + "%");
            ps.setString(2, "%" + textoBusqueda + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar libros: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Obtiene los primeros 5 libros como recomendados.
     * 
     * @return Una lista de arrays de objetos, donde cada array representa un libro
     *         con su tÃ­tulo y categorÃ­a.
     */
    public java.util.List<Object[]> obtenerRecomendados() {
        java.util.List<Object[]> recomendados = new java.util.ArrayList<>();
        String sql = "SELECT l.titulo, c.nombre as categoria FROM libro l JOIN categoria c ON l.id_categoria = c.id_categoria LIMIT 5";

        try (Connection conn = ConexionMySQL.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = {
                        rs.getString("titulo"),
                        rs.getString("categoria")
                };
                recomendados.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libros recomendados: " + e.getMessage());
        }

        return recomendados;
    }

}
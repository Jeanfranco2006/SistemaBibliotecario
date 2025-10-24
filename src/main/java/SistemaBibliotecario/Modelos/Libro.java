package SistemaBibliotecario.Modelos;

import java.sql.Timestamp;

public class Libro {
    private int idLibro;
    private String isbn;
    private String titulo;
    private int stock;
    private String autor;
    private int anioPublicacion;
    private int idCategoria;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    public Libro() {}

    public Libro(int idLibro, String isbn, String titulo, int stock, String autor,
                 int anioPublicacion, int idCategoria, Timestamp fechaCreacion, Timestamp fechaActualizacion) {
        this.idLibro = idLibro;
        this.isbn = isbn;
        this.titulo = titulo;
        this.stock = stock;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.idCategoria = idCategoria;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y setters
    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public int getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}

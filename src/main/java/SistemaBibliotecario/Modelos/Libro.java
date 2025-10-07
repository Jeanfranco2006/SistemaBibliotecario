/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SistemaBibliotecario.Modelos;

import java.time.LocalDateTime;

/**
 *
 * @author User
 */
public class Libro {
    private int id_libro;
    private String isbn;
    private String titulo;
    private int stock;
    private String autor;
    private int anio_publicacion;
    private LocalDateTime fecha_creacion;
    private int id_categoria;

    public Libro() {
    }

    public Libro(int id_libro, String isbn, String titulo, int stock, String autor,
                 int anio_publicacion, LocalDateTime fecha_creacion, int id_categoria) {
        this.id_libro = id_libro;
        this.isbn = isbn;
        this.titulo = titulo;
        this.stock = stock;
        this.autor = autor;
        this.anio_publicacion = anio_publicacion;
        this.fecha_creacion = fecha_creacion;
        this.id_categoria = id_categoria;
    }

    public int getId_libro() { return id_libro; }
    public void setId_libro(int id_libro) { this.id_libro = id_libro; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public int getAnio_publicacion() { return anio_publicacion; }
    public void setAnio_publicacion(int anio_publicacion) { this.anio_publicacion = anio_publicacion; }

    public LocalDateTime getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(LocalDateTime fecha_creacion) { this.fecha_creacion = fecha_creacion; }

    public int getId_categoria() { return id_categoria; }
    public void setId_categoria(int id_categoria) { this.id_categoria = id_categoria; }
}
package SistemaBibliotecario.Modelos;

import java.sql.Timestamp;

public class Prestamo {
    private int idPrestamo;
    private int idUsuario;
    private int idBibliotecario;
    private Timestamp fechaPrestamo;
    private Timestamp fechaDevolucion;
    private String estado;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;
    
    // Nuevos atributos para mostrar en la tabla
    private String dniLector;
    private String nombresLector;
    private String tituloLibro;
    private String isbn;

    public Prestamo() {}

    public Prestamo(int idPrestamo, int idUsuario, int idBibliotecario,
                    Timestamp fechaPrestamo, Timestamp fechaDevolucion, String estado,
                    Timestamp fechaCreacion, Timestamp fechaActualizacion) {
        this.idPrestamo = idPrestamo;
        this.idUsuario = idUsuario;
        this.idBibliotecario = idBibliotecario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y setters existentes
    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdBibliotecario() { return idBibliotecario; }
    public void setIdBibliotecario(int idBibliotecario) { this.idBibliotecario = idBibliotecario; }

    public Timestamp getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(Timestamp fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public Timestamp getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(Timestamp fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    // Nuevos getters y setters para los campos de visualización
    public String getDniLector() { return dniLector; }
    public void setDniLector(String dniLector) { this.dniLector = dniLector; }

    public String getNombresLector() { return nombresLector; }
    public void setNombresLector(String nombresLector) { this.nombresLector = nombresLector; }

    public String getTituloLibro() { return tituloLibro; }
    public void setTituloLibro(String tituloLibro) { this.tituloLibro = tituloLibro; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
}
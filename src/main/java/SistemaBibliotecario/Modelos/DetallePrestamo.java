package SistemaBibliotecario.Modelos;

import java.sql.Timestamp;

public class DetallePrestamo {
    private int idPrestamo;
    private int idLibro;
    private int cantidad;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    public DetallePrestamo() {}

    public DetallePrestamo(int idPrestamo, int idLibro, int cantidad,
                           Timestamp fechaCreacion, Timestamp fechaActualizacion) {
        this.idPrestamo = idPrestamo;
        this.idLibro = idLibro;
        this.cantidad = cantidad;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y setters
    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}

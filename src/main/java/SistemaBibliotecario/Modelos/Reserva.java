package SistemaBibliotecario.Modelos;

import java.sql.Timestamp;

public class Reserva {
    private int idReserva;
    private int idUsuario;
    private int idLibro;
    private Timestamp fechaReserva;
    private String estado;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    public Reserva() {}

    public Reserva(int idReserva, int idUsuario, int idLibro, Timestamp fechaReserva,
                   String estado, Timestamp fechaCreacion, Timestamp fechaActualizacion) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.fechaReserva = fechaReserva;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y setters
    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }

    public Timestamp getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(Timestamp fechaReserva) { this.fechaReserva = fechaReserva; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}

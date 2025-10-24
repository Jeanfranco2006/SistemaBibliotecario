package SistemaBibliotecario.Modelos;

import java.sql.Timestamp;

public class Usuario {
    private int idUsuario;
    private int idPersona;
    private String contrasena;
    private String rol;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    public Usuario() {}

    public Usuario(int idUsuario, int idPersona, String contrasena, String rol,
                   Timestamp fechaCreacion, Timestamp fechaActualizacion) {
        this.idUsuario = idUsuario;
        this.idPersona = idPersona;
        this.contrasena = contrasena;
        this.rol = rol;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdPersona() { return idPersona; }
    public void setIdPersona(int idPersona) { this.idPersona = idPersona; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}

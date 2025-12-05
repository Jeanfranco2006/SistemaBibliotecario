package SistemaBibliotecario.Modelos;

import java.sql.Timestamp;

public class Usuario {
    private int idUsuario;
    private int idPersona;
    private String contrasena;
    private String rol;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    // Campos extra para mostrar en la interfaz (opcional, como vimos antes)
    private String nombre;
    private String apellido;

    public Usuario() {
    }

    public Usuario(int idUsuario, int idPersona, String contrasena, String rol,
            Timestamp fechaCreacion, Timestamp fechaActualizacion) {
        this.idUsuario = idUsuario;
        this.idPersona = idPersona;
        this.contrasena = contrasena;
        this.rol = rol;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y Setters

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getContrasena() {
        return contrasena;
    }

    // ✅ Validación: Mínimo 6 caracteres
    public void setContrasena(String contrasena) {
        if (contrasena == null || contrasena.trim().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        if (rol == null || rol.trim().isEmpty()) {
            throw new IllegalArgumentException("El rol es obligatorio.");
        }
        // Opcional: Validar roles específicos
        // if (!rol.equalsIgnoreCase("lector") && !rol.equalsIgnoreCase("admin") ...)
        this.rol = rol.toLowerCase();
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y Setters para los campos extra de Sesión
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
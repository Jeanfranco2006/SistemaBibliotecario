package SistemaBibliotecario.Modelos;

import java.sql.Timestamp;

public class Persona {
    private int idPersona;
    private String dni;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String direccion;
    private String telefono;
    private String email;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    public Persona() {}

    public Persona(int idPersona, String dni, String nombre, String apellidoP, String apellidoM,
                   String direccion, String telefono, String email, Timestamp fechaCreacion, Timestamp fechaActualizacion) {
        this.idPersona = idPersona;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y setters
    public int getIdPersona() { return idPersona; }
    public void setIdPersona(int idPersona) { this.idPersona = idPersona; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoP() { return apellidoP; }
    public void setApellidoP(String apellidoP) { this.apellidoP = apellidoP; }

    public String getApellidoM() { return apellidoM; }
    public void setApellidoM(String apellidoM) { this.apellidoM = apellidoM; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}

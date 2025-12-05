package SistemaBibliotecario.Modelos;

import java.sql.Timestamp;
import java.util.regex.Pattern; // 1️⃣ Importamos Pattern para validaciones

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

    public Persona() {
    }

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

    // Getters y Setters con Validaciones

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getDni() {
        return dni;
    }

    // ✅ Validación: DNI debe tener 8 dígitos numéricos
    public void setDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe contener exactamente 8 dígitos numéricos.");
        }
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    // ✅ Validación: No vacío
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public String getApellidoP() {
        return apellidoP;
    }

    // ✅ Validación: No vacío
    public void setApellidoP(String apellidoP) {
        if (apellidoP == null || apellidoP.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido paterno es obligatorio.");
        }
        this.apellidoP = apellidoP.trim();
    }

    public String getApellidoM() {
        return apellidoM;
    }

    // ✅ Validación: No vacío
    public void setApellidoM(String apellidoM) {
        if (apellidoM == null || apellidoM.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido materno es obligatorio.");
        }
        this.apellidoM = apellidoM.trim();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        this.direccion = direccion.trim();
    }

    public String getTelefono() {
        return telefono;
    }

    // ✅ Validación: Solo números, entre 7 y 15 dígitos
    public void setTelefono(String telefono) {
        if (telefono == null || !telefono.matches("\\d{7,15}")) {
            throw new IllegalArgumentException("El teléfono debe contener solo números (mínimo 7 dígitos).");
        }
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    // ✅ Validación: Formato de correo electrónico
    public void setEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || !Pattern.compile(emailRegex).matcher(email).matches()) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }
        this.email = email.trim();
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
}
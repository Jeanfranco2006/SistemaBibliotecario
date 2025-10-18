/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SistemaBibliotecario.Modelos;

/**
 *
 * @author User
 */
public class Persona {
    private int id_persona;
    private String dni;
    private String nombre;
    private String apellido_p;
    private String apellido_m;
    private String direccion;
    private String telefono;
    private String email;

    public Persona() {
    }

    public Persona(int id_persona, String dni, String nombre, String apellido_p, String apellido_m,
                   String direccion, String telefono, String email) {
        this.id_persona = id_persona;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido_p = apellido_p;
        this.apellido_m = apellido_m;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    public int getId_persona() { return id_persona; }
    public void setId_persona(int id_persona) { this.id_persona = id_persona; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido_p() { return apellido_p; }
    public void setApellido_p(String apellido_p) { this.apellido_p = apellido_p; }

    public String getApellido_m() { return apellido_m; }
    public void setApellido_m(String apellido_m) { this.apellido_m = apellido_m; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

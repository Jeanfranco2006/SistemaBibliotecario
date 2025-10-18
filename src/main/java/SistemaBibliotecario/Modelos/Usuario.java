/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SistemaBibliotecario.Modelos;

/**
 *
 * @author User
 */
public class Usuario {
    private int id_usuario;
    private int id_persona;
    private String contrasena;
    private String rol; // administrador, bibliotecario, lector

    public Usuario() {
    }

    public Usuario(int id_usuario, int id_persona, String contrasena, String rol) {
        this.id_usuario = id_usuario;
        this.id_persona = id_persona;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public int getId_persona() { return id_persona; }
    public void setId_persona(int id_persona) { this.id_persona = id_persona; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
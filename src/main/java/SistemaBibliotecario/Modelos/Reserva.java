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
public class Reserva {
    private int id_reserva;
    private int id_usuario;
    private int id_libro;
    private LocalDateTime fecha_reserva;
    private String estado;

    public Reserva() {
    }

    public Reserva(int id_reserva, int id_usuario, int id_libro,
                   LocalDateTime fecha_reserva, String estado) {
        this.id_reserva = id_reserva;
        this.id_usuario = id_usuario;
        this.id_libro = id_libro;
        this.fecha_reserva = fecha_reserva;
        this.estado = estado;
    }

    public int getId_reserva() { return id_reserva; }
    public void setId_reserva(int id_reserva) { this.id_reserva = id_reserva; }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public int getId_libro() { return id_libro; }
    public void setId_libro(int id_libro) { this.id_libro = id_libro; }

    public LocalDateTime getFecha_reserva() { return fecha_reserva; }
    public void setFecha_reserva(LocalDateTime fecha_reserva) { this.fecha_reserva = fecha_reserva; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
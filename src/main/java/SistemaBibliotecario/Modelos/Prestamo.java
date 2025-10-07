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
public class Prestamo {
    private int id_prestamo;
    private int id_usuario;
    private int id_bibliotecario;
    private LocalDateTime fecha_prestamo;
    private LocalDateTime fecha_devolucion;
    private String estado;

    public Prestamo() {
    }

    public Prestamo(int id_prestamo, int id_usuario, int id_bibliotecario,
                    LocalDateTime fecha_prestamo, LocalDateTime fecha_devolucion, String estado) {
        this.id_prestamo = id_prestamo;
        this.id_usuario = id_usuario;
        this.id_bibliotecario = id_bibliotecario;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_devolucion = fecha_devolucion;
        this.estado = estado;
    }

    public int getId_prestamo() { return id_prestamo; }
    public void setId_prestamo(int id_prestamo) { this.id_prestamo = id_prestamo; }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public int getId_bibliotecario() { return id_bibliotecario; }
    public void setId_bibliotecario(int id_bibliotecario) { this.id_bibliotecario = id_bibliotecario; }

    public LocalDateTime getFecha_prestamo() { return fecha_prestamo; }
    public void setFecha_prestamo(LocalDateTime fecha_prestamo) { this.fecha_prestamo = fecha_prestamo; }

    public LocalDateTime getFecha_devolucion() { return fecha_devolucion; }
    public void setFecha_devolucion(LocalDateTime fecha_devolucion) { this.fecha_devolucion = fecha_devolucion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
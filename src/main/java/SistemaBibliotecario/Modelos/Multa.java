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
public class Multa {
    private int id_multa;
    private int id_prestamo;
    private double monto;
    private LocalDateTime fecha_pago;

    public Multa() {
    }

    public Multa(int id_multa, int id_prestamo, double monto, LocalDateTime fecha_pago) {
        this.id_multa = id_multa;
        this.id_prestamo = id_prestamo;
        this.monto = monto;
        this.fecha_pago = fecha_pago;
    }

    public int getId_multa() { return id_multa; }
    public void setId_multa(int id_multa) { this.id_multa = id_multa; }

    public int getId_prestamo() { return id_prestamo; }
    public void setId_prestamo(int id_prestamo) { this.id_prestamo = id_prestamo; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public LocalDateTime getFecha_pago() { return fecha_pago; }
    public void setFecha_pago(LocalDateTime fecha_pago) { this.fecha_pago = fecha_pago; }
}
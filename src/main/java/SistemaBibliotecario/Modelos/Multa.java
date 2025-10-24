package SistemaBibliotecario.Modelos;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Multa {
    private int idMulta;
    private int idPrestamo;
    private BigDecimal monto;
    private Timestamp fechaPago;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    public Multa() {}

    public Multa(int idMulta, int idPrestamo, BigDecimal monto, Timestamp fechaPago,
                 Timestamp fechaCreacion, Timestamp fechaActualizacion) {
        this.idMulta = idMulta;
        this.idPrestamo = idPrestamo;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y setters
    public int getIdMulta() { return idMulta; }
    public void setIdMulta(int idMulta) { this.idMulta = idMulta; }

    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public Timestamp getFechaPago() { return fechaPago; }
    public void setFechaPago(Timestamp fechaPago) { this.fechaPago = fechaPago; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}

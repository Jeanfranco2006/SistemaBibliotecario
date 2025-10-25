package SistemaBibliotecario.Modelos;

import java.sql.Timestamp;

public class Categoria {
    private int idCategoria;
    private String nombre;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    public Categoria() {}

    public Categoria(int idCategoria, String nombre, Timestamp fechaCreacion, Timestamp fechaActualizacion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y setters
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    // ✅ AGREGAR ESTE MÉTODO PARA QUE SE MUESTRE BIEN EN EL COMBOBOX
    @Override
    public String toString() {
        return nombre;
    }
}
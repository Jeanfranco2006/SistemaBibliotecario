package SistemaBibliotecario.Modelos;

public class SesionActual {
    public static String dni = null;
    public static String rol = null;
    public static String nombre = null; // ✅ AGREGAR ESTE CAMPO
    
    public static void cerrarSesion() {
        dni = null;
        rol = null;
        nombre = null;
    }
}
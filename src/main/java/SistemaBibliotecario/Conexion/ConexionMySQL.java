package SistemaBibliotecario.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    // Instancia única del Singleton
    private static ConexionMySQL instancia;
    private Connection conexion;

    // Datos de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String CLAVE = ""; // coloca tu contraseña si tienes

    // Constructor privado para evitar instancias externas
    private ConexionMySQL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Cargar el driver de MySQL
            conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("✅ Conexión exitosa a la base de datos 'biblioteca'.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    // Método público para obtener la instancia única
    public static ConexionMySQL getInstancia() {
        if (instancia == null) {
            instancia = new ConexionMySQL();
        }
        return instancia;
    }

    // Devuelve la conexión activa
    public Connection getConexion() {
    try {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    } catch (SQLException e) {
        System.err.println("Error al obtener conexión: " + e.getMessage());
        return null;
    }
}



    // Método opcional para cerrar la conexión
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}

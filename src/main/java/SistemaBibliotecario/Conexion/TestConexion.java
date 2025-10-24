package SistemaBibliotecario.Conexion;

import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        Connection conn = ConexionMySQL.getInstancia().getConexion();
        if (conn != null) {
            System.out.println("Conexión establecida correctamente ✅");
        } else {
            System.out.println("No se pudo establecer la conexión ❌");
        }
    }
}

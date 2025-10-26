package SistemaBibliotecario.Dao;

import SistemaBibliotecario.Modelos.Prestamo;
import SistemaBibliotecario.Modelos.SesionActual;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;

import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import SistemaBibliotecario.Conexion.ConexionMySQL;

public class ReporteDAO {

    public List<Prestamo> obtenerPrestamosPorFechaYBibliotecario(Date fechaDesde, Date fechaHasta, String dniBibliotecario) {
        List<Prestamo> prestamos = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = ConexionMySQL.getInstancia().getConexion();
            String sql = "SELECT " +
                        "p.id_prestamo, " +
                        "per_lector.dni as dni_lector, " +
                        "CONCAT(per_lector.nombre, ' ', per_lector.apellido_p, ' ', per_lector.apellido_m) as nombres_lector, " +
                        "l.titulo as titulo_libro, " +
                        "l.isbn, " +
                        "p.fecha_prestamo, " +
                        "p.fecha_devolucion, " +
                        "p.estado " +
                        "FROM prestamo p " +
                        "INNER JOIN usuario u_lector ON p.id_usuario = u_lector.id_usuario " +
                        "INNER JOIN persona per_lector ON u_lector.id_persona = per_lector.id_persona " +
                        "INNER JOIN usuario u_bibliotecario ON p.id_bibliotecario = u_bibliotecario.id_usuario " +
                        "INNER JOIN persona per_bibliotecario ON u_bibliotecario.id_persona = per_bibliotecario.id_persona " +
                        "LEFT JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
                        "LEFT JOIN libro l ON dp.id_libro = l.id_libro " +
                        "WHERE p.fecha_prestamo BETWEEN ? AND ? " +
                        "AND per_bibliotecario.dni = ? " +
                        "ORDER BY p.fecha_prestamo DESC";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new Timestamp(fechaDesde.getTime()));
            stmt.setTimestamp(2, new Timestamp(fechaHasta.getTime()));
            stmt.setString(3, dniBibliotecario);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                prestamo.setDniLector(rs.getString("dni_lector"));
                prestamo.setNombresLector(rs.getString("nombres_lector"));
                prestamo.setTituloLibro(rs.getString("titulo_libro"));
                prestamo.setIsbn(rs.getString("isbn"));
                prestamo.setFechaPrestamo(rs.getTimestamp("fecha_prestamo"));
                prestamo.setFechaDevolucion(rs.getTimestamp("fecha_devolucion"));
                prestamo.setEstado(rs.getString("estado"));
                
                prestamos.add(prestamo);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return prestamos;
    }

    // MÉTODO ÚNICO - sin duplicados
    public boolean generarReportePrestamosExcel(Date fechaDesde, Date fechaHasta, String dniBibliotecario, String rutaArchivo) {
        try {
            // Obtener los préstamos del rango de fechas Y del bibliotecario específico
            List<Prestamo> prestamos = obtenerPrestamosPorFechaYBibliotecario(fechaDesde, fechaHasta, dniBibliotecario);
            
            // Crear el libro de Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Mis Préstamos");
            
            // Crear estilo para el encabezado
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            
            // Crear fila de encabezado
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "DNI Lector", "Nombres Lector", "Libro", "ISBN", "Fecha Préstamo", "Fecha Devolución", "Estado"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Llenar datos
            int rowNum = 1;
            for (Prestamo prestamo : prestamos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(prestamo.getIdPrestamo());
                row.createCell(1).setCellValue(prestamo.getDniLector());
                row.createCell(2).setCellValue(prestamo.getNombresLector());
                row.createCell(3).setCellValue(prestamo.getTituloLibro());
                row.createCell(4).setCellValue(prestamo.getIsbn());
                row.createCell(5).setCellValue(prestamo.getFechaPrestamo().toString());
                row.createCell(6).setCellValue(
                    prestamo.getFechaDevolucion() != null ? prestamo.getFechaDevolucion().toString() : "Pendiente"
                );
                row.createCell(7).setCellValue(prestamo.getEstado());
            }
            
            // Autoajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Guardar archivo
            try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                workbook.write(fileOut);
            }
            
            workbook.close();
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para generar estadísticas (opcional)
    public String generarEstadisticasTexto(Date fechaDesde, Date fechaHasta, String dniBibliotecario) {
        List<Prestamo> prestamos = obtenerPrestamosPorFechaYBibliotecario(fechaDesde, fechaHasta, dniBibliotecario);
        
        long totalPrestamos = prestamos.size();
        long prestamosActivos = prestamos.stream().filter(p -> "activo".equals(p.getEstado())).count();
        long prestamosDevueltos = prestamos.stream().filter(p -> "devuelto".equals(p.getEstado())).count();
        long prestamosVencidos = prestamos.stream().filter(p -> "vencido".equals(p.getEstado())).count();
        
        return String.format(
            "REPORTE PERSONAL - MIS PRÉSTAMOS\n\n" +
            "Período: %s a %s\n" +
            "Total de Préstamos: %d\n" +
            "Préstamos Activos: %d\n" +
            "Préstamos Devueltos: %d\n" +
            "Préstamos Vencidos: %d",
            fechaDesde, fechaHasta, totalPrestamos, prestamosActivos, prestamosDevueltos, prestamosVencidos
        );
    }

 public List<Prestamo> obtenerPrestamosPorFechaYEstado(Date fechaDesde, Date fechaHasta, String estado) {
    List<Prestamo> prestamos = new ArrayList<>();
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        
        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "p.id_prestamo, " +
            "per_lector.dni as dni_lector, " +
            "CONCAT(per_lector.nombre, ' ', per_lector.apellido_p, ' ', per_lector.apellido_m) as nombres_lector, " +
            "l.titulo as titulo_libro, " +
            "l.isbn, " +
            "p.fecha_prestamo, " +
            "p.fecha_devolucion, " +
            "p.estado, " +
            "per_bibliotecario.nombre as nombre_bibliotecario " + // Agregar info del bibliotecario
            "FROM prestamo p " +
            "INNER JOIN usuario u_lector ON p.id_usuario = u_lector.id_usuario " +
            "INNER JOIN persona per_lector ON u_lector.id_persona = per_lector.id_persona " +
            "INNER JOIN usuario u_bibliotecario ON p.id_bibliotecario = u_bibliotecario.id_usuario " +
            "INNER JOIN persona per_bibliotecario ON u_bibliotecario.id_persona = per_bibliotecario.id_persona " +
            "LEFT JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
            "LEFT JOIN libro l ON dp.id_libro = l.id_libro " +
            "WHERE p.fecha_prestamo BETWEEN ? AND ? "
        );
        
        // Agregar condición de estado solo si no es null
        if (estado != null && !estado.isEmpty()) {
            sql.append("AND p.estado = ? ");
        }
        
        sql.append("ORDER BY p.fecha_prestamo DESC");
        
        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        stmt.setTimestamp(1, new Timestamp(fechaDesde.getTime()));
        stmt.setTimestamp(2, new Timestamp(fechaHasta.getTime()));
        
        // Si hay filtro por estado, establecer el parámetro
        if (estado != null && !estado.isEmpty()) {
            stmt.setString(3, estado);
        }
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Prestamo prestamo = new Prestamo();
            prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
            prestamo.setDniLector(rs.getString("dni_lector"));
            prestamo.setNombresLector(rs.getString("nombres_lector"));
            prestamo.setTituloLibro(rs.getString("titulo_libro"));
            prestamo.setIsbn(rs.getString("isbn"));
            prestamo.setFechaPrestamo(rs.getTimestamp("fecha_prestamo"));
            prestamo.setFechaDevolucion(rs.getTimestamp("fecha_devolucion"));
            prestamo.setEstado(rs.getString("estado"));
            
            prestamos.add(prestamo);
        }
        
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return prestamos;
}

public boolean generarReportePrestamosExcelTotal(Date fechaDesde, Date fechaHasta, String estado, String rutaArchivo) {
   try {
        // Obtener los préstamos
        List<Prestamo> prestamos = obtenerPrestamosPorFechaYEstado(fechaDesde, fechaHasta, estado);
        
        // Crear el libro de Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte Préstamos");
        
        // Crear estilo para el encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        
        // Crear fila de encabezado (más columnas)
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "ID Préstamo", 
            "DNI Lector", 
            "Nombres Lector", 
            "Libro", 
            "ISBN", 
            "Fecha Préstamo", 
            "Fecha Devolución", 
            "Estado"
        };
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos
        int rowNum = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        for (Prestamo prestamo : prestamos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(prestamo.getIdPrestamo());
            row.createCell(1).setCellValue(prestamo.getDniLector());
            row.createCell(2).setCellValue(prestamo.getNombresLector());
            row.createCell(3).setCellValue(prestamo.getTituloLibro());
            row.createCell(4).setCellValue(prestamo.getIsbn());
            row.createCell(5).setCellValue(dateFormat.format(prestamo.getFechaPrestamo()));
            
            // Fecha de devolución (puede ser null)
            if (prestamo.getFechaDevolucion() != null) {
                row.createCell(6).setCellValue(dateFormat.format(prestamo.getFechaDevolucion()));
            } else {
                row.createCell(6).setCellValue("Pendiente");
            }
            
            row.createCell(7).setCellValue(prestamo.getEstado());
        }
        
        // Autoajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Guardar archivo
        try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
            workbook.write(fileOut);
        }
        
        workbook.close();
        
        // Mostrar estadísticas en consola
        System.out.println("📊 Reporte generado: " + prestamos.size() + " préstamos encontrados");
        return true;
        
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

// Método para libros más populares (CORREGIDO)
public List<Object[]> obtenerLibrosPopulares(Date fechaDesde, Date fechaHasta, String categoria) {
    List<Object[]> libros = new ArrayList<>();
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        
        StringBuilder sql = new StringBuilder("""
            SELECT 
                l.titulo,
                l.autor,
                l.isbn,
                c.nombre as categoria,
                COUNT(dp.id_libro) as total_prestamos
            FROM libro l
            LEFT JOIN detalle_prestamo dp ON l.id_libro = dp.id_libro
            LEFT JOIN prestamo p ON dp.id_prestamo = p.id_prestamo
            LEFT JOIN categoria c ON l.id_categoria = c.id_categoria
            WHERE p.fecha_prestamo BETWEEN ? AND ?
            """);
        
        // Agregar filtro de categoría si se especifica
        if (categoria != null && !categoria.isEmpty()) {
            sql.append(" AND c.nombre = ?");
        }
        
        sql.append("""
            GROUP BY l.id_libro, l.titulo, l.autor, l.isbn, c.nombre
            ORDER BY total_prestamos DESC
            """);
        
        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        stmt.setTimestamp(1, new Timestamp(fechaDesde.getTime()));
        stmt.setTimestamp(2, new Timestamp(fechaHasta.getTime()));
        
        // Si hay filtro de categoría, establecer el parámetro
        if (categoria != null && !categoria.isEmpty()) {
            stmt.setString(3, categoria);
        }
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Object[] libro = new Object[5];
            libro[0] = rs.getString("titulo");
            libro[1] = rs.getString("autor");
            libro[2] = rs.getString("isbn");
            libro[3] = rs.getString("categoria");
            libro[4] = rs.getInt("total_prestamos");
            libros.add(libro);
        }
        
        System.out.println("📚 Libros populares encontrados: " + libros.size());
        
    } catch (Exception e) {
        System.err.println("❌ Error en obtenerLibrosPopulares: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return libros;
}

// Método para libros menos prestados (CORREGIDO)
public List<Object[]> obtenerLibrosMenosPrestados(Date fechaDesde, Date fechaHasta, String categoria) {
    List<Object[]> libros = new ArrayList<>();
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        
        StringBuilder sql = new StringBuilder("""
            SELECT 
                l.titulo,
                l.autor,
                l.isbn,
                c.nombre as categoria,
                COUNT(dp.id_libro) as total_prestamos
            FROM libro l
            LEFT JOIN detalle_prestamo dp ON l.id_libro = dp.id_libro
            LEFT JOIN prestamo p ON dp.id_prestamo = p.id_prestamo
            LEFT JOIN categoria c ON l.id_categoria = c.id_categoria
            WHERE p.fecha_prestamo BETWEEN ? AND ?
            """);
        
        // Agregar filtro de categoría si se especifica
        if (categoria != null && !categoria.isEmpty()) {
            sql.append(" AND c.nombre = ?");
        }
        
        sql.append("""
            GROUP BY l.id_libro, l.titulo, l.autor, l.isbn, c.nombre
            ORDER BY total_prestamos ASC
            """);
        
        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        stmt.setTimestamp(1, new Timestamp(fechaDesde.getTime()));
        stmt.setTimestamp(2, new Timestamp(fechaHasta.getTime()));
        
        // Si hay filtro de categoría, establecer el parámetro
        if (categoria != null && !categoria.isEmpty()) {
            stmt.setString(3, categoria);
        }
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Object[] libro = new Object[5];
            libro[0] = rs.getString("titulo");
            libro[1] = rs.getString("autor");
            libro[2] = rs.getString("isbn");
            libro[3] = rs.getString("categoria");
            libro[4] = rs.getInt("total_prestamos");
            libros.add(libro);
        }
        
        System.out.println("📚 Libros menos prestados encontrados: " + libros.size());
        
    } catch (Exception e) {
        System.err.println("❌ Error en obtenerLibrosMenosPrestados: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return libros;
}

// Método para generar Excel de libros (actualizado con categoría)
public boolean generarReporteLibrosExcel(Date fechaDesde, Date fechaHasta, boolean esPopular, String categoria, String rutaArchivo) {
    try {
        List<Object[]> libros;
        String tipoReporte;
        
        if (esPopular) {
            libros = obtenerLibrosPopulares(fechaDesde, fechaHasta, categoria);
            tipoReporte = "Libros Más Populares";
        } else {
            libros = obtenerLibrosMenosPrestados(fechaDesde, fechaHasta, categoria);
            tipoReporte = "Libros Menos Prestados";
        }
        
        // Crear el libro de Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(tipoReporte);
        
        // Crear estilo para el encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        
        // Crear fila de encabezado
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Título", "Autor", "ISBN", "Categoría", "Total Préstamos"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos
        int rowNum = 1;
        for (Object[] libro : libros) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(libro[0] != null ? libro[0].toString() : "");
            row.createCell(1).setCellValue(libro[1] != null ? libro[1].toString() : "");
            row.createCell(2).setCellValue(libro[2] != null ? libro[2].toString() : "");
            row.createCell(3).setCellValue(libro[3] != null ? libro[3].toString() : "");
            row.createCell(4).setCellValue(libro[4] != null ? Integer.parseInt(libro[4].toString()) : 0);
        }
        
        // Autoajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Guardar archivo
        try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
            workbook.write(fileOut);
        }
        
        workbook.close();
        return true;
        
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public List<String> obtenerTodasLasCategorias() {
    List<String> categorias = new ArrayList<>();
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        String sql = "SELECT nombre FROM categoria ORDER BY nombre";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            categorias.add(rs.getString("nombre"));
        }
        
        System.out.println("✅ Se encontraron " + categorias.size() + " categorías en la BD");
        
    } catch (Exception e) {
        System.err.println("❌ Error al obtener categorías: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return categorias;
}





//REPORTE USUARIOS
// Método para obtener TODOS los usuarios registrados en un período
public List<Object[]> obtenerTodosUsuariosRegistrados(Date fechaDesde, Date fechaHasta, String rolFiltro) {
    List<Object[]> usuarios = new ArrayList<>();
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        
        StringBuilder sql = new StringBuilder("""
            SELECT 
                p.dni,
                p.nombre,
                p.apellido_p,
                p.apellido_m,
                p.email,
                p.telefono,
                u.rol,
                u.fecha_creacion,
                u.ultimo_acceso
            FROM usuario u
            INNER JOIN persona p ON u.id_persona = p.id_persona
            WHERE u.fecha_creacion BETWEEN ? AND ?
            """);
        
        // Agregar filtro de rol si se especifica
        if (rolFiltro != null && !rolFiltro.equals("todos")) {
            sql.append(" AND u.rol = ?");
        }
        
        sql.append(" ORDER BY u.fecha_creacion DESC");
        
        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        stmt.setTimestamp(1, new Timestamp(fechaDesde.getTime()));
        stmt.setTimestamp(2, new Timestamp(fechaHasta.getTime()));
        
        if (rolFiltro != null && !rolFiltro.equals("todos")) {
            stmt.setString(3, rolFiltro);
        }
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Object[] usuario = new Object[9];
            usuario[0] = rs.getString("dni");
            usuario[1] = rs.getString("nombre");
            usuario[2] = rs.getString("apellido_p") + " " + rs.getString("apellido_m");
            usuario[3] = rs.getString("email");
            usuario[4] = rs.getString("telefono");
            usuario[5] = rs.getString("rol");
            usuario[6] = rs.getTimestamp("fecha_creacion");
            usuario[7] = rs.getTimestamp("ultimo_acceso");
            
            // Calcular estado basado en último acceso
            if (usuario[7] != null) {
                long diff = System.currentTimeMillis() - ((Timestamp) usuario[7]).getTime();
                long diasInactivo = diff / (1000 * 60 * 60 * 24);
                usuario[8] = diasInactivo > 30 ? "Inactivo" : "Activo";
            } else {
                usuario[8] = "Nunca accedió";
            }
            
            usuarios.add(usuario);
        }
        
        System.out.println("👥 Usuarios registrados encontrados: " + usuarios.size());
        
    } catch (Exception e) {
        System.err.println("❌ Error en obtenerTodosUsuariosRegistrados: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return usuarios;
}

// Método para obtener estadísticas de préstamos por usuario (solo lectores pueden tener préstamos)
public List<Object[]> obtenerEstadisticasPrestamosUsuarios(Date fechaDesde, Date fechaHasta) {
    List<Object[]> estadisticas = new ArrayList<>();
    Connection conn = null;
    
    try {
        conn = ConexionMySQL.getInstancia().getConexion();
        
        String sql = """
            SELECT 
                u_lector.id_usuario,
                p.dni,
                CONCAT(p.nombre, ' ', p.apellido_p, ' ', p.apellido_m) as nombre_completo,
                p.email,
                u_lector.rol,
                COUNT(pst.id_prestamo) as total_prestamos,
                MAX(pst.fecha_prestamo) as ultimo_prestamo,
                SUM(CASE WHEN pst.estado = 'devuelto' THEN 1 ELSE 0 END) as prestamos_devueltos,
                SUM(CASE WHEN pst.estado = 'activo' THEN 1 ELSE 0 END) as prestamos_activos,
                SUM(CASE WHEN pst.estado = 'vencido' THEN 1 ELSE 0 END) as prestamos_vencidos
            FROM usuario u_lector
            INNER JOIN persona p ON u_lector.id_persona = p.id_persona
            LEFT JOIN prestamo pst ON u_lector.id_usuario = pst.id_usuario
            WHERE u_lector.rol = 'lector'
            AND (pst.fecha_prestamo BETWEEN ? AND ? OR pst.fecha_prestamo IS NULL)
            GROUP BY u_lector.id_usuario, p.dni, nombre_completo, p.email, u_lector.rol
            ORDER BY total_prestamos DESC
            """;
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setTimestamp(1, new Timestamp(fechaDesde.getTime()));
        stmt.setTimestamp(2, new Timestamp(fechaHasta.getTime()));
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Object[] usuario = new Object[10];
            usuario[0] = rs.getString("dni");
            usuario[1] = rs.getString("nombre_completo");
            usuario[2] = rs.getString("email");
            usuario[3] = rs.getString("rol");
            usuario[4] = rs.getInt("total_prestamos");
            usuario[5] = rs.getInt("prestamos_devueltos");
            usuario[6] = rs.getInt("prestamos_activos");
            usuario[7] = rs.getInt("prestamos_vencidos");
            usuario[8] = rs.getTimestamp("ultimo_prestamo");
            usuario[9] = rs.getInt("total_prestamos") > 0 ? "Activo" : "Inactivo";
            
            estadisticas.add(usuario);
        }
        
        System.out.println("📊 Estadísticas de préstamos por usuario: " + estadisticas.size());
        
    } catch (Exception e) {
        System.err.println("❌ Error en obtenerEstadisticasPrestamosUsuarios: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return estadisticas;
}

// Método actualizado para generar Excel de usuarios
public boolean generarReporteUsuariosExcel(Date fechaDesde, Date fechaHasta, int tipoReporte, String rolFiltro, String rutaArchivo) {
    try {
        List<Object[]> usuarios;
        String tipoReporteStr;
        String[] headers;
        
        switch (tipoReporte) {
            case 1: // Todos los usuarios registrados
                usuarios = obtenerTodosUsuariosRegistrados(fechaDesde, fechaHasta, rolFiltro);
                tipoReporteStr = "Usuarios Registrados";
                headers = new String[]{"DNI", "Nombre", "Apellidos", "Email", "Teléfono", "Rol", "Fecha Registro", "Último Acceso", "Estado"};
                break;
            case 2: // Estadísticas de préstamos (solo lectores)
                usuarios = obtenerEstadisticasPrestamosUsuarios(fechaDesde, fechaHasta);
                tipoReporteStr = "Estadísticas de Préstamos por Usuario";
                headers = new String[]{"DNI", "Nombre Completo", "Email", "Rol", "Total Préstamos", "Devueltos", "Activos", "Vencidos", "Último Préstamo", "Estado"};
                break;
            default:
                throw new IllegalArgumentException("Tipo de reporte no válido");
        }
        
        // Crear el libro de Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(tipoReporteStr);
        
        // Crear estilo para el encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        
        // Crear fila de encabezado
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos
        int rowNum = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        for (Object[] usuario : usuarios) {
            Row row = sheet.createRow(rowNum++);
            
            for (int i = 0; i < usuario.length; i++) {
                if (usuario[i] instanceof Timestamp) {
                    row.createCell(i).setCellValue(dateFormat.format((Timestamp) usuario[i]));
                } else if (usuario[i] != null) {
                    row.createCell(i).setCellValue(usuario[i].toString());
                } else {
                    row.createCell(i).setCellValue("N/A");
                }
            }
        }
        
        // Autoajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Guardar archivo
        try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
            workbook.write(fileOut);
        }
        
        workbook.close();
        System.out.println("✅ Excel de usuarios generado con " + usuarios.size() + " registros");
        return true;
        
    } catch (Exception e) {
        System.err.println("❌ Error al generar Excel de usuarios: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
}
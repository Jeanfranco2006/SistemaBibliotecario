-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 05-12-2025 a las 21:33:10
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `biblioteca`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE `categoria` (
  `id_categoria` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`id_categoria`, `nombre`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(1, 'Literatura Infantil', '2025-10-26 16:10:49', '2025-10-26 16:10:49'),
(2, 'Novela', '2025-10-26 16:10:49', '2025-10-26 16:10:49'),
(3, 'Ciencia Ficción', '2025-10-26 16:10:49', '2025-10-26 16:10:49'),
(4, 'Clásicos', '2025-10-26 16:10:49', '2025-10-26 16:10:49'),
(5, 'Misterio', '2025-10-26 16:11:22', '2025-10-26 16:11:22'),
(6, 'zz', '2025-10-26 18:26:01', '2025-10-26 18:26:01'),
(7, 'Suspenso', '2025-10-26 21:45:53', '2025-10-26 21:45:53');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_prestamo`
--

CREATE TABLE `detalle_prestamo` (
  `id_prestamo` int(11) NOT NULL,
  `id_libro` int(11) NOT NULL,
  `cantidad` int(11) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `detalle_prestamo`
--

INSERT INTO `detalle_prestamo` (`id_prestamo`, `id_libro`, `cantidad`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(9, 63, 1, '2025-10-26 18:11:33', '2025-10-26 18:11:33'),
(10, 82, 1, '2025-10-26 21:50:14', '2025-10-26 21:50:14'),
(11, 74, 1, '2025-10-26 21:51:45', '2025-10-26 21:51:45'),
(12, 61, 1, '2025-10-26 21:56:59', '2025-10-26 21:56:59'),
(13, 62, 1, '2025-10-26 21:56:59', '2025-10-26 21:56:59'),
(14, 63, 1, '2025-10-26 21:56:59', '2025-10-26 21:56:59');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libro`
--

CREATE TABLE `libro` (
  `id_libro` int(11) NOT NULL,
  `isbn` varchar(13) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `stock` int(11) NOT NULL,
  `autor` varchar(50) NOT NULL,
  `anio_publicacion` int(11) NOT NULL,
  `id_categoria` int(11) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `libro`
--

INSERT INTO `libro` (`id_libro`, `isbn`, `titulo`, `stock`, `autor`, `anio_publicacion`, `id_categoria`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(61, '9780000000001', 'El Principito', 11, 'Antoine de Saint-Exupéry', 1943, 1, '2025-10-26 16:11:29', '2025-10-26 21:59:34'),
(62, '9780000000002', 'Cien Años de Soledad', 8, 'Gabriel García Márquez', 1967, 2, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(63, '9780000000003', '1984', 13, 'George Orwell', 1949, 3, '2025-10-26 16:11:29', '2025-12-05 20:32:18'),
(64, '9780000000004', 'Don Quijote de la Mancha', 5, 'Miguel de Cervantes', 1605, 1, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(65, '9780000000005', 'La Odisea', 8, 'infante', 2001, 4, '2025-10-26 16:11:29', '2025-10-26 18:23:40'),
(66, '9780000000006', 'Hamlet', 6, 'William Shakespeare', 1603, 3, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(67, '9780000000007', 'Crimen y Castigo', 4, 'Fiódor Dostoyevski', 1866, 6, '2025-10-26 16:11:29', '2025-10-26 18:26:04'),
(68, '9780000000008', 'Orgullo y Prejuicio', 11, 'Jane Austen', 1813, 2, '2025-10-26 16:11:29', '2025-10-26 17:29:28'),
(69, '9780000000009', 'El Gran Gatsby', 11, 'F. Scott Fitzgerald', 1925, 3, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(70, '9780000000010', 'Moby Dick', 4, 'Herman Melville', 1851, 4, '2025-10-26 16:11:29', '2025-10-26 17:31:14'),
(71, '9780000000011', 'La Metamorfosis', 8, 'Franz Kafka', 1915, 3, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(72, '9780000000012', 'El Hobbit', 10, 'J.R.R. Tolkien', 1937, 1, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(73, '9780000000013', 'Fahrenheit 451', 7, 'Ray Bradbury', 1953, 3, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(74, '9780000000014', 'Drácula', 5, 'Bram Stoker', 1897, 2, '2025-10-26 16:11:29', '2025-10-26 21:53:35'),
(75, '9780000000015', 'El Retrato de Dorian Gray', 6, 'Oscar Wilde', 1890, 2, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(76, '9780000000016', 'Alicia en el País de las Maravillas', 9, 'Lewis Carroll', 1865, 1, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(77, '9780000000017', 'Viaje al Centro de la Tierra', 7, 'Julio Verne', 1864, 4, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(78, '9780000000018', 'La Iliada', 4, 'Homero', -750, 4, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(79, '9780000000019', 'Los Miserables', 5, 'Victor Hugo', 1862, 2, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(80, '9780000000020', 'El Código Da Vinci', 12, 'Dan Brown', 2003, 5, '2025-10-26 16:11:29', '2025-10-26 16:11:29'),
(81, '9780000000005', 'La Odisea', 8, 'infante', 2001, 4, '2025-10-26 18:18:43', '2025-10-26 18:23:40'),
(82, '9780000000044', 'El maldito de las matematicas', 10, 'Shekpeare', 1942, 7, '2025-10-26 21:46:23', '2025-10-26 21:51:17');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `multa`
--

CREATE TABLE `multa` (
  `id_multa` int(11) NOT NULL,
  `id_prestamo` int(11) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `fecha_pago` timestamp NULL DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `multa`
--

INSERT INTO `multa` (`id_multa`, `id_prestamo`, `monto`, `fecha_pago`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(1, 12, 8.00, NULL, '2025-10-26 21:59:34', '2025-10-26 21:59:34'),
(2, 14, 92.00, NULL, '2025-12-05 20:32:18', '2025-12-05 20:32:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `persona`
--

CREATE TABLE `persona` (
  `id_persona` int(11) NOT NULL,
  `dni` varchar(8) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `apellido_p` varchar(30) NOT NULL,
  `apellido_m` varchar(30) NOT NULL,
  `direccion` varchar(50) NOT NULL,
  `telefono` varchar(9) NOT NULL,
  `email` varchar(50) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `persona`
--

INSERT INTO `persona` (`id_persona`, `dni`, `nombre`, `apellido_p`, `apellido_m`, `direccion`, `telefono`, `email`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(1, '12345678', 'Carlos', 'Ramírez', 'Gómez', 'Av. Los Olivos 123', '987654321', 'carlos.ramirez@example.com', '2025-10-26 16:12:54', '2025-10-26 16:12:54'),
(2, '74883636', 'María', 'Torres', 'Pérez', 'Jr. San Martín 456', '912345678', 'maria.torres@example.com', '2025-10-26 16:12:54', '2025-10-26 16:13:31'),
(3, '74883675', 'Luis', 'Fernández', 'Rojas', 'Calle Lima 789', '998877665', 'luis.fernandez@example.com', '2025-10-26 16:12:54', '2025-10-26 16:13:04'),
(4, '45678901', 'Ana', 'Mendoza', 'Castro', 'Av. Arequipa 321', '976543210', 'ana.mendoza@example.com', '2025-10-26 16:12:54', '2025-10-26 16:12:54'),
(5, '56789012', 'Jorge', 'Sánchez', 'Flores', 'Jr. Grau 654', '934561278', 'jorge.sanchez@example.com', '2025-10-26 16:12:54', '2025-10-26 16:12:54'),
(6, '74883633', 'Jean', 'Chamorro', 'Granados', 'Av. Arequipa 321', '976543210', 'jean@example.com', '2025-10-26 21:30:50', '2025-10-26 21:48:36'),
(7, '74883625', 'Jeanfranco', 'Chamorro', 'Granados', 'Lima - Ate', '987456321', 'jeanchamorro2006@gmail.com', '2025-12-05 20:29:59', '2025-12-05 20:29:59');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `prestamo`
--

CREATE TABLE `prestamo` (
  `id_prestamo` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_bibliotecario` int(11) NOT NULL,
  `fecha_prestamo` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_devolucion` timestamp NULL DEFAULT NULL,
  `estado` enum('activo','devuelto','vencido') NOT NULL DEFAULT 'activo',
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `prestamo`
--

INSERT INTO `prestamo` (`id_prestamo`, `id_usuario`, `id_bibliotecario`, `fecha_prestamo`, `fecha_devolucion`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(9, 5, 2, '2025-10-26 18:11:33', '2025-11-02 18:11:33', 'devuelto', '2025-10-26 18:11:33', '2025-10-26 18:14:02'),
(10, 4, 5, '2025-10-26 21:50:14', '2025-11-02 21:50:14', 'devuelto', '2025-10-26 21:50:14', '2025-10-26 21:51:17'),
(11, 4, 5, '2025-10-26 21:51:45', '2025-11-02 21:51:45', 'devuelto', '2025-10-26 21:51:45', '2025-10-26 21:53:35'),
(12, 3, 2, '2025-10-15 15:00:00', '2025-10-22 15:00:00', 'devuelto', '2025-10-26 21:56:01', '2025-10-26 21:59:34'),
(13, 3, 2, '2025-10-14 20:30:00', '2025-10-21 20:30:00', 'vencido', '2025-10-26 21:56:01', '2025-10-26 21:57:43'),
(14, 3, 2, '2025-10-13 14:00:00', '2025-10-20 14:00:00', 'devuelto', '2025-10-26 21:56:01', '2025-12-05 20:32:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reserva`
--

CREATE TABLE `reserva` (
  `id_reserva` int(11) NOT NULL,
  `id_usuario` int(11) DEFAULT NULL,
  `id_libro` int(11) DEFAULT NULL,
  `fecha_reserva` timestamp NOT NULL DEFAULT current_timestamp(),
  `estado` varchar(15) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL,
  `id_persona` int(11) NOT NULL,
  `contrasena` varchar(100) NOT NULL,
  `rol` enum('administrador','bibliotecario','lector') NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `ultimo_acceso` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `id_persona`, `contrasena`, `rol`, `fecha_creacion`, `fecha_actualizacion`, `ultimo_acceso`) VALUES
(1, 1, 'admin123', 'administrador', '2025-10-26 16:12:54', '2025-10-26 21:38:04', '2025-10-26 16:38:04'),
(2, 2, '$2a$10$NHb4YI9GdS7Tz56ZDTnwe.LNgZen22Dq.xxuP5whH27rDKEKQJWtK', 'bibliotecario', '2025-10-26 16:12:54', '2025-12-05 20:31:28', '2025-12-05 15:31:28'),
(3, 3, '12345678', 'lector', '2025-10-26 16:12:54', '2025-10-26 21:37:27', '2025-10-26 16:37:27'),
(4, 4, 'lector101', 'lector', '2025-10-26 16:12:54', '2025-10-26 18:55:45', '2025-10-26 13:55:45'),
(5, 5, '$2a$10$n1Fy.dPUp/UUCdckR9aUSu2nYTHoBE4W5crxQH4PCvvykIHJvmjHu', 'bibliotecario', '2025-10-26 16:12:54', '2025-12-05 20:30:34', '2025-10-26 16:49:08'),
(6, 6, '12345678', 'lector', '2025-10-26 21:30:50', '2025-10-26 21:30:50', NULL),
(7, 7, '$2a$10$QqMCZXblJXzt9FLNBnKXkubExopSZVAX77VVeeOyzV7OygteNkGeO', 'administrador', '2025-12-05 20:29:59', '2025-12-05 20:30:19', '2025-12-05 15:30:19');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`id_categoria`);

--
-- Indices de la tabla `detalle_prestamo`
--
ALTER TABLE `detalle_prestamo`
  ADD KEY `id_prestamo` (`id_prestamo`),
  ADD KEY `id_libro` (`id_libro`);

--
-- Indices de la tabla `libro`
--
ALTER TABLE `libro`
  ADD PRIMARY KEY (`id_libro`),
  ADD KEY `id_categoria` (`id_categoria`);

--
-- Indices de la tabla `multa`
--
ALTER TABLE `multa`
  ADD PRIMARY KEY (`id_multa`),
  ADD KEY `id_prestamo` (`id_prestamo`);

--
-- Indices de la tabla `persona`
--
ALTER TABLE `persona`
  ADD PRIMARY KEY (`id_persona`),
  ADD UNIQUE KEY `dni` (`dni`);

--
-- Indices de la tabla `prestamo`
--
ALTER TABLE `prestamo`
  ADD PRIMARY KEY (`id_prestamo`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_bibliotecario` (`id_bibliotecario`);

--
-- Indices de la tabla `reserva`
--
ALTER TABLE `reserva`
  ADD PRIMARY KEY (`id_reserva`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_libro` (`id_libro`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD KEY `id_persona` (`id_persona`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categoria`
--
ALTER TABLE `categoria`
  MODIFY `id_categoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `libro`
--
ALTER TABLE `libro`
  MODIFY `id_libro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=83;

--
-- AUTO_INCREMENT de la tabla `multa`
--
ALTER TABLE `multa`
  MODIFY `id_multa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `persona`
--
ALTER TABLE `persona`
  MODIFY `id_persona` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `prestamo`
--
ALTER TABLE `prestamo`
  MODIFY `id_prestamo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `reserva`
--
ALTER TABLE `reserva`
  MODIFY `id_reserva` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `detalle_prestamo`
--
ALTER TABLE `detalle_prestamo`
  ADD CONSTRAINT `detalle_prestamo_ibfk_1` FOREIGN KEY (`id_prestamo`) REFERENCES `prestamo` (`id_prestamo`),
  ADD CONSTRAINT `detalle_prestamo_ibfk_2` FOREIGN KEY (`id_libro`) REFERENCES `libro` (`id_libro`);

--
-- Filtros para la tabla `libro`
--
ALTER TABLE `libro`
  ADD CONSTRAINT `libro_ibfk_1` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id_categoria`);

--
-- Filtros para la tabla `multa`
--
ALTER TABLE `multa`
  ADD CONSTRAINT `multa_ibfk_1` FOREIGN KEY (`id_prestamo`) REFERENCES `prestamo` (`id_prestamo`);

--
-- Filtros para la tabla `prestamo`
--
ALTER TABLE `prestamo`
  ADD CONSTRAINT `prestamo_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  ADD CONSTRAINT `prestamo_ibfk_2` FOREIGN KEY (`id_bibliotecario`) REFERENCES `usuario` (`id_usuario`);

--
-- Filtros para la tabla `reserva`
--
ALTER TABLE `reserva`
  ADD CONSTRAINT `reserva_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  ADD CONSTRAINT `reserva_ibfk_2` FOREIGN KEY (`id_libro`) REFERENCES `libro` (`id_libro`);

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id_persona`);

DELIMITER $$
--
-- Eventos
--
CREATE DEFINER=`root`@`localhost` EVENT `actualizar_prestamos_vencidos` ON SCHEDULE EVERY 1 DAY STARTS '2025-10-26 11:56:19' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE prestamo 
    SET estado = 'vencido', 
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE estado = 'activo' 
    AND fecha_devolucion < CURRENT_TIMESTAMP$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

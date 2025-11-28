-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 28-11-2025 a las 21:00:05
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
(1, 'Literatura Infantil', '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(2, 'Novela', '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(3, 'Ciencia Ficción', '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(4, 'Clásicos', '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(5, 'Misterio', '2025-11-28 17:36:39', '2025-11-28 17:36:39');

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
(1, 16, 1, '2025-11-28 18:32:57', '2025-11-28 18:32:57'),
(2, 14, 1, '2025-11-28 19:27:37', '2025-11-28 19:27:37'),
(5, 7, 1, '2025-11-28 19:43:01', '2025-11-28 19:43:01');

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
(1, '9780000000001', 'El Principito', 10, 'Antoine de Saint-Exupéry', 1943, 1, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(2, '9780000000002', 'Cien Años de Soledad', 8, 'Gabriel García Márquez', 1967, 2, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(3, '9780000000003', '1984', 12, 'George Orwell', 1949, 3, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(4, '9780000000004', 'Don Quijote de la Mancha', 4, 'Miguel de Cervantes', 1605, 1, '2025-11-28 17:36:39', '2025-11-28 19:31:06'),
(5, '9780000000005', 'La Odisea', 7, 'Homero', -800, 4, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(6, '9780000000006', 'Hamlet', 6, 'William Shakespeare', 1603, 3, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(7, '9780000000007', 'Crimen y Castigo', 3, 'Fiódor Dostoyevski', 1866, 2, '2025-11-28 17:36:39', '2025-11-28 19:43:01'),
(8, '9780000000008', 'Orgullo y Prejuicio', 9, 'Jane Austen', 1813, 2, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(9, '9780000000009', 'El Gran Gatsby', 11, 'F. Scott Fitzgerald', 1925, 3, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(10, '9780000000010', 'Moby Dick', 3, 'Herman Melville', 1851, 4, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(11, '9780000000011', 'La Metamorfosis', 8, 'Franz Kafka', 1915, 3, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(12, '9780000000012', 'El Hobbit', 10, 'J.R.R. Tolkien', 1937, 1, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(13, '9780000000013', 'Fahrenheit 451', 7, 'Ray Bradbury', 1953, 3, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(14, '9780000000014', 'Drácula', 3, 'Bram Stoker', 1897, 2, '2025-11-28 17:36:39', '2025-11-28 19:37:30'),
(15, '9780000000015', 'El Retrato de Dorian Gray', 6, 'Oscar Wilde', 1890, 2, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(16, '9780000000016', 'Alicia en el País de las Maravillas', 8, 'Lewis Carroll', 1865, 1, '2025-11-28 17:36:39', '2025-11-28 18:32:57'),
(17, '9780000000017', 'Viaje al Centro de la Tierra', 7, 'Julio Verne', 1864, 4, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(18, '9780000000018', 'La Iliada', 4, 'Homero', -750, 4, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(19, '9780000000019', 'Los Miserables', 5, 'Victor Hugo', 1862, 2, '2025-11-28 17:36:39', '2025-11-28 17:36:39'),
(20, '9780000000020', 'El Código Da Vinci', 12, 'Dan Brown', 2003, 5, '2025-11-28 17:36:39', '2025-11-28 17:36:39');

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
(6, '74888812', 'Jean', 'c', 'g', 'Lima', '945623652', 'jeancg025@gmail.com', '2025-11-28 17:37:52', '2025-11-28 17:37:52'),
(9, '74883636', 'Jean', 'c', 'g', 'ss', '987456541', 'jhean@gmail.com', '2025-11-28 17:44:51', '2025-11-28 17:44:51'),
(10, '74883625', 'Lucia', 'F', 'G', 'Lima - Ate', '987789654', 'Lucia@gmail.com', '2025-11-28 18:22:57', '2025-11-28 18:22:57');

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
  `estado` varchar(15) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `prestamo`
--

INSERT INTO `prestamo` (`id_prestamo`, `id_usuario`, `id_bibliotecario`, `fecha_prestamo`, `fecha_devolucion`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(1, 9, 8, '2025-11-28 18:32:57', '2025-12-05 19:27:55', 'activo', '2025-11-28 18:32:57', '2025-11-28 19:27:55'),
(2, 9, 8, '2025-11-28 19:27:37', '2025-12-05 19:30:37', 'activo', '2025-11-28 19:27:37', '2025-11-28 19:30:37'),
(5, 9, 8, '2025-11-28 19:43:01', '2025-12-05 19:43:01', 'activo', '2025-11-28 19:43:01', '2025-11-28 19:43:01');

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
  `ultimo_acceso` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `id_persona`, `contrasena`, `rol`, `fecha_creacion`, `fecha_actualizacion`, `ultimo_acceso`) VALUES
(6, 6, '$2a$10$7GLtgE/mrx2WtIlziRpqpuqT8bsBbMahSPGWwbLf3oFjMy/.pXjFC', 'administrador', '2025-11-28 17:37:52', '2025-11-28 18:24:26', '2025-11-28 18:24:26'),
(8, 9, '$2a$10$V9wKIWwdMUrdrUI8Rk5Ck.Dx0EigLwbOUyY33LduPUKaZf9x.tc1C', 'bibliotecario', '2025-11-28 17:44:51', '2025-11-28 17:50:02', '2025-11-28 17:50:02'),
(9, 10, '$2a$10$ruEznKl2ptn9lyiQF39oFOtPKrwQXLRww9TnmUmWDBEoYhh4qZRpW', 'lector', '2025-11-28 18:22:57', '2025-11-28 19:53:12', '2025-11-28 19:53:12');

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
  ADD UNIQUE KEY `unique_isbn` (`isbn`),
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
  MODIFY `id_categoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `libro`
--
ALTER TABLE `libro`
  MODIFY `id_libro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `multa`
--
ALTER TABLE `multa`
  MODIFY `id_multa` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `persona`
--
ALTER TABLE `persona`
  MODIFY `id_persona` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `prestamo`
--
ALTER TABLE `prestamo`
  MODIFY `id_prestamo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `reserva`
--
ALTER TABLE `reserva`
  MODIFY `id_reserva` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

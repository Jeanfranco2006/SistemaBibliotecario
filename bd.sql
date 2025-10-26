
-- Tabla persona
CREATE TABLE persona (
  id_persona INT PRIMARY KEY AUTO_INCREMENT,
  dni VARCHAR(8) NOT NULL UNIQUE,
  nombre VARCHAR(30) NOT NULL,
  apellido_p VARCHAR(30) NOT NULL,
  apellido_m VARCHAR(30) NOT NULL,
  direccion VARCHAR(50) NOT NULL,
  telefono VARCHAR(9) NOT NULL,
  email VARCHAR(50) NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla usuario
CREATE TABLE usuario (
  id_usuario INT PRIMARY KEY AUTO_INCREMENT,
  id_persona INT NOT NULL,
  contrasena VARCHAR(100) NOT NULL,
  rol ENUM('administrador','bibliotecario','lector') NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_persona) REFERENCES persona(id_persona)
);

-- Tabla categoria
CREATE TABLE categoria (
  id_categoria INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(30) NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla libro
CREATE TABLE libro (
  id_libro INT PRIMARY KEY AUTO_INCREMENT,
  isbn VARCHAR(13) NOT NULL,
  titulo VARCHAR(100) NOT NULL,
  stock INT NOT NULL,
  autor VARCHAR(50) NOT NULL,
  anio_publicacion INT NOT NULL,
  id_categoria INT,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

-- Tabla prestamo (cabecera)
CREATE TABLE prestamo (
  id_prestamo INT PRIMARY KEY AUTO_INCREMENT,
  id_usuario INT NOT NULL,          -- Usuario que recibe el libro
  id_bibliotecario INT NOT NULL,    -- Bibliotecario que entrega el libro
  fecha_prestamo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_devolucion TIMESTAMP NULL,
  estado VARCHAR(15) NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
  FOREIGN KEY (id_bibliotecario) REFERENCES usuario(id_usuario)
);

create table devolucion (
  id_devolucion INT PRIMARY KEY AUTO_INCREMENT,
  id_prestamo INT NOT NULL,
  id_bibliotecario INT NOT NULL,
  estado VARCHAR(15) NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_prestamo) REFERENCES prestamo(id_prestamo),
  FOREIGN KEY (id_bibliotecario) REFERENCES usuario(id_usuario)
)

-- Tabla detalle_prestamo
CREATE TABLE detalle_prestamo (
  id_prestamo INT NOT NULL,
  id_libro INT NOT NULL,
  cantidad INT DEFAULT 1,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_prestamo) REFERENCES prestamo(id_prestamo),
  FOREIGN KEY (id_libro) REFERENCES libro(id_libro)
);

-- Tabla reserva
CREATE TABLE reserva (
  id_reserva INT PRIMARY KEY AUTO_INCREMENT,
  id_usuario INT,
  id_libro INT,
  fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  estado VARCHAR(15) NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
  FOREIGN KEY (id_libro) REFERENCES libro(id_libro)
);


-- Tabla multa
CREATE TABLE multa (
  id_multa INT PRIMARY KEY AUTO_INCREMENT,
  id_prestamo INT NOT NULL,
  monto DECIMAL(10,2) NOT NULL,
  fecha_pago TIMESTAMP NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_prestamo) REFERENCES prestamo(id_prestamo)
);


INSERT INTO libro (isbn, titulo, stock, autor, anio_publicacion, id_categoria)
VALUES
('9780000000001', 'El Principito', 10, 'Antoine de Saint-Exupéry', 1943, 1),
('9780000000002', 'Cien Años de Soledad', 8, 'Gabriel García Márquez', 1967, 2),
('9780000000003', '1984', 12, 'George Orwell', 1949, 3),
('9780000000004', 'Don Quijote de la Mancha', 5, 'Miguel de Cervantes', 1605, 1),
('9780000000005', 'La Odisea', 7, 'Homero', -800, 4),
('9780000000006', 'Hamlet', 6, 'William Shakespeare', 1603, 3),
('9780000000007', 'Crimen y Castigo', 4, 'Fiódor Dostoyevski', 1866, 2),
('9780000000008', 'Orgullo y Prejuicio', 9, 'Jane Austen', 1813, 2),
('9780000000009', 'El Gran Gatsby', 11, 'F. Scott Fitzgerald', 1925, 3),
('9780000000010', 'Moby Dick', 3, 'Herman Melville', 1851, 4),
('9780000000011', 'La Metamorfosis', 8, 'Franz Kafka', 1915, 3),
('9780000000012', 'El Hobbit', 10, 'J.R.R. Tolkien', 1937, 1),
('9780000000013', 'Fahrenheit 451', 7, 'Ray Bradbury', 1953, 3),
('9780000000014', 'Drácula', 5, 'Bram Stoker', 1897, 2),
('9780000000015', 'El Retrato de Dorian Gray', 6, 'Oscar Wilde', 1890, 2),
('9780000000016', 'Alicia en el País de las Maravillas', 9, 'Lewis Carroll', 1865, 1),
('9780000000017', 'Viaje al Centro de la Tierra', 7, 'Julio Verne', 1864, 4),
('9780000000018', 'La Iliada', 4, 'Homero', -750, 4),
('9780000000019', 'Los Miserables', 5, 'Victor Hugo', 1862, 2),
('9780000000020', 'El Código Da Vinci', 12, 'Dan Brown', 2003, 5);

ALTER TABLE libro
ADD CONSTRAINT unique_isbn UNIQUE (isbn);


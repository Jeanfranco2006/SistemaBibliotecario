
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

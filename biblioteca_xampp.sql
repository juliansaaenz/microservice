-- =====================================================================
--  Biblioteca SENA - Microservicios
--  Script para XAMPP (phpMyAdmin > Importar, o pestaña SQL > Continuar)
--
--  Son DOS bases de datos, una por microservicio:
--    biblioteca_libros     -> la usa SOLO libros-service    (puerto 8081)
--    biblioteca_prestamos  -> la usa SOLO prestamos-service (puerto 8082)
--
--  Ojo: prestamos.libro_id NO tiene llave foránea hacia libros.
--  Están en bases distintas a propósito: en microservicios cada servicio
--  es dueño de sus datos y la relación se valida por API, no por SQL.
-- =====================================================================

-- ---------------------------------------------------------------------
-- Base de datos de libros-service
-- ---------------------------------------------------------------------
DROP DATABASE IF EXISTS biblioteca_libros;
CREATE DATABASE biblioteca_libros CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biblioteca_libros;

CREATE TABLE libros (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    titulo       VARCHAR(255) NOT NULL,
    autor        VARCHAR(255) NOT NULL,
    anio         INT          NULL,
    disponibles  INT          NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    CONSTRAINT chk_disponibles CHECK (disponibles >= 0)
) ENGINE=InnoDB;

INSERT INTO libros (titulo, autor, anio, disponibles) VALUES
('Cien años de soledad',                 'Gabriel García Márquez', 1967, 3),
('La vorágine',                          'José Eustasio Rivera',   1924, 2),
('El coronel no tiene quien le escriba', 'Gabriel García Márquez', 1961, 1),
('Clean Code',                           'Robert C. Martin',       2008, 4),
('Delirio',                              'Laura Restrepo',         2004, 0);

-- ---------------------------------------------------------------------
-- Base de datos de prestamos-service
-- ---------------------------------------------------------------------
DROP DATABASE IF EXISTS biblioteca_prestamos;
CREATE DATABASE biblioteca_prestamos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biblioteca_prestamos;

CREATE TABLE prestamos (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    libro_id          BIGINT       NOT NULL,          -- id que vive en biblioteca_libros
    titulo_libro      VARCHAR(255) NOT NULL,          -- copia del título para mostrarlo
    estudiante        VARCHAR(255) NOT NULL,
    fecha_prestamo    DATE         NOT NULL,
    fecha_devolucion  DATE         NULL,
    estado            VARCHAR(20)  NOT NULL DEFAULT 'ACTIVO',  -- ACTIVO | DEVUELTO
    PRIMARY KEY (id),
    INDEX idx_prestamos_libro (libro_id)
) ENGINE=InnoDB;

-- Sin préstamos de ejemplo: el primero se registra en vivo en la exposición.

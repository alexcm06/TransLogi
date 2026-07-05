CREATE DATABASE translogi;
USE translogi;

-- ==========================
-- TABLA ROL
-- ==========================
CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE
);

-- ==========================
-- TABLA USUARIO
-- ==========================
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    id_rol INT NOT NULL,

    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (id_rol)
        REFERENCES rol(id_rol)
);

-- ==========================
-- TABLA CONDUCTOR
-- ==========================
CREATE TABLE conductor (
    id_conductor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    numero_licencia VARCHAR(50) NOT NULL UNIQUE,
    fecha_vencimiento_licencia DATE NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

-- ==========================
-- TABLA EMPRESA
-- ==========================
CREATE TABLE empresa (
    id_empresa INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    correo_contacto VARCHAR(150),
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

-- ==========================
-- TABLA UBICACION
-- ==========================
CREATE TABLE ubicacion (
    id_ubicacion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    direccion VARCHAR(255) NOT NULL
);

-- ==========================
-- TABLA ESTADO VIAJE
-- ==========================
CREATE TABLE estado_viaje (
    id_estado_viaje INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado VARCHAR(50) NOT NULL UNIQUE
);

-- ==========================
-- TABLA VIAJE
-- ==========================
CREATE TABLE viaje (
    id_viaje INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora_salida TIME NOT NULL,
    kilometros_recorridos DECIMAL(10,2),
    observaciones VARCHAR(500),

    id_empresa INT NOT NULL,
    id_conductor INT NOT NULL,
    id_origen INT NOT NULL,
    id_destino INT NOT NULL,
    id_estado_viaje INT NOT NULL,

    CONSTRAINT fk_viaje_empresa
        FOREIGN KEY (id_empresa)
        REFERENCES empresa(id_empresa),

    CONSTRAINT fk_viaje_conductor
        FOREIGN KEY (id_conductor)
        REFERENCES conductor(id_conductor),

    CONSTRAINT fk_viaje_origen
        FOREIGN KEY (id_origen)
        REFERENCES ubicacion(id_ubicacion),

    CONSTRAINT fk_viaje_destino
        FOREIGN KEY (id_destino)
        REFERENCES ubicacion(id_ubicacion),

    CONSTRAINT fk_viaje_estado
        FOREIGN KEY (id_estado_viaje)
        REFERENCES estado_viaje(id_estado_viaje)
);

-- ==========================
-- TABLA HISTORIAL ESTADO VIAJE
-- ==========================
CREATE TABLE historial_estado_viaje (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    fecha_cambio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion VARCHAR(255),

    id_viaje INT NOT NULL,
    id_estado_viaje INT NOT NULL,

    CONSTRAINT fk_historial_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viaje(id_viaje),

    CONSTRAINT fk_historial_estado
        FOREIGN KEY (id_estado_viaje)
        REFERENCES estado_viaje(id_estado_viaje)
);

-- ==========================
-- TABLA TIPO GASTO
-- ==========================
CREATE TABLE tipo_gasto (
    id_tipo_gasto INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo VARCHAR(50) NOT NULL UNIQUE
);

-- ==========================
-- TABLA GASTO
-- ==========================
CREATE TABLE gasto (
    id_gasto INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255),
    monto DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL,

    id_viaje INT NOT NULL,
    id_tipo_gasto INT NOT NULL,

    CONSTRAINT fk_gasto_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viaje(id_viaje),

    CONSTRAINT fk_gasto_tipo
        FOREIGN KEY (id_tipo_gasto)
        REFERENCES tipo_gasto(id_tipo_gasto)
);

-- ==========================
-- TABLA COMPROBANTE
-- ==========================
CREATE TABLE comprobante (
    id_comprobante INT AUTO_INCREMENT PRIMARY KEY,
    nombre_archivo VARCHAR(255) NOT NULL,
    url_firebase VARCHAR(500) NOT NULL,
    tipo_archivo VARCHAR(50),
    fecha_subida DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    id_gasto INT NOT NULL,

    CONSTRAINT fk_comprobante_gasto
        FOREIGN KEY (id_gasto)
        REFERENCES gasto(id_gasto)
);
    
-- ==========================
-- TABLA NOTIFICACION
-- ==========================
CREATE TABLE notificacion (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    mensaje VARCHAR(255) NOT NULL,
    fecha_envio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    leida BOOLEAN NOT NULL DEFAULT FALSE,

    id_usuario INT NOT NULL,

    CONSTRAINT fk_notificacion_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
);

ALTER TABLE viaje
CHANGE fecha fecha_programada DATE NOT NULL;

ALTER TABLE viaje
CHANGE hora_salida hora_programada TIME NOT NULL;

ALTER TABLE viaje
ADD fecha_hora_inicio DATETIME NULL;

ALTER TABLE viaje
ADD fecha_hora_fin DATETIME NULL;

ALTER TABLE conductor
ADD id_usuario INT UNIQUE;

ALTER TABLE conductor
ADD CONSTRAINT fk_conductor_usuario
FOREIGN KEY (id_usuario)
REFERENCES usuario(id_usuario);

ALTER TABLE viaje
ADD COLUMN cantidad_pasajeros INT NOT NULL DEFAULT 1
AFTER hora_programada;

CREATE TABLE tipo_equipaje (
    id_tipo_equipaje INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo VARCHAR(50) NOT NULL
);

CREATE TABLE equipaje_viaje (
    id_equipaje_viaje INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,

    id_viaje INT NOT NULL,
    id_tipo_equipaje INT NOT NULL,

    CONSTRAINT fk_equipaje_viaje_viaje
        FOREIGN KEY (id_viaje)
        REFERENCES viaje(id_viaje),

    CONSTRAINT fk_equipaje_viaje_tipo
        FOREIGN KEY (id_tipo_equipaje)
        REFERENCES tipo_equipaje(id_tipo_equipaje)
);

USE translogi;

-- ==========================
-- ROLES
-- ==========================
INSERT INTO rol (nombre_rol) VALUES
('Administrador'),
('Supervisor'),
('Conductor');

-- ==========================
-- USUARIOS
-- ==========================
INSERT INTO usuario (nombre, correo, contrasena, estado, id_rol) VALUES
('Administrador General','admin@translogi.com','123456',TRUE,1),
('Carlos Rodríguez','carlos@translogi.com','123456',TRUE,3),
('Luis Pérez','luis@translogi.com','123456',TRUE,3),
('Ana Mora','ana@translogi.com','123456',TRUE,2);

-- ==========================
-- CONDUCTORES
-- ==========================
INSERT INTO conductor
(nombre, telefono, numero_licencia, fecha_vencimiento_licencia, estado, id_usuario)
VALUES
('Carlos Rodríguez','88881111','LIC-1001','2028-05-15',TRUE,2),
('Luis Pérez','88882222','LIC-1002','2027-10-10',TRUE,3);

-- ==========================
-- EMPRESAS
-- ==========================
INSERT INTO empresa
(nombre, telefono, correo_contacto, estado)
VALUES
('Hospital CIMA','22221111','contacto@cima.com',TRUE),
('INS','22870000','transportes@ins.cr',TRUE),
('Hospital Metropolitano','25210000','info@metropolitano.com',TRUE);

-- ==========================
-- UBICACIONES
-- ==========================
INSERT INTO ubicacion (nombre,direccion)
VALUES
('Hospital CIMA','Escazú'),
('Hospital México','La Uruca'),
('Hospital Calderón Guardia','San José'),
('Aeropuerto Juan Santamaría','Alajuela'),
('Hospital Metropolitano','Lindora');

-- ==========================
-- ESTADOS
-- ==========================
INSERT INTO estado_viaje (nombre_estado)
VALUES
('Programado'),
('En proceso'),
('Finalizado'),
('Cancelado');

-- ==========================
-- VIAJES
-- ==========================
INSERT INTO viaje
(
fecha_programada,
hora_programada,
cantidad_pasajeros,
kilometros_recorridos,
observaciones,
fecha_hora_inicio,
fecha_hora_fin,
id_empresa,
id_conductor,
id_origen,
id_destino,
id_estado_viaje
)
VALUES
('2026-07-05','08:00:00',2,15.5,'Paciente estable','2026-07-05 08:05:00','2026-07-05 08:40:00',1,1,1,2,3),

('2026-07-05','10:00:00',1,NULL,'Pendiente de salida',NULL,NULL,2,2,2,3,1),

('2026-07-05','14:00:00',3,8.7,'Traslado interno','2026-07-05 14:05:00',NULL,3,1,3,5,2),

('2026-07-06','09:00:00',1,NULL,'Paciente adulto mayor',NULL,NULL,1,2,1,4,1),

('2026-07-06','15:30:00',2,NULL,'Traslado programado',NULL,NULL,2,1,4,2,1);

-- ==========================
-- HISTORIAL
-- ==========================
INSERT INTO historial_estado_viaje
(observacion,id_viaje,id_estado_viaje)
VALUES
('Viaje creado',1,1),
('Viaje iniciado',1,2),
('Viaje finalizado',1,3),
('Viaje creado',2,1),
('Viaje creado',3,1),
('Conductor inició recorrido',3,2);

-- ==========================
-- TIPOS DE GASTO
-- ==========================
INSERT INTO tipo_gasto (nombre_tipo)
VALUES
('Combustible'),
('Peaje'),
('Parqueo'),
('Alimentación');

-- ==========================
-- GASTOS
-- ==========================
INSERT INTO gasto
(descripcion,monto,fecha,id_viaje,id_tipo_gasto)
VALUES
('Gasolina',25000,'2026-07-05',1,1),
('Peaje Ruta 27',1500,'2026-07-05',1,2),
('Parqueo Hospital',2500,'2026-07-05',1,3),
('Gasolina',18000,'2026-07-05',3,1);

-- ==========================
-- COMPROBANTES
-- ==========================
INSERT INTO comprobante
(nombre_archivo,url_firebase,tipo_archivo,id_gasto)
VALUES
('factura1.jpg','https://firebase.com/factura1','image/jpeg',1),
('peaje.pdf','https://firebase.com/peaje','application/pdf',2),
('parqueo.jpg','https://firebase.com/parqueo','image/jpeg',3);

-- ==========================
-- NOTIFICACIONES
-- ==========================
INSERT INTO notificacion
(mensaje,id_usuario)
VALUES
('Tiene un nuevo viaje asignado.',2),
('Debe actualizar su licencia.',3),
('Nuevo reporte disponible.',1);

-- ==========================
-- TIPOS DE EQUIPAJE
-- ==========================
INSERT INTO tipo_equipaje (nombre_tipo)
VALUES
('Maleta'),
('Bolso'),
('Silla de ruedas'),
('Equipo médico');

-- ==========================
-- EQUIPAJE VIAJE
-- ==========================
INSERT INTO equipaje_viaje
(cantidad,id_viaje,id_tipo_equipaje)
VALUES
(2,1,1),
(1,1,4),
(1,2,2),
(1,3,3);


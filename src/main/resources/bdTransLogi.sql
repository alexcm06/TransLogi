-- ============================================================
-- Base de datos: translogi
-- ============================================================

CREATE DATABASE IF NOT EXISTS `translogi`
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `translogi`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
SET UNIQUE_CHECKS = 0;
SET SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO';

-- ============================================================
-- 1. TABLAS INDEPENDIENTES (sin llaves foráneas)
-- ============================================================

--
-- Tabla: `usuario`
--

DROP TABLE IF EXISTS `usuario`;

CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `username` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo` varchar(75) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono` varchar(25) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ruta_imagen` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `correo` (`correo`),
  KEY `ndx_correo` (`correo`),
  CONSTRAINT `usuario_chk_1` CHECK (regexp_like(`correo`,_utf8mb4'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `usuario` VALUES (1,'admin','$2b$10$BL/mCW9nk3.ugJOJulXMVutb/EmZ7/Sv.Sc.gvSqGhPnse6n5/YIG','Administrador','General','admin@translogi.com','8800-0001','https://i.pravatar.cc/150?img=1',1,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(2,'carlos','$2b$10$BL/mCW9nk3.ugJOJulXMVutb/EmZ7/Sv.Sc.gvSqGhPnse6n5/YIG','Carlos','Rodríguez','carlos@translogi.com','8888-1111','https://i.pravatar.cc/150?img=2',1,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(3,'luis','$2b$10$BL/mCW9nk3.ugJOJulXMVutb/EmZ7/Sv.Sc.gvSqGhPnse6n5/YIG','Luis','Pérez','luis@translogi.com','8888-2222','https://i.pravatar.cc/150?img=3',1,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(4,'ana','$2b$10$BL/mCW9nk3.ugJOJulXMVutb/EmZ7/Sv.Sc.gvSqGhPnse6n5/YIG','Ana','Mora','ana@translogi.com','8800-0004','https://i.pravatar.cc/150?img=4',1,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(5,'marco','$2b$10$BL/mCW9nk3.ugJOJulXMVutb/EmZ7/Sv.Sc.gvSqGhPnse6n5/YIG','Marco','Jiménez','marco@translogi.com','8888-3333','https://i.pravatar.cc/150?img=5',1,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(6,'sofia','$2b$10$BL/mCW9nk3.ugJOJulXMVutb/EmZ7/Sv.Sc.gvSqGhPnse6n5/YIG','Sofía','Vargas','sofia@translogi.com','8800-0006','https://i.pravatar.cc/150?img=6',1,'2026-07-20 00:02:50','2026-07-20 00:02:50');

--
-- Tabla: `rol`
--

DROP TABLE IF EXISTS `rol`;

CREATE TABLE `rol` (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `nombre_rol` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nombre_rol` (`nombre_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `rol` VALUES (1,'Administrador','2026-07-20 00:02:50','2026-07-20 00:02:50'),(2,'Supervisor','2026-07-20 00:02:50','2026-07-20 00:02:50'),(3,'Conductor','2026-07-20 00:02:50','2026-07-20 00:02:50');

--
-- Tabla: `empresa`
--

DROP TABLE IF EXISTS `empresa`;

CREATE TABLE `empresa` (
  `id_empresa` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `correo_contacto` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_empresa`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `ndx_nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `empresa` VALUES (1,'Hospital CIMA','2208-1000','contacto@cima.com',1,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(2,'INS - Instituto Nacional de Seguros','2287-6000','transportes@ins.cr',1,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(3,'Hospital Metropolitano','2521-0000','info@metropolitano.com',1,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(4,'Hospital San Juan de Dios','2547-8000','contacto@hsjd.cr',1,'2026-07-20 00:02:50','2026-07-20 00:02:50');

--
-- Tabla: `ubicacion`
--

DROP TABLE IF EXISTS `ubicacion`;

CREATE TABLE `ubicacion` (
  `id_ubicacion` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `direccion` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_ubicacion`),
  KEY `ndx_nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `ubicacion` VALUES (1,'Hospital CIMA','Escazú, San José Editado','2026-07-20 00:02:50','2026-07-20 16:53:07'),(2,'Hospital México','La Uruca, San José','2026-07-20 00:02:50','2026-07-20 00:02:50'),(3,'Hospital Calderón Guardia','Barrio Aranjuez, San José','2026-07-20 00:02:50','2026-07-20 00:02:50'),(4,'Aeropuerto Internacional Juan Santamaría','Alajuela','2026-07-20 00:02:50','2026-07-20 00:02:50'),(5,'Hospital Metropolitano','Lindora, Santa Ana','2026-07-20 00:02:50','2026-07-20 00:02:50'),(6,'Hospital San Juan de Dios','Paseo Colón, San José','2026-07-20 00:02:50','2026-07-20 00:02:50');

--
-- Tabla: `estado_viaje`
--

DROP TABLE IF EXISTS `estado_viaje`;

CREATE TABLE `estado_viaje` (
  `id_estado_viaje` int NOT NULL AUTO_INCREMENT,
  `nombre_estado` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_estado_viaje`),
  UNIQUE KEY `nombre_estado` (`nombre_estado`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `estado_viaje` VALUES (1,'Programado','2026-07-20 00:02:50','2026-07-20 00:02:50'),(2,'En proceso','2026-07-20 00:02:50','2026-07-20 00:02:50'),(3,'Finalizado','2026-07-20 00:02:50','2026-07-20 00:02:50'),(4,'Cancelado','2026-07-20 00:02:50','2026-07-20 00:02:50');

--
-- Tabla: `tipo_equipaje`
--

DROP TABLE IF EXISTS `tipo_equipaje`;

CREATE TABLE `tipo_equipaje` (
  `id_tipo_equipaje` int NOT NULL AUTO_INCREMENT,
  `nombre_tipo` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_tipo_equipaje`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `tipo_equipaje` VALUES (1,'Maleta','2026-07-20 00:02:50','2026-07-20 00:02:50'),(2,'Bolso','2026-07-20 00:02:50','2026-07-20 00:02:50'),(3,'Silla de ruedas','2026-07-20 00:02:50','2026-07-20 00:02:50'),(4,'Equipo médico','2026-07-20 00:02:50','2026-07-20 00:02:50');

--
-- Tabla: `tipo_gasto`
--

DROP TABLE IF EXISTS `tipo_gasto`;

CREATE TABLE `tipo_gasto` (
  `id_tipo_gasto` int NOT NULL AUTO_INCREMENT,
  `nombre_tipo` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_tipo_gasto`),
  UNIQUE KEY `nombre_tipo` (`nombre_tipo`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `tipo_gasto` VALUES (1,'Combustible','2026-07-20 00:02:50','2026-07-20 00:02:50'),(2,'Peaje','2026-07-20 00:02:50','2026-07-20 00:02:50'),(3,'Parqueo','2026-07-20 00:02:50','2026-07-20 00:02:50'),(4,'Alimentación','2026-07-20 00:02:50','2026-07-20 00:02:50');

-- ============================================================
-- 2. TABLAS QUE DEPENDEN DE LAS ANTERIORES (nivel 1)
-- ============================================================

--
-- Tabla: `conductor`  (depende de usuario)
--

DROP TABLE IF EXISTS `conductor`;

CREATE TABLE `conductor` (
  `id_conductor` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `numero_licencia` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_vencimiento_licencia` date NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `id_usuario` int DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_conductor`),
  UNIQUE KEY `numero_licencia` (`numero_licencia`),
  UNIQUE KEY `id_usuario` (`id_usuario`),
  KEY `ndx_numero_licencia` (`numero_licencia`),
  CONSTRAINT `conductor_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `conductor` VALUES (1,'Carlos Rodríguez','8888-1111','B1-1001-2020','2028-05-15',1,2,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(2,'Luis Pérez','8888-2222','B1-1002-2019','2027-10-10',1,3,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(3,'Marco Jiménez','8888-3333','B1-1003-2021','2029-02-20',1,5,'2026-07-20 00:02:50','2026-07-20 00:02:50');

--
-- Tabla: `usuario_rol`  (depende de usuario, rol)
--

DROP TABLE IF EXISTS `usuario_rol`;

CREATE TABLE `usuario_rol` (
  `id_usuario` int NOT NULL,
  `id_rol` int NOT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`,`id_rol`),
  KEY `fk_usuarioRol_rol` (`id_rol`),
  CONSTRAINT `usuario_rol_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `usuario_rol_ibfk_2` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `usuario_rol` VALUES (1,1,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(2,3,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(3,3,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(4,2,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(5,3,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(6,2,'2026-07-20 00:02:50','2026-07-20 00:02:50');

--
-- Tabla: `ruta`  (depende de rol)
--

DROP TABLE IF EXISTS `ruta`;

CREATE TABLE `ruta` (
  `id_ruta` int NOT NULL AUTO_INCREMENT,
  `ruta` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_rol` int DEFAULT NULL,
  `requiere_rol` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_ruta`),
  KEY `id_rol` (`id_rol`),
  CONSTRAINT `ruta_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`),
  CONSTRAINT `ruta_chk_1` CHECK (((`id_rol` is not null) or (`requiere_rol` = false)))
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `ruta` VALUES (44,'/js/**',NULL,0,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(45,'/css/**',NULL,0,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(46,'/webjars/**',NULL,0,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(47,'/login',NULL,0,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(48,'/error',NULL,0,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(49,'/acceso_denegado',NULL,0,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(50,'/usuario/nuevo',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(51,'/usuario/nuevo',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(52,'/usuario/guardar',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(53,'/usuario/guardar',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(54,'/usuario/modificar/**',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(55,'/usuario/modificar/**',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(56,'/usuario/eliminar/**',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(57,'/usuario/eliminar/**',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(58,'/rol/nuevo',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(59,'/rol/nuevo',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(60,'/rol/guardar',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(61,'/rol/guardar',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(62,'/rol/modificar/**',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(63,'/rol/modificar/**',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(64,'/empresa/nuevo',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(65,'/empresa/nuevo',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(66,'/empresa/guardar',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(67,'/empresa/guardar',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(68,'/empresa/modificar/**',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(69,'/empresa/modificar/**',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(70,'/empresa/eliminar/**',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(71,'/empresa/eliminar/**',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(72,'/usuario/listado',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(73,'/usuario/listado',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(74,'/rol/listado',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(75,'/rol/listado',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(76,'/empresa/listado',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(77,'/empresa/listado',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(78,'/conductor/listado',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(79,'/conductor/listado',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(80,'/conductor/nuevo',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(81,'/conductor/nuevo',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(82,'/conductor/guardar',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(83,'/conductor/guardar',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(84,'/conductor/modificar/**',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(85,'/conductor/modificar/**',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(86,'/conductor/eliminar/**',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(87,'/conductor/eliminar/**',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(88,'/ubicacion/listado',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(89,'/ubicacion/listado',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(90,'/ubicacion/nuevo',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(91,'/ubicacion/nuevo',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(92,'/ubicacion/guardar',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(93,'/ubicacion/guardar',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(94,'/ubicacion/modificar/**',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(95,'/ubicacion/modificar/**',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(96,'/ubicacion/eliminar/**',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(97,'/ubicacion/eliminar/**',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(98,'/estadoViaje/listado',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(99,'/estadoViaje/listado',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(100,'/estadoViaje/nuevo',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(101,'/estadoViaje/nuevo',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(102,'/estadoViaje/guardar',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(103,'/estadoViaje/guardar',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(104,'/viaje/listado',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(105,'/viaje/listado',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(106,'/viaje/nuevo',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(107,'/viaje/nuevo',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(108,'/viaje/guardar',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(109,'/viaje/guardar',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(110,'/viaje/modificar/**',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(111,'/viaje/modificar/**',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(112,'/viaje/eliminar/**',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(113,'/viaje/eliminar/**',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(114,'/viaje/reportes',1,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(115,'/viaje/reportes',2,1,'2026-07-20 16:48:45','2026-07-20 16:48:45'),(116,'/',1,1,'2026-07-21 18:26:03','2026-07-21 18:26:03'),(117,'/index',1,1,'2026-07-21 18:26:03','2026-07-21 18:26:03'),(118,'/',2,1,'2026-07-21 18:26:03','2026-07-21 18:26:03'),(119,'/index',2,1,'2026-07-21 18:26:03','2026-07-21 18:26:03');

--
-- Tabla: `notificacion`  (depende de usuario)
--

DROP TABLE IF EXISTS `notificacion`;

CREATE TABLE `notificacion` (
  `id_notificacion` int NOT NULL AUTO_INCREMENT,
  `mensaje` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_envio` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `leida` tinyint(1) NOT NULL DEFAULT '0',
  `id_usuario` int NOT NULL,
  PRIMARY KEY (`id_notificacion`),
  KEY `ndx_id_usuario` (`id_usuario`),
  CONSTRAINT `notificacion_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `notificacion` VALUES (1,'Tiene un nuevo viaje asignado.','2026-07-19 18:02:50',0,2),(2,'Debe actualizar su licencia próximamente.','2026-07-19 18:02:50',0,3),(3,'Nuevo reporte de viajes disponible.','2026-07-19 18:02:50',0,1),(4,'Se canceló el viaje #7.','2026-07-19 18:02:50',0,4);

-- ============================================================
-- 3. TABLA VIAJE (nivel 2, depende de empresa, conductor, ubicacion, estado_viaje)
-- ============================================================

--
-- Tabla: `viaje`
--

DROP TABLE IF EXISTS `viaje`;

CREATE TABLE `viaje` (
  `id_viaje` int NOT NULL AUTO_INCREMENT,
  `fecha_programada` date NOT NULL,
  `hora_programada` time NOT NULL,
  `cantidad_pasajeros` int NOT NULL DEFAULT '1',
  `kilometros_recorridos` decimal(10,2) DEFAULT NULL,
  `observaciones` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_empresa` int NOT NULL,
  `id_conductor` int NOT NULL,
  `id_origen` int NOT NULL,
  `id_destino` int NOT NULL,
  `id_estado_viaje` int NOT NULL,
  `fecha_hora_inicio` datetime DEFAULT NULL,
  `fecha_hora_fin` datetime DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_viaje`),
  KEY `ndx_fecha_programada` (`fecha_programada`),
  KEY `ndx_id_conductor` (`id_conductor`),
  KEY `fk_viaje_empresa` (`id_empresa`),
  KEY `fk_viaje_origen` (`id_origen`),
  KEY `fk_viaje_destino` (`id_destino`),
  KEY `fk_viaje_activo` (`id_estado_viaje`),
  CONSTRAINT `viaje_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id_empresa`),
  CONSTRAINT `viaje_ibfk_2` FOREIGN KEY (`id_conductor`) REFERENCES `conductor` (`id_conductor`),
  CONSTRAINT `viaje_ibfk_3` FOREIGN KEY (`id_origen`) REFERENCES `ubicacion` (`id_ubicacion`),
  CONSTRAINT `viaje_ibfk_4` FOREIGN KEY (`id_destino`) REFERENCES `ubicacion` (`id_ubicacion`),
  CONSTRAINT `viaje_ibfk_5` FOREIGN KEY (`id_estado_viaje`) REFERENCES `estado_viaje` (`id_estado_viaje`),
  CONSTRAINT `viaje_chk_1` CHECK ((`cantidad_pasajeros` > 0)),
  CONSTRAINT `viaje_chk_2` CHECK ((`kilometros_recorridos` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `viaje` VALUES (1,'2026-07-04','09:00:00',2,12.40,'Paciente estable, traslado sin novedades',1,1,1,2,3,'2026-07-04 09:05:00','2026-07-04 09:45:00','2026-07-20 00:02:50','2026-07-20 00:02:50'),(2,'2026-07-05','10:00:00',1,NULL,'Pendiente de salida',2,2,2,3,1,NULL,NULL,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(3,'2026-07-05','14:00:00',3,8.70,'Traslado interno entre hospitales',3,1,3,5,2,'2026-07-05 14:05:00',NULL,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(4,'2026-07-06','09:00:00',1,NULL,'Paciente adulto mayor, requiere asistencia',1,2,1,4,1,NULL,NULL,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(5,'2026-07-06','15:30:00',2,NULL,'Traslado programado de rutina',2,1,4,2,1,NULL,NULL,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(6,'2026-07-08','07:30:00',3,NULL,'Traslado de tres pacientes',1,1,2,4,1,NULL,NULL,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(7,'2026-07-09','06:00:00',1,15.20,'Traslado cancelado por el cliente',4,3,6,1,4,NULL,NULL,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(8,'2026-07-10','11:00:00',2,NULL,'Traslado con silla de ruedas',3,3,5,6,1,NULL,NULL,'2026-07-20 00:02:50','2026-07-20 00:02:50');

-- ============================================================
-- 4. TABLAS QUE DEPENDEN DE VIAJE (nivel 3)
-- ============================================================

--
-- Tabla: `gasto`  (depende de viaje, tipo_gasto)
--

DROP TABLE IF EXISTS `gasto`;

CREATE TABLE `gasto` (
  `id_gasto` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `monto` decimal(10,2) NOT NULL,
  `fecha` date NOT NULL,
  `id_viaje` int NOT NULL,
  `id_tipo_gasto` int NOT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_gasto`),
  KEY `ndx_id_viaje` (`id_viaje`),
  KEY `fk_gasto_tipo` (`id_tipo_gasto`),
  CONSTRAINT `gasto_ibfk_1` FOREIGN KEY (`id_viaje`) REFERENCES `viaje` (`id_viaje`),
  CONSTRAINT `gasto_ibfk_2` FOREIGN KEY (`id_tipo_gasto`) REFERENCES `tipo_gasto` (`id_tipo_gasto`),
  CONSTRAINT `gasto_chk_1` CHECK ((`monto` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `gasto` VALUES (1,'Gasolina',25000.00,'2026-07-04',1,1,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(2,'Peaje Ruta 27',1500.00,'2026-07-04',1,2,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(3,'Parqueo Hospital México',2500.00,'2026-07-04',1,3,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(4,'Gasolina',18000.00,'2026-07-05',3,1,'2026-07-20 00:02:50','2026-07-20 00:02:50'),(5,'Almuerzo conductor',4500.00,'2026-07-05',3,4,'2026-07-20 00:02:50','2026-07-20 00:02:50');

--
-- Tabla: `equipaje_viaje`  (depende de viaje, tipo_equipaje)
--

DROP TABLE IF EXISTS `equipaje_viaje`;

CREATE TABLE `equipaje_viaje` (
  `id_equipaje_viaje` int NOT NULL AUTO_INCREMENT,
  `cantidad` int NOT NULL,
  `id_viaje` int NOT NULL,
  `id_tipo_equipaje` int NOT NULL,
  PRIMARY KEY (`id_equipaje_viaje`),
  KEY `fk_equipajeViaje_viaje` (`id_viaje`),
  KEY `fk_equipajeViaje_tipo` (`id_tipo_equipaje`),
  CONSTRAINT `equipaje_viaje_ibfk_1` FOREIGN KEY (`id_viaje`) REFERENCES `viaje` (`id_viaje`),
  CONSTRAINT `equipaje_viaje_ibfk_2` FOREIGN KEY (`id_tipo_equipaje`) REFERENCES `tipo_equipaje` (`id_tipo_equipaje`),
  CONSTRAINT `equipaje_viaje_chk_1` CHECK ((`cantidad` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `equipaje_viaje` VALUES (1,2,1,1),(2,1,1,4),(3,1,2,2),(4,1,3,3),(5,3,6,1),(6,1,8,3);

--
-- Tabla: `historial_estado_viaje`  (depende de viaje, estado_viaje)
--

DROP TABLE IF EXISTS `historial_estado_viaje`;

CREATE TABLE `historial_estado_viaje` (
  `id_historial` int NOT NULL AUTO_INCREMENT,
  `fecha_cambio` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `observacion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_viaje` int NOT NULL,
  `id_estado_viaje` int NOT NULL,
  PRIMARY KEY (`id_historial`),
  KEY `fk_historial_viaje` (`id_viaje`),
  KEY `fk_historial_activo` (`id_estado_viaje`),
  CONSTRAINT `historial_estado_viaje_ibfk_1` FOREIGN KEY (`id_viaje`) REFERENCES `viaje` (`id_viaje`),
  CONSTRAINT `historial_estado_viaje_ibfk_2` FOREIGN KEY (`id_estado_viaje`) REFERENCES `estado_viaje` (`id_estado_viaje`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `historial_estado_viaje` VALUES (1,'2026-07-19 18:02:50','Viaje creado',1,1),(2,'2026-07-19 18:02:50','Conductor inició el recorrido',1,2),(3,'2026-07-19 18:02:50','Viaje finalizado sin novedades',1,3),(4,'2026-07-19 18:02:50','Viaje creado',2,1),(5,'2026-07-19 18:02:50','Viaje creado',3,1),(6,'2026-07-19 18:02:50','Conductor inició el recorrido',3,2),(7,'2026-07-19 18:02:50','Viaje creado',7,1),(8,'2026-07-19 18:02:50','Cliente canceló el traslado',7,4);

-- ============================================================
-- 5. TABLA COMPROBANTE (nivel 4, depende de gasto)
-- ============================================================

--
-- Tabla: `comprobante`
--

DROP TABLE IF EXISTS `comprobante`;

CREATE TABLE `comprobante` (
  `id_comprobante` int NOT NULL AUTO_INCREMENT,
  `nombre_archivo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `url_firebase` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_archivo` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_subida` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_gasto` int NOT NULL,
  PRIMARY KEY (`id_comprobante`),
  KEY `ndx_id_gasto` (`id_gasto`),
  CONSTRAINT `comprobante_ibfk_1` FOREIGN KEY (`id_gasto`) REFERENCES `gasto` (`id_gasto`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `comprobante` VALUES (1,'factura_gasolina_v1.jpg','https://firebasestorage.googleapis.com/v0/b/translogi-app.appspot.com/o/comprobantes%2Ffactura_gasolina_v1.jpg?alt=media','image/jpeg','2026-07-19 18:02:50',1),(2,'recibo_peaje_v1.pdf','https://firebasestorage.googleapis.com/v0/b/translogi-app.appspot.com/o/comprobantes%2Frecibo_peaje_v1.pdf?alt=media','application/pdf','2026-07-19 18:02:50',2),(3,'recibo_parqueo_v1.jpg','https://firebasestorage.googleapis.com/v0/b/translogi-app.appspot.com/o/comprobantes%2Frecibo_parqueo_v1.jpg?alt=media','image/jpeg','2026-07-19 18:02:50',3);


-- ============================================================
-- Fin de la carga de datos
-- ============================================================

SET FOREIGN_KEY_CHECKS = 1;
SET UNIQUE_CHECKS = 1;
# TransLogi

Sistema web para administrar servicios de transporte empresarial.

## Descripcion

TransLogi permite controlar usuarios, roles, conductores, empresas, ubicaciones,
viajes, gastos y reportes. El proyecto esta construido con Spring Boot,
Thymeleaf, Spring Security, MySQL, Bootstrap y servicios de Firebase Storage
para guardar imagenes.

## Objetivo

Centralizar la operacion diaria de una empresa de transporte y facilitar el
seguimiento de viajes programados, viajes en proceso, viajes finalizados y
gastos asociados.

## Integrantes

- Alexander Campos Marin
- Sebastian Picado Vargas
- Fernando Valverde Cubero

## Tecnologias

- Java 17 o superior
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- Bootstrap
- Apache POI para reportes Excel
- iText para reportes PDF
- Firebase Storage para imagenes

## Estructura principal

- `controller`: recibe peticiones web, prepara modelos y retorna vistas.
- `domain`: contiene las entidades que se relacionan con las tablas de la BD.
- `repository`: define consultas y acceso a datos con Spring Data JPA.
- `service`: concentra reglas de negocio, validaciones y operaciones externas.
- `templates`: contiene las pantallas Thymeleaf por modulo.
- `static`: contiene estilos, imagenes y scripts JavaScript.
- `resources/messages*.properties`: contiene textos para internacionalizacion.

## Modulos

- Dashboard: muestra resumen general del sistema.
- Usuarios: administra cuentas, imagenes y roles.
- Conductores: administra datos del conductor y fotos de licencia.
- Empresas: administra clientes empresariales.
- Ubicaciones: administra origenes y destinos.
- Viajes: programa, edita, elimina y consulta viajes.
- Mis Viajes: vista del conductor para iniciar, finalizar y registrar gastos.
- Reportes: filtra viajes y exporta resultados a Excel o PDF.

## Seguridad

La autenticacion usa Spring Security con usuarios de la base de datos. Las rutas
y sus permisos se cargan desde la tabla `ruta`, por medio de `RutaService` y
`SecurityConfig`. Esto permite controlar el acceso desde la BD sin dejar las
rutas fijas en el codigo.

Roles principales:

- Administrador: acceso completo.
- Supervisor: acceso operativo segun permisos definidos.
- Conductor: acceso a sus viajes y gastos.

## Flujo de viajes

Un viaje inicia como Programado. El conductor puede iniciarlo cuando la hora
actual esta cerca o despues de la hora programada. Al iniciar, pasa a En proceso.
Luego puede finalizarlo llenando kilometros recorridos, fecha y hora de inicio,
y fecha y hora de fin. Al guardar esos datos, el viaje pasa al estado final del
sistema.

Los gastos se registran aparte desde Mis Viajes. Solo se aceptan para viajes En
proceso o Finalizados, no para viajes Programados.

## Reportes

La pantalla de reportes permite filtrar por rango de fechas, empresa, conductor
y estado. Esos mismos filtros se reutilizan para la vista HTML, la descarga de
Excel y la generacion de PDF.

El Excel se genera en `ReporteExcelServiceImpl` usando Apache POI. El servicio
crea un libro, agrega titulo, resumen por estado, tabla de viajes y estilos para
encabezados y celdas. El archivo se devuelve como stream para descargarlo desde
el navegador.

## Firebase Storage

Las imagenes de usuarios y licencias se suben a Firebase Storage. La
configuracion de credenciales esta en `StorageConfig`. El archivo JSON se lee
desde una ruta externa para que en despliegue, como Render, pueda venir desde
Secret Files y no quede guardado dentro del proyecto.

## Ejecucion local

1. Crear la base de datos MySQL con el script del proyecto.
2. Revisar credenciales y propiedades en `application.properties`.
3. Configurar el archivo JSON de Firebase en la ruta indicada por las
   propiedades `firebase.json.path` y `firebase.json.file`.
4. Ejecutar el proyecto desde el IDE o con Maven.
5. Abrir la aplicacion en el navegador usando el puerto configurado.

## Convencion de trabajo

- No trabajar directamente sobre `main`.
- Crear ramas `feature` para nuevas funcionalidades.
- Revisar cambios antes de integrarlos a `develop`.
- Mantener comentarios breves, claros y sin afectar la funcionalidad.

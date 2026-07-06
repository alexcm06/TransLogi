# TransLogi

Sistema web orientado a la gestión de servicios de transporte empresarial.

## Descripción

TransLogi es una aplicación web desarrollada para apoyar la gestión de servicios de transporte empresarial mediante el control de conductores, empresas clientes, viajes y gastos operativos.

## Objetivo

Centralizar la información relacionada con la operación diaria de una empresa de transporte y facilitar el seguimiento de los servicios realizados.

## Integrantes

- Alexander Campos Marín
- Sebastián Picado Vargas
- Carlos Saborio Vega
- Fernando Valverde Cubero

## Tecnologías previstas

- Java
- Spring Boot
- MySQL
- HTML
- CSS

## Estructura de ramas

main
└── develop
    ├── features

## Convención de trabajo

- No trabajar directamente sobre main.
- Toda funcionalidad se desarrolla en una rama feature.
- Los cambios serán integrados mediante pull requests.
- El código deberá ser revisado antes de realizar merge a develop.

## Estado del proyecto

Fase de análisis y planificación
Avance II: Estructura del Proyecto y Capas del Sistema
   Para este segundo avance del proyecto, nos enfocamos en ordenar toda la estructura y los archivos del sistema. Por el lado del código, creamos el paquete principal com.TransLogi y lo dividimos en capas claras: los controladores para manejar las peticiones web, el dominio para definir las entidades, los repositorios para conectarnos con la base de datos, el módulo de seguridad para controlar los accesos y los servicios para manejar toda la lógica del negocio.

    En paralelo, se estructuró la sección de recursos bajo src/main/resources, donde se centralizaron los estilos CSS y los scripts de JavaScript estáticos para dar identidad visual e interactividad al sistema. Asimismo, se crearon los paquetes de plantillas HTML organizados por módulos funcionales para segmentar la interfaz de usuario: plantillas generales para la estructura común, un panel de control (dashboard), y módulos específicos dedicados a la gestión de conductores, empresas, ubicaciones y los viajes de la plataforma logística.

    Cabe destacar que, respecto a la seguridad y el sistema de login, se presentaron algunos inconvenientes técnicos relacionados con la encriptación de las credenciales. Debido a estas complicaciones, y considerando que este manejo específico no se ha abarcado detalladamente en las lecciones de clase, se prefirió postergar su implementación completa. La decisión se tomó con el fin de validar y confirmar previamente con el docente la forma correcta en que se debe estructurar este apartado, garantizando así el cumplimiento de las buenas prácticas requeridas.

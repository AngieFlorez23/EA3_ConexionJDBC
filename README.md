
# EA3 - Gestor de Conexiones JDBC

## Descripción

Aplicación de escritorio desarrollada en Java que permite comprobar conexiones a bases de datos mediante JDBC.

Cuenta con una interfaz gráfica creada con Java Swing, donde el usuario puede ingresar los datos de conexión y visualizar si la operación fue exitosa o si ocurrió un error.

## Tecnologías utilizadas

- Java 21
- Java Swing
- JDBC (Java Database Connectivity)
- SQLite
- IntelliJ IDEA

## Funcionalidades

- Ingresar la URL de conexión JDBC.
- Ingresar usuario y contraseña cuando corresponda.
- Probar la conexión mediante un botón.
- Mostrar mensajes de éxito en color verde.
- Mostrar errores de conexión en color rojo.
- Capturar excepciones SQLException.
- Detectar controladores ausentes mediante ClassNotFoundException.
- Validar que la URL no esté vacía.
- Cerrar las conexiones de forma segura.

## Estructura del proyecto

- `src/ConexionTester.java`: aplicación principal e interfaz gráfica.
- `src/Main.java`: archivo Java generado al crear el proyecto.
- `lib/sqlite-jdbc-3.54.1.0.jar`: controlador JDBC de SQLite.

## Cómo ejecutar la aplicación

1. Descargar o clonar este repositorio.
2. Abrir el proyecto en IntelliJ IDEA.
3. Configurar el JDK 21.
4. Verificar que el archivo JAR de SQLite esté agregado en las dependencias del módulo.
5. Ejecutar el método main de la clase `ConexionTester`.
6. Ingresar la URL de conexión y presionar "Probar conexión".

## Ejemplo de conexión SQLite

URL de conexión:

`jdbc:sqlite:inventario.db`

Usuario: dejar vacío.

Contraseña: dejar vacía.

SQLite creará el archivo de base de datos si no existe y dispone de permisos para hacerlo.

## Pruebas realizadas

1. Conexión exitosa a SQLite.
2. Error de conexión por ubicación inexistente.
3. Error por controlador JDBC no encontrado.
4. Validación de URL de conexión vacía.

Los cuatro escenarios fueron comprobados mediante la interfaz gráfica.

## Manejo de errores

La aplicación muestra los mensajes técnicos de las excepciones JDBC, lo que facilita identificar problemas de conexión.


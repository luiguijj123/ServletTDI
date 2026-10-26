# Tarea 3 - Financiera Huanca

Continuacion de la tarea 2, hecha con Spring Boot, Java 21 y MySQL. Se agrega una tabla de pagos de tarjetas debajo del formulario de credito.

## Como ejecutarlo

1. Tener Java 21, Maven y MySQL instalados.
2. Iniciar MySQL y crear la base `huanca_creditos`.
3. Crear la carpeta `.local` en la raiz del proyecto y dentro el archivo `application.properties`. Configurar `spring.datasource.username` y `spring.datasource.password` con el usuario y la contraseña de MySQL de su equipo. Si MySQL no usa localhost y el puerto 3306, configurar tambien `spring.datasource.url`.
4. El usuario de MySQL debe tener permisos SELECT, INSERT, UPDATE y CREATE sobre la base.
5. Abrir el proyecto en NetBeans con Java 21 y pulsar Ejecutar proyecto.
6. Entrar a [http://localhost:8081/credito](http://localhost:8081/credito).

Las tablas y los datos iniciales de pagos se crean al iniciar. Si el puerto 8081 esta ocupado, detener la otra aplicacion antes de ejecutar. La carpeta `.local` no se incluye en GitHub porque contiene la contraseña.

## Funcionamiento

Solo se puede editar la columna Pagos. Al pulsar Aceptar, se guarda el credito, se actualizan los pagos y se muestran los datos en la segunda vista. Los pagos se reemplazan por el importe escrito, no se suman. Un campo vacio indica que no hay pago registrado.

Las tablas `creditos` y `pagos_tarjeta` son independientes, sin llave foranea entre ellas. Los pagos se comparten entre las vistas de resultados y los registros se conservan al cerrar la aplicacion.

## Organizacion del codigo

- **Modelo:** `Credito.java`, `Pago.java` y `PagoForm.java` contienen los datos del credito y la lista de pagos.
- **Vista:** `formulario.jsp` y `resultado.jsp` muestran el formulario y los resultados. `estilos.css` define su apariencia.
- **Controlador:** `CreditoController.java` recibe las solicitudes y selecciona las vistas.
- **Servicios:** `CreditoService.java` y `PagoService.java` validan los datos y coordinan el guardado.
- **Repositorios:** `CreditoRepository.java` y `PagoRepository.java` guardan y consultan los datos en MySQL.
- **Base de datos:** `schema.sql` crea las tablas y carga los movimientos iniciales.

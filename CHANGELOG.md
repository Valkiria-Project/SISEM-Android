# CHANGELOG
Ajustes y correcciones aplicadas segun versiones:



# version 2.4.5 *(26.07.2026)*
----------------------------------------
### Correcciones

- **Inicio de sesión tras expiración de sesión:** Se corrige un problema donde, al expirar la sesión automáticamente, el usuario era redirigido al login pero al intentar ingresar de nuevo veía el mensaje *"credenciales incorrectas"*. Ahora la sesión se cierra correctamente en el servidor antes de redirigir al login, permitiendo iniciar sesión sin inconvenientes.

- **Mapa congelado al volver a la aplicación:** Se corrige un problema donde, al regresar a SISEM desde segundo plano, el mapa de la incidencia activa aparecía congelado o en blanco. Ahora el mapa se recarga correctamente cada vez que se vuelve a la pantalla.

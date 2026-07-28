# CHANGELOG
Ajustes y correcciones aplicadas segun versiones:



# version 2.4.X *(X.X.XXXX)*
----------------------------------------
### Mejoras de interfaz

- **Pantallas de inicio de sesión y preoperacional mejor ajustadas:** Se corrigió un espacio vacío excesivo que aparecía en la parte superior de estas pantallas, haciendo que el contenido se vea más ordenado y aproveche mejor el espacio de la pantalla del dispositivo.
- **Teclado se cierra automáticamente al aparecer un aviso:** Cuando la aplicación muestra un mensaje de alerta o confirmación (como "Guardar cambios"), el teclado del dispositivo ahora se oculta automáticamente para que el aviso sea completamente visible.=
- **Botones de avisos siempre visibles:** Los botones de acción dentro de las ventanas emergentes (como "Cancelar" o "Guardar") ya no quedaban ocultos detrás de la barra de navegación del dispositivo. Ahora siempre son accesibles.

### Correcciones

- **Cierre inesperado al expirar la sesión:** Se corrigió un error poco frecuente que podía cerrar la aplicación abruptamente al intentar redirigir al usuario al inicio de sesión por sesión expirada.

# version 2.4.5 *(26.07.2026)*
----------------------------------------
### Correcciones

- **Inicio de sesión tras expiración de sesión:** Se corrige un problema donde, al expirar la sesión automáticamente, el usuario era redirigido al login pero al intentar ingresar de nuevo veía el mensaje *"credenciales incorrectas"*. Ahora la sesión se cierra correctamente en el servidor antes de redirigir al login, permitiendo iniciar sesión sin inconvenientes.

- **Mapa congelado al volver a la aplicación:** Se corrige un problema donde, al regresar a SISEM desde segundo plano, el mapa de la incidencia activa aparecía congelado o en blanco. Ahora el mapa se recarga correctamente cada vez que se vuelve a la pantalla.

# CHANGELOG
Ajustes y correcciones aplicadas segun versiones:



# version 2.4.8 *(28.07.2026)*
----------------------------------------
### Mejoras de interfaz

- **Pantallas de inicio de sesión y preoperacional mejor ajustadas:** Se corrigió un espacio vacío excesivo que aparecía en la parte superior de estas pantallas, haciendo que el contenido se vea más ordenado y aproveche mejor el espacio de la pantalla del dispositivo.
- **Teclado se cierra automáticamente al aparecer un aviso:** Cuando la aplicación muestra un mensaje de alerta o confirmación (como "Guardar cambios"), el teclado del dispositivo ahora se oculta automáticamente para que el aviso sea completamente visible.
- **Botones de avisos siempre visibles:** Los botones de acción dentro de las ventanas emergentes (como "Cancelar" o "Guardar") ya no quedaban ocultos detrás de la barra de navegación del dispositivo. Ahora siempre son accesibles.

### Correcciones

- **Sesión activa en otro dispositivo:** Se corrige un problema donde, al intentar iniciar sesión teniendo una sesión abierta en otro dispositivo o navegador, la aplicación quedaba bloqueada en el inicio de sesión sin forma de continuar. Ahora se muestra el aviso *"Duplicidad"* con las opciones **Sí** y **No**: al elegir **Sí** se cierra la sesión del otro dispositivo y se confirma en pantalla, quedando el usuario habilitado para ingresar.

- **Cierre inesperado al expirar la sesión:** Se corrigió un error poco frecuente que podía cerrar la aplicación abruptamente al intentar redirigir al usuario al inicio de sesión por sesión expirada.

# version 2.4.7 *(27.07.2026)*
----------------------------------------
### Correcciones

- **Teclado en pantallas con botones fijos:** Se extiende a otras pantallas la corrección aplicada en la 2.4.6 a *"Olvidó su contraseña"*. Al abrir el teclado en *Cambio de contraseña*, *Autenticación del dispositivo*, *Firma*, *Registro de firma*, *Novedades* e *Inventario (detalle)*, el contenido y los botones inferiores quedaban ocultos o inaccesibles en equipos donde el teclado ocupa una porción mayor de la pantalla, como el Motorola G47. Ahora los botones se elevan sobre el teclado y el contenido permanece visible.

# version 2.4.6 *(27.07.2026)*
----------------------------------------
### Correcciones

- **Contraseña vencida:** Se corrige un problema donde, al cerrar el aviso *"Su contraseña se ha vencido"*, el usuario quedaba en la pantalla de inicio de sesión sin ninguna opción para cambiarla. Ahora al cerrar el aviso se abre directamente la pantalla de cambio de contraseña.

- **Recuperar contraseña en pantallas pequeñas:** Se corrige un problema donde, al abrir el teclado en la pantalla *"Olvidó su contraseña"*, desaparecían el campo de correo y los botones Cancelar y Enviar, dejando la pantalla inutilizable. Se presentaba en equipos donde el teclado ocupa una porción mayor de la pantalla, como el Motorola G47. Ahora el contenido y los botones permanecen visibles sobre el teclado.

# version 2.4.5 *(26.07.2026)*
----------------------------------------
### Correcciones

- **Inicio de sesión tras expiración de sesión:** Se corrige un problema donde, al expirar la sesión automáticamente, el usuario era redirigido al login pero al intentar ingresar de nuevo veía el mensaje *"credenciales incorrectas"*. Ahora la sesión se cierra correctamente en el servidor antes de redirigir al login, permitiendo iniciar sesión sin inconvenientes.

- **Mapa congelado al volver a la aplicación:** Se corrige un problema donde, al regresar a SISEM desde segundo plano, el mapa de la incidencia activa aparecía congelado o en blanco. Ahora el mapa se recarga correctamente cada vez que se vuelve a la pantalla.

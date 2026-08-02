# CHANGELOG
Ajustes y correcciones aplicadas segun versiones:



# Version 2.4.11 *(02.08.2026)*
----------------------------------------
### Diagnóstico (temporal)

- **Registro de logs en almacenamiento:** La aplicación ahora guarda un registro diario de actividad y errores en la carpeta **Descargas / SISEM-Logs/** del dispositivo. Los archivos se eliminan automáticamente después de 10 días. Esta función es temporal y permite al equipo recibir logs de forma manual para diagnosticar problemas en campo.

# Version 2.4.10 *(30.07.2026)*
----------------------------------------
### Correcciones

- **Cierre de sesión duplicada y navegación automática:** Al confirmar el cierre de una sesión activa en otro dispositivo, la aplicación reintenta el inicio de sesión automáticamente enviando el parámetro `force_close_session` al servidor. Esto elimina la sesión remota y autentica al usuario en un solo paso, navegando directamente a la pantalla correspondiente sin requerir un segundo inicio de sesión manual.

- **Crash al volver al mapa tras animación de cámara:** Se corrige un error que cerraba la aplicación abruptamente cuando la animación de la cámara de navegación terminaba después de que el mapa había sido destruido en segundo plano.

# Version 2.4.9 *(30.07.2026)*
----------------------------------------
### Correcciones

- **Navegación hacia atrás tras cerrar sesión:** Se corrige un problema donde, al presionar el botón físico de retroceso después de cerrar sesión, la aplicación navegaba incorrectamente hacia pantallas preoperacionales o de inicio de sesión de otros usuarios. Ahora al cerrar sesión, ya sea manualmente o por expiración de sesión, la pila de navegación se limpia completamente y no es posible volver a pantallas de sesiones anteriores.

### Mejoras de rendimiento

- **Cierre de sesión más rápido:** Se corrige un problema donde al cerrar sesión la aplicación realizaba decenas de peticiones innecesarias al servidor de ubicación y servicios externos, provocando que el proceso fuera lento. Ahora el servicio de rastreo GPS y las tareas pendientes de envío de ubicación se detienen inmediatamente al iniciar el cierre de sesión.
- **Reducción de peticiones a servicio de IP:** La consulta de IP pública usada para auditoría ahora se renueva cada 10 minutos en lugar de ejecutarse en cada petición al servidor, reduciendo significativamente el tráfico de red innecesario.

### Control de acceso

- **Restricción de inicio de sesión tras cierre de turno:** Al cerrar sesión un tripulante, la pantalla de selección de usuario solo permite iniciar sesión con el mismo tipo de rol que cerró la sesión. Si se intenta seleccionar un rol diferente, se muestra un aviso indicando qué tipo de tripulante debe ingresar. Esto evita que la tripulación quede incompleta al reemplazar roles incorrectos.

### Correcciones de mapa
 
- **Ruta de navegación no se restauraba al reabrir la app:** Se corrige un problema donde, al cerrar completamente la aplicación y volver a abrirla, el mapa cargaba pero no mostraba la ruta de navegación activa hacia el incidente asignado. Ahora la ruta se reanuda correctamente tanto si la app fue minimizada como si fue cerrada por completo.

# Version 2.4.8 *(28.07.2026)*
----------------------------------------
### Mejoras de interfaz

- **Pantallas de inicio de sesión y preoperacional mejor ajustadas:** Se corrigió un espacio vacío excesivo que aparecía en la parte superior de estas pantallas, haciendo que el contenido se vea más ordenado y aproveche mejor el espacio de la pantalla del dispositivo.
- **Teclado se cierra automáticamente al aparecer un aviso:** Cuando la aplicación muestra un mensaje de alerta o confirmación (como "Guardar cambios"), el teclado del dispositivo ahora se oculta automáticamente para que el aviso sea completamente visible.
- **Botones de avisos siempre visibles:** Los botones de acción dentro de las ventanas emergentes (como "Cancelar" o "Guardar") ya no quedaban ocultos detrás de la barra de navegación del dispositivo. Ahora siempre son accesibles.

### Correcciones

- **Sesión activa en otro dispositivo:** Se corrige un problema donde, al intentar iniciar sesión teniendo una sesión abierta en otro dispositivo o navegador, la aplicación quedaba bloqueada en el inicio de sesión sin forma de continuar. Ahora se muestra el aviso *"Duplicidad"* con las opciones **Sí** y **No**: al elegir **Sí** se cierra la sesión del otro dispositivo y se confirma en pantalla, quedando el usuario habilitado para ingresar.

- **Cierre inesperado al expirar la sesión:** Se corrigió un error poco frecuente que podía cerrar la aplicación abruptamente al intentar redirigir al usuario al inicio de sesión por sesión expirada.

# Version 2.4.7 *(27.07.2026)*
----------------------------------------
### Correcciones

- **Teclado en pantallas con botones fijos:** Se extiende a otras pantallas la corrección aplicada en la 2.4.6 a *"Olvidó su contraseña"*. Al abrir el teclado en *Cambio de contraseña*, *Autenticación del dispositivo*, *Firma*, *Registro de firma*, *Novedades* e *Inventario (detalle)*, el contenido y los botones inferiores quedaban ocultos o inaccesibles en equipos donde el teclado ocupa una porción mayor de la pantalla, como el Motorola G47. Ahora los botones se elevan sobre el teclado y el contenido permanece visible.

# Version 2.4.6 *(27.07.2026)*
----------------------------------------
### Correcciones

- **Contraseña vencida:** Se corrige un problema donde, al cerrar el aviso *"Su contraseña se ha vencido"*, el usuario quedaba en la pantalla de inicio de sesión sin ninguna opción para cambiarla. Ahora al cerrar el aviso se abre directamente la pantalla de cambio de contraseña.

- **Recuperar contraseña en pantallas pequeñas:** Se corrige un problema donde, al abrir el teclado en la pantalla *"Olvidó su contraseña"*, desaparecían el campo de correo y los botones Cancelar y Enviar, dejando la pantalla inutilizable. Se presentaba en equipos donde el teclado ocupa una porción mayor de la pantalla, como el Motorola G47. Ahora el contenido y los botones permanecen visibles sobre el teclado.

# Version 2.4.5 *(26.07.2026)*
----------------------------------------
### Correcciones

- **Inicio de sesión tras expiración de sesión:** Se corrige un problema donde, al expirar la sesión automáticamente, el usuario era redirigido al login pero al intentar ingresar de nuevo veía el mensaje *"credenciales incorrectas"*. Ahora la sesión se cierra correctamente en el servidor antes de redirigir al login, permitiendo iniciar sesión sin inconvenientes.

- **Mapa congelado al volver a la aplicación:** Se corrige un problema donde, al regresar a SISEM desde segundo plano, el mapa de la incidencia activa aparecía congelado o en blanco. Ahora el mapa se recarga correctamente cada vez que se vuelve a la pantalla.

# CHANGELOG
Ajustes y correcciones aplicadas segun versiones:


# Version 2.4.X *(pendiente de asignar al sincronizar con GitLab)*
----------------------------------------
### Modo sin conexión

- **Navegación sin señal:** Las pantallas que la tripulación ya abrió (historia clínica, preoperacionales, novedades, etc.) se guardan cifradas en el dispositivo durante 72 horas. Si la ambulancia pierde la señal, la app las muestra desde el dispositivo en lugar de quedarse cargando o mostrar un error. La información de cada paciente se guarda por separado.
- **Asignación de incidentes con señal débil:** Si llega la notificación de un incidente asignado pero el detalle (pacientes e historias clínicas) no alcanza a cargar, la app lo sigue intentando sola en segundo plano hasta lograrlo, durante un máximo de 2 horas. Antes la asignación quedaba incompleta y no se podía abrir la historia clínica del paciente.
- **Registro sin señal:** La historia clínica, sus fotos y adjuntos, la retención de camilla, el envío por correo, los preoperacionales, las novedades, el retorno de traslado y el cambio de tripulación ya no se pierden sin internet. La app los guarda cifrados en el dispositivo, permite seguir trabajando y los envía sola cuando vuelve la señal, en el mismo orden en que se hicieron.
- **Cada registro se envía a nombre de quien lo hizo:** Un registro guardado sin señal solo se envía con la sesión del tripulante que lo creó, nunca con la de quien tenga el mismo rol después de un cambio de turno. Si esa persona ya no tiene sesión en el dispositivo, el registro espera a que vuelva a ingresar.
- **Aviso al cerrar sesión con registros sin enviar:** Si un tripulante intenta cerrar sesión con registros suyos que aún no llegan al servidor, la app le avisa cuántos son y le permite cancelar. Esos registros solo pueden enviarse a su nombre, así que si cierra sesión quedan esperando hasta que vuelva a ingresar en ese dispositivo.
- **Aviso de conexión:** Una franja en la parte superior indica cuando no hay señal, cuántos registros están pendientes de envío y si el servidor rechazó alguno, para que la tripulación pueda reportarlo a soporte.

### Correcciones

- **Cierre de sesión al perder la señal:** Se corrige un problema donde la aplicación cerraba la sesión de la tripulación cuando la ambulancia pasaba más de unos minutos sin internet. Al vencerse el permiso de acceso, la app intentaba renovarlo; si no había señal, trataba esa falla como si la sesión hubiera terminado, sacaba al usuario al inicio de sesión y dejaba la sesión abierta en el servidor, lo que luego provocaba el aviso de *"Duplicidad"* o *"credenciales incorrectas"* al volver a ingresar. Ahora la sesión solo se cierra cuando el servidor la rechaza; mientras no haya señal, la app conserva la sesión y reintenta al recuperar la conexión.

# Version 2.4.14 *(24.08.2026)*
----------------------------------------
### Bloqueo de inicio de sesión biométrico con sesión activa en otro rol

- **No se permite cambiar de rol silenciosamente vía biometría:** Si el rostro reconocido corresponde a un tripulante que ya tiene una sesión activa en el dispositivo bajo un rol distinto al que se intenta ingresar (por ejemplo, entró como Conductor y luego se intenta ingresar por el card de Auxiliar con biometría), el inicio de sesión ya no se ejecuta. En su lugar se muestra un aviso indicando que el tripulante ya tiene una sesión activa, con su nombre y el rol en negrita. Aplica tanto al primer ingreso por card como al cambio de turno.

### Mejoras en la verificación de registro biométrico (/exists)

- **Aviso de "sin registro" en lugar de alerta de conexión:** Cuando el líder APH busca a un tripulante que aún no tiene biometría registrada (respuesta 404 o `exists: false`), la app ya no muestra la alerta roja de fallo de red; en su lugar presenta un aviso informativo azul indicando que el usuario no tiene registro y puede enrolarse en ese momento.
- **Datos de la persona traídos desde el servidor:** Cuando el tripulante sí tiene biometría registrada, la pantalla muestra su nombre, número de documento y rol tal como los entrega el backend, sin depender de que la persona haya iniciado sesión previamente en el dispositivo.
- **Distinción real entre "sin conexión" y "sin registro":** La alerta de intermitencia de red queda reservada exclusivamente para fallas reales de conectividad; un 404 o `exists: false` ya no se confunde con un problema de red.
- **Datos de la persona visibles aunque no tenga biometría registrada:** Si el backend devuelve nombre, documento o rol junto con `exists: false`, la pantalla ya no los descarta — se muestran igual que en el caso registrado, y solo se deja de mostrar el documento ingresado por el líder cuando el backend no entrega ningún dato de identidad.
- **VersionCode** Se sube version code a 98 para registrar actualizaciones.

### Correcciones

- **Navegación tras autenticar el dispositivo:** Se corrige un problema en las tres salidas de la pantalla de autenticación del dispositivo, donde el destino no se resolvía correctamente al volver a las tarjetas de autorización.

# Version 2.4.13 *(21.08.2026)*
----------------------------------------
### Rediseño del registro y validación biométrica facial

- **El registro facial ahora lo realiza el líder APH:** El enrolamiento del rostro se traslada al flujo de firma del líder APH. En lugar de que cada tripulante se registre tras su propio inicio de sesión, el líder registra a la tripulación buscando por número de documento, sin necesidad de que la persona tenga una sesión activa en el dispositivo. Se elimina el registro biométrico por tripulante y su ítem del menú lateral.
- **La huella dactilar se reemplaza por biometría facial:** En la pantalla de firma, el componente de huella deja de mostrarse; el registro biométrico pasa a ser exclusivamente por reconocimiento facial.
- **Nuevo botón "Ingresar con Biometría" en la pantalla de login:** Aparece automáticamente cuando el dispositivo ya tiene un rostro enrolado con credenciales previas. Solo se muestra si existe al menos un registro biométrico válido en el dispositivo.
- **Inicio de sesión biométrico como atajo (re-autenticación silenciosa):** Al reconocer el rostro, la aplicación recupera las credenciales del usuario y ejecuta el inicio de sesión normal de forma automática, redirigiendo a las tarjetas de cada tripulante igual que un login manual. Funciona como atajo independiente del estado de sesión de los demás tripulantes: no requiere que el resto de la tripulación tenga su sesión activa.
- **Credenciales almacenadas de forma cifrada:** La contraseña se guarda localmente cifrada con AES/GCM; la llave de cifrado reside en el Android Keystore del dispositivo. Esto permite la re-autenticación por rostro sin exponer la contraseña en texto plano.
- **Embeddings biométricos entregados en la respuesta de inicio de sesión:** Los datos faciales del usuario ahora llegan directamente en la respuesta de login y se almacenan localmente en ese momento, eliminando una consulta adicional al servidor. El botón de biometría queda disponible en la siguiente visita a la pantalla de login.
- **Depuración automática por inactividad (10 días):** Los registros biométricos y sus credenciales cifradas que permanezcan sin uso por más de 10 días se eliminan automáticamente al cargar la pantalla de login. Cada inicio de sesión exitoso (manual o biométrico) renueva la vigencia del registro, por lo que un usuario activo nunca se depura (ventana deslizante).
- **Los datos biométricos sobreviven a las actualizaciones de la app:** La migración de la base de datos local es no destructiva, garantizando que la tripulación no deba volver a registrarse tras actualizar la aplicación.

### Rediseño visual del scanner biométrico

- **Nueva interfaz de captura:** El scanner se rediseña con encabezado de título y subtítulo, óvalo guía punteado, esquinas de encuadre (brackets) y un fondo atenuado que resalta el área del rostro. El nuevo diseño aplica tanto al registro (enrolamiento) como a la verificación (inicio de sesión).
- **Guías de distancia en tiempo real:** Mientras se ubica el rostro, la app indica al usuario si debe acercarse, alejarse o si su posición es correcta, mejorando la tasa de captura exitosa. Se conserva el desafío de liveness (anti-suplantación).
- **Indicadores de estado (cargando, éxito y falla):** Durante el procesamiento se muestra un círculo de carga; al finalizar, un círculo con check de éxito o cruz de falla comunica claramente el resultado del registro o la verificación.
- **Navegación automática al mapa tras enrolamiento exitoso:** Cuando el líder APH captura y envía correctamente el registro biométrico, la aplicación navega al mapa, replicando el comportamiento de registrar o actualizar la firma.

### Correcciones

- **Resolución del Android ID:** Se restaura la resolución dinámica del identificador del dispositivo con almacenamiento en caché, evitando valores fijos.


# Version 2.4.12 *(19.08.2026)*
----------------------------------------
### Autenticación biométrica por reconocimiento facial

- **Motor FaceNet 512 (TFLite):** El sistema de reconocimiento facial migra de un enfoque geométrico basado en contornos a un modelo de aprendizaje profundo (FaceNet 512 dimensiones) ejecutado completamente en el dispositivo. La precisión de comparación mejora de ~70-80% a ~99%, sin enviar imágenes a ningún servidor.
- **Liveness detection — anti-suplantación:** Antes de capturar o verificar un rostro, la app emite un desafío aleatorio (parpadear, girar la cabeza a la izquierda o a la derecha) con un contador regresivo de 8 segundos. Esto impide el acceso mediante fotos impresas, pantallas con la imagen del usuario o videos pregrabados. El desafío aplica tanto al registro como a la verificación.
- **Registro en base de datos local (Room):** Los embeddings biométricos se almacenan ahora en una tabla cifrada de la base de datos del dispositivo (`biometric_credentials`) en lugar de preferencias compartidas. Esto mejora la gestión, la trazabilidad y el control del ciclo de vida de los datos biométricos por usuario.
- **Sincronización en la nube:** Al completar el registro facial, los embeddings se suben automáticamente a un servicio en la nube. Si la subida falla por falta de conexión, WorkManager reintenta el envío en segundo plano con backoff exponencial cuando se restaura la red.
- **Portabilidad entre dispositivos:** Al iniciar sesión en un dispositivo donde el usuario no tiene biometría registrada localmente, la aplicación consulta la nube automáticamente. Si el usuario ya se registró en otro dispositivo, sus datos se descargan y almacenan localmente, permitiendo autenticarse con el rostro sin necesidad de volver a registrarse.
- **Falla de modal en vista de mapa en tablet:** Se corrige la visualizacion de como se mostraba la info de un incidente en ruta en un modal, asi mismo los datos de distancia que se sobreponian
    - Resuelve:  https://skgtecnologia.atlassian.net/browse/SMA-763

- **Pad de firma:** Se corrige la visualizacion de pad de firma en vista de tablet
    - Resuelve:  https://skgtecnologia.atlassian.net/browse/SMA-764

### Correcciones

- **Doble navegación al aceptar el enroll facial:** Al pulsar "Registrar ahora" en el diálogo de enrollment, se producían dos navegaciones simultáneas que corrompían el historial de pantallas. Corregido limpiando el evento de navegación pendiente antes de redirigir a la cámara de registro.
    - Resuelve https://skgtecnologia.atlassian.net/browse/SMA-762

# Version 2.4.11 *(02.08.2026)*
----------------------------------------
### Mejoras de interfaz — Registro APH

- **Espacio y teclado en formulario APH:** Se corrige el espacio excesivo entre el encabezado y el contenido del formulario. Al abrir el teclado, el layout ahora se ajusta correctamente permitiendo interactuar con todos los campos sin que queden ocultos.
- **Vista de detalle APH e incidentes:** Se aplica el mismo ajuste de layout a la pantalla de detalle de un registro APH y a la pantalla de listado de incidentes, corrigiendo el espacio y el comportamiento del teclado.

### Mejoras de firma

- **Firma en modo landscape:** Al ingresar a la pantalla de firma, la aplicación rota automáticamente a modo horizontal para aprovechar todo el ancho de la pantalla al firmar. Al guardar o cancelar, regresa a modo vertical.
- **Área de firma identificable:** El pad de firma ahora tiene un fondo diferenciado y borde redondeado para indicar claramente el área donde se debe firmar.
- **Firma completa en el formulario:** Se corrige un problema donde la firma capturada se mostraba recortada en el formulario. Ahora se muestra completa y proporcional independientemente del tamaño del pad.

### Correcciones
- Se agregan mejoras para corregir el flujo de navegacion cuando el usuario ya no tiene incidentes asignados, para no mostrar navegacion activa cuando recibe un actualizacion de estado tipo 508

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

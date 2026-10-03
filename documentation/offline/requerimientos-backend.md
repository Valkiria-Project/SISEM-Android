# Modo sin conexión: qué necesita el backend

La app ya opera sin señal (PRs #313, #314, #315 y el de precarga). Esto cubre lo que **no se puede resolver solo del lado del celular**. Está ordenado por impacto: los puntos 1 a 3 evitan datos duplicados o mal fechados en registros clínicos; el resto mejora la operación.

## Cómo envía la app hoy lo que se registró sin señal

Cada escritura operativa (`aph`, `aph/files`, `aph/delete-file`, `aph/save-stretcher-retention`, `aph/send-mail`, `preoperational/*`, `novelty/*/*`, `transfer-return`, `crew`) lleva desde su **primer** intento estos encabezados:

| Encabezado | Valor | Para qué |
|---|---|---|
| `Idempotency-Key` | UUID, igual en el primer intento y en todos los reenvíos | Reconocer un registro que ya se guardó |
| `X-Client-Created-At` | ISO-8601 UTC (`2026-10-02T13:45:10.123Z`) | Momento real en que la tripulación hizo el registro |
| `X-Sisem-Replay` | Id interno del registro en cola (solo en reenvíos) | Saber que llega tarde, desde la cola |

Si no hay red, el registro se guarda cifrado en el dispositivo y se reenvía en el mismo orden en que se hizo, **firmado con el token de quien lo creó**. Los encabezados de auditoría (`geolocation`, `x-ip`) del reenvío son los del momento del registro, no los del envío.

## 1. Deduplicar por `Idempotency-Key` — crítico

**Problema.** Si la señal se cae *después* de que el servidor guardó una HC pero *antes* de que la respuesta llegue al celular, la app ve un timeout, guarda la HC y la reenvía. Sin deduplicación queda **duplicada**. Esto ya pasaba antes del modo sin conexión cuando la tripulación reintentaba a mano; ahora el reenvío es automático.

**Qué se necesita.** En los endpoints de la lista:
- Guardar `Idempotency-Key` junto con el registro (o en una tabla aparte con TTL de al menos 7 días).
- Si llega una clave ya vista: responder **2xx** (idealmente el mismo código de la primera vez) sin volver a guardar.
- Si no llega la clave (versiones viejas de la app), seguir como hoy.

## 2. Usar `X-Client-Created-At` como fecha del registro — crítico

**Problema.** Un preoperacional hecho a las 6:00 sin señal puede llegar a las 9:00. Si el servidor lo fecha al recibirlo, el reporte dice que se hizo tarde, y una HC queda con la hora equivocada.

**Qué se necesita.** Tomar `X-Client-Created-At` como fecha de creación cuando venga, y guardar la de recepción por separado. Si las validaciones de negocio dependen de la hora (por ejemplo, "preoperacional antes del inicio del turno"), deben usar la del cliente.

## 3. Códigos de respuesta coherentes — alto

La app decide qué hacer con un reenvío según el código:

| Respuesta | La app hace |
|---|---|
| 2xx | Lo da por enviado y lo borra de la cola |
| 401 / 403 | Espera a que el creador tenga una sesión válida |
| 408, 429, 5xx, sin red | Reintenta más tarde (backoff exponencial desde 30 s) |
| Cualquier otro 4xx | Lo aparta como **rechazado** y avisa a la tripulación que contacte a soporte |

**Qué se necesita.**
- Un duplicado detectado sin `Idempotency-Key` (por ejemplo, "esta HC ya existe") debe responder **409** o **2xx**, no 500. Un 500 se reintenta indefinidamente.
- Un error de validación permanente debe ser **4xx**. Hoy varios errores de negocio salen como 500 y la app no puede distinguirlos de una caída del servidor.
- Fotos demasiado grandes: responder **413**. Hoy la app las trataría como rechazo permanente, lo cual es correcto, pero conviene saber el límite real para comprimirlas antes de encolarlas.

## 4. Registros cuyo creador no vuelve a ingresar — alto, requiere decisión

**Problema.** La app firma cada reenvío solo con la sesión de quien lo creó: si un auxiliar registra una HC sin señal y entrega el turno, la HC no puede salir a nombre del siguiente auxiliar. Si ese auxiliar no vuelve a iniciar sesión en ese dispositivo, la HC queda esperando en el celular.

**Opciones (decidir con el equipo de backend):**
1. **Credencial de dispositivo:** un token del vehículo (client credentials en Keycloak) con el que la app pueda enviar registros ajenos, indicando el autor en un campo `created_by` firmado. El backend confía en el autor declarado solo para escrituras con `X-Sisem-Replay`.
2. **Ventana de gracia del refresh token:** con `offline_access` el refresh token no vence por tiempo (`refresh_expires_in: 0`; solo lo limita el *Offline Session Idle* del realm, que conviene confirmar), así que mientras el usuario no cierre sesión explícitamente la app puede renovar su token sin que esté presente. Esto ya funciona; el problema queda solo para quien **cierra sesión** con registros pendientes. Del lado de la app, avisar antes del cierre de sesión cubre la mayoría de los casos.

Se recomienda la opción 2 más el aviso al cerrar sesión, y evaluar la 1 solo si en campo siguen quedando registros sin enviar.

## 5. Asignación de incidentes sin señal estable — alto

**Problema.** La asignación llega por FCM y la app inmediatamente llama a `incident` para traer el detalle. Si esa llamada falla (llega el push pero la señal es mala), la asignación se pierde: hoy solo se muestra un error.

**Qué se necesita.**
- Incluir en el payload del push los datos mínimos del incidente (id, dirección, prioridad, pacientes con `id_aph`), para que la app pueda operar sin la segunda llamada. El límite de FCM es 4 KB.
- Opcional: un endpoint `incident/active?vehicle=…` que la app consulte al recuperar señal, para no depender de que el push haya llegado.

## 6. Pantallas server-driven sin efectos secundarios y versionadas — medio

**Problema.** La app ahora guarda (72 h) y **precarga** las pantallas `screen/*` del incidente activo: `aph`, `aph-vital-signs`, `aph-medicines`, `pre-aph-stretcher-retention`, `aph-stretcher-retention` e `incident-view`.

**Qué se necesita.**
- Confirmar que ningún `POST screen/*` tiene efectos secundarios (marcar como "visto", bloquear la HC, iniciar tiempos). Si alguno los tiene, la precarga los dispararía antes de que la tripulación abra la pantalla. **Hay que confirmarlo antes de llevar la precarga a producción.**
- Agregar una versión o `ETag` en la respuesta de cada pantalla. Si un formulario cambia, hoy la app puede mostrar la copia anterior hasta por 72 h sin señal, y el servidor debería aceptar o rechazar con un 4xx claro un envío hecho con la versión anterior.

## 7. Inicio de sesión requiere red — medio

Keycloak exige conexión para el primer ingreso de cada tripulante. Un cambio de turno en una zona sin señal no se puede completar. Mientras la sesión ya exista, la app sigue funcionando (PR #313).

**Opción:** permitir el ingreso offline con biometría local para un usuario que ya inició sesión antes en ese dispositivo, emitiendo la sesión real al volver la señal. Requiere definir con seguridad cuánto tiempo puede durar esa sesión local.

## 8. Ubicaciones por lote — bajo

Cada ubicación es un `POST location` independiente, y las de más de 5 minutos se descartan. Tras una hora sin señal se acumulan cientos de envíos que salen todos a la vez al volver la conexión.

**Qué se necesita.** Un `POST location/batch` que reciba una lista de `{latitude, longitude, origin_at}` permitiría enviar el recorrido completo sin señal en una sola llamada y conservar el histórico en lugar de descartarlo.

## 9. APN privado de las SIM — bajo, a confirmar con ETB

Android marca una red como "validada" solo si llega a `connectivitycheck.gstatic.com`. Si el APN privado de las SIM bloquea ese dominio, la red nunca se valida. La app ya lo maneja (solo considera "sin conexión" cuando no hay red en absoluto), pero conviene que ETB confirme si ese dominio es accesible, para que el sistema y otras apps no traten la red como inexistente.

---

**Resumen para priorizar:** 1 y 2 son los que pueden afectar la integridad de los registros clínicos y deberían estar antes de llevar el modo sin conexión a producción. El 6 (confirmar que no hay efectos secundarios) es una pregunta de una línea que hay que responder antes de activar la precarga.

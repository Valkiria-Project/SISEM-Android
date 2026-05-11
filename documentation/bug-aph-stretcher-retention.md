# Bug — `POST /screen/aph-stretcher-retention` no responde (timeout)

Reporte para escalar al equipo backend de SISEM. La app Android (v2.4.0) muestra el banner *"Servicio no disponible — Sucedió algo inesperado, por favor intenta de nuevo"* al entrar a **Registra retención de camilla → seleccionar paciente**. La causa raíz es un timeout del lado servidor; el comportamiento del cliente es correcto.

## Endpoint afectado

```
POST https://api.emergencias.saludcapital.gov.co/sisem-api/v1/screen/aph-stretcher-retention
Content-Type: application/json
Body: {"params":{"id_aph":"77"}}
```

## Síntoma

La request **no responde nunca**. El cliente OkHttp corta a los 15 s con `SocketTimeoutException` (timeout configurado en `app/src/main/java/com/skgtecnologia/sisem/di/CoreNetworkModule.kt:59`, aplicado en `BearerNetworkModule.kt:35-37`).

## Evidencia reproducida (2026-05-11 11:40, hora Bogotá)

Sobre staging (`api.emergencias.saludcapital.gov.co`), build `com.skgtecnologia.sisem-v2.4.0-83-staging`, usuario `oauxiliar`:

```
11:40:42.695  --> POST .../screen/aph-stretcher-retention   body={"params":{"id_aph":"77"}}
11:40:57.838  <-- HTTP FAILED: java.net.SocketTimeoutException (15.141 ms)
11:40:57.897  E/StretcherRetentionViewModel: This is a failure
              BannerModel(title=Servicio no disponible,
                          description=Sucedió algo inesperado, por favor intenta de nuevo)
```

La llamada inmediatamente anterior funciona OK con la misma sesión / token:

```
11:40:17.804  --> POST .../screen/pre-aph-stretcher-retention   body={"params":{"id_incident":"104"}}
11:40:19.454  <-- 200 OK (1.634 ms, 578 B)
```

→ El problema está aislado al endpoint `aph-stretcher-retention`, no es de red ni de auth.

## Contexto de prueba

| Dato            | Valor                                                 |
|-----------------|-------------------------------------------------------|
| Ambiente        | `api.emergencias.saludcapital.gov.co`                 |
| Build           | `com.skgtecnologia.sisem-v2.4.0-83-staging`           |
| Usuario         | `oauxiliar` (curso)                                   |
| Vehículo        | JHS026 — serial Android `7ec2c127b2095132`            |
| `id_incident`   | `104`                                                 |
| `id_aph`        | `77` (ANGEL MARTIN)                                   |
| `actorId` token | `13025`                                               |

## Lo que necesitamos del backend

1. Revisar el handler de `POST /screen/aph-stretcher-retention` — probablemente colgado en alguna query o dependencia downstream que no completa.
2. Confirmar si reproduce con cualquier `id_aph` o solo con ciertos pacientes / incidentes.
3. Logs del servicio entre `2026-05-11 16:40:42 UTC` y `2026-05-11 16:40:57 UTC` para correlacionar con la traza.

## Lado cliente

El comportamiento Android es correcto:
- `OkHttpClient` con `connect/read/writeTimeout = 15_000 ms` (`CoreNetworkModule.kt:59`).
- `SocketTimeoutException` mapeado a `error_server_title` / `error_server_description` en `NetworkApi.kt:87`.

Una vez resuelto el backend, opcionalmente se puede mejorar la UX agregando un botón *"Reintentar"* en el banner para errores de timeout/server. No es bloqueante para destrabar el flujo.

## Cómo reproducir localmente

Ver `documentation/testing.md` — pasos completos para spoofear el Android ID con el serial `7ec2c127b2095132` y loguearse con los usuarios de curso.

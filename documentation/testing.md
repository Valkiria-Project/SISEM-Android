# Testing Guide

This document explains how to set up a local environment to reproduce QA scenarios on the SISEM Android app, including spoofing the device identifier (Android ID) so a fresh emulator can be paired against a known vehicle/AVL in the backend.

## Build variant

For QA against the Bogotá production-ish backend, use the **staging** variant — it points to `api.emergencias.saludcapital.gov.co`, same host the test users below live in.

```bash
./gradlew assembleStaging
adb install -r app/build/outputs/apk/staging/com.skgtecnologia.sisem-v*.apk
```

| Variant   | Base URL                                      |
|-----------|-----------------------------------------------|
| debug     | test.emergencias-sisem.co                     |
| staging   | api.emergencias.saludcapital.gov.co           |
| preProd   | mobile-preprod.sisem.co                       |
| release   | api.emergencias.saludcapital.gov.co           |

## Test users (course role — "Cursos")

| Role        | Username   | Password   |
|-------------|-----------|------------|
| Médico      | OMEDICO   | 22042601   |
| Conductor   | OCONDUCTOR| 22042602   |
| Auxiliar APH| OAUXILIAR | 22042603   |

These users are tied to a vehicle that has been pre-registered in the backend with a specific device serial (see next section). If you try to log in from an emulator that the backend doesn't know, the device-auth screen blocks you.

## Faking the Android ID (device serial)

The vehicle/AVL is keyed by the device's Android ID, which the app reads through `AndroidIdProvider`:

- Interface: `app/src/main/java/com/skgtecnologia/sisem/commons/resources/AndroidIdProvider.kt`
- Impl: `app/src/main/java/com/skgtecnologia/sisem/commons/resources/AndroidIdProviderImpl.kt`

How it works:

1. On first read, the app creates `<app filesDir>/android_id`. If empty, it falls back to `Settings.Secure.ANDROID_ID` and caches the value inside that file.
2. On every subsequent read it returns the cached value. So if you overwrite the file, the new value sticks until the app data is cleared.

**Serial registered for the test users:** `7ec2c127b2095132`

### Steps to inject it (emulator or rooted device)

```bash
# 1. Install + run the staging APK once so the app's filesDir exists
adb install -r app/build/outputs/apk/staging/com.skgtecnologia.sisem-v*.apk
adb shell am start -n com.skgtecnologia.sisem/.ui.MainActivity   # or just open it
# Close the app after the first launch so the file is created.

# 2. Force-stop and overwrite the cached android_id
adb shell am force-stop com.skgtecnologia.sisem
adb shell "run-as com.skgtecnologia.sisem sh -c 'printf 7ec2c127b2095132 > files/android_id'"

# 3. Verify
adb shell "run-as com.skgtecnologia.sisem cat files/android_id"
# → 7ec2c127b2095132
```

> `run-as` only works on `debug`/`staging` builds (debuggable). For `release` you need root.

If `run-as` is not available, push via a temp file:

```bash
echo -n "7ec2c127b2095132" > /tmp/android_id
adb push /tmp/android_id /sdcard/android_id
adb shell "run-as com.skgtecnologia.sisem cp /sdcard/android_id files/android_id"
```

### Resetting

To go back to the real Android ID, just wipe app data — next launch will regenerate the file from `Settings.Secure.ANDROID_ID`:

```bash
adb shell pm clear com.skgtecnologia.sisem
```

## Reproducing the "Registra retención de camilla" failure

This is the issue captured in `Desktop/Android/falla.mp4` (May 2026).

### Steps

1. Login with one of the test users above.
2. Make sure the device has an active incident assigned (the side menu only shows "Registra retención de camilla" when there's an active operation with `id_aph`).
3. Open the side drawer → tap **Registra retención de camilla**.
4. The patient list loads (`POST /sisem-api/v1/screen/pre-aph-stretcher-retention` → 200 in ~200 ms).
5. Tap a patient.
6. Wait ~15 s. The form never loads and a red banner appears:
   > **Servicio no disponible** — Sucedió algo inesperado, por favor intenta de nuevo

### What's happening under the hood

- The failing call is `POST /sisem-api/v1/screen/aph-stretcher-retention` with body `{"params":{"id_aph":"<id>"}}` (see `StretcherRetentionApi.kt:17`, `StretcherRetentionRemoteDataSource.kt:31`).
- OkHttp client timeout for bearer-authenticated calls is **15 s** — `di/CoreNetworkModule.kt:59` (`CLIENT_TIMEOUT_DEFAULTS = 15_000L`) wired in `di/BearerNetworkModule.kt:35`.
- The Network Inspector shows the request hanging the full 15 s with **no status code** and an empty response — i.e. `SocketTimeoutException`.
- `NetworkApi.parseError` (`data/remote/api/NetworkApi.kt:87`) maps `SocketTimeoutException` to the strings `error_server_title` / `error_server_description`, which is exactly the banner from the video (`res/values/strings.xml:29-30`).

**Diagnosis:** Backend regression. The `aph-stretcher-retention` endpoint is not responding within 15 s for the `id_aph` returned by `pre-aph-stretcher-retention`. The Android side is behaving correctly (it surfaces a server-error banner instead of hanging forever).

### Useful inspection commands while reproducing

```bash
# Tail app logs
adb logcat -s SISEM:* OkHttp:* AndroidRuntime:E

# Watch the cached network log written by NetworkApi.storeSuccessResponse / storeErrorResponse
adb shell "run-as com.skgtecnologia.sisem cat files/android_networking.txt"
```

(See `ANDROID_NETWORKING_FILE_NAME` in `commons/resources/`.)

## Notes for future tests

- Always force-stop the app after rewriting `files/android_id` — the value is read on demand but cached in memory after the first call.
- The `pre-aph-stretcher-retention` call also depends on the **active incident** held in `IncidentCacheDataSource`. If you wipe app data you'll need to re-login *and* have a fresh incident assigned, otherwise the pre-screen shows the empty state (`emptyStretcherRetentionHeader()`).
- Test users for the `Cursos` course role share the same vehicle serial, so all three can sign in on the same emulator if you switch sessions.

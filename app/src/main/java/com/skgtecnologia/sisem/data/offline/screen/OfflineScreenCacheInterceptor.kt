package com.skgtecnologia.sisem.data.offline.screen

import com.skgtecnologia.sisem.commons.connectivity.Connectivity
import com.skgtecnologia.sisem.commons.connectivity.NetworkMonitor
import com.skgtecnologia.sisem.commons.extensions.resultOf
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import timber.log.Timber
import java.io.IOException
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

/** Set on a response that came from the device instead of the network. */
const val OFFLINE_SOURCE_HEADER = "X-Sisem-Offline"

private const val SCREEN_PATH_SEGMENT = "/screen/"
private const val HTTP_OK = 200
private val JSON = "application/json; charset=utf-8".toMediaType()

/**
 * Every form in the app is drawn from a screen the backend sends, and every one of those is a
 * POST — which OkHttp's own cache never stores. Without this, losing signal meant losing every
 * form, including the medical history in the middle of attending a patient.
 *
 * Each screen that arrives is kept, keyed by its address and request body, and served back when
 * the network fails. With no network at all the copy is served straight away, so the crew is not
 * left waiting on a timeout to see a form the device already has.
 */
@Singleton
class OfflineScreenCacheInterceptor @Inject constructor(
    private val store: ScreenCacheStore,
    private val networkMonitor: NetworkMonitor
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!request.isScreenRequest()) return chain.proceed(request)

        val key = request.cacheKey()
        val servedOffline = if (networkMonitor.connectivity.value == Connectivity.OFFLINE) {
            store.get(key)
        } else {
            null
        }

        return servedOffline?.let { request.offlineResponse(it) }
            ?: fetchAndKeep(chain, request, key)
    }

    private fun fetchAndKeep(chain: Interceptor.Chain, request: Request, key: String): Response {
        val response = try {
            chain.proceed(request)
        } catch (exception: IOException) {
            val cached = store.get(key) ?: throw exception
            return request.offlineResponse(cached)
        }

        return if (response.isSuccessful) response.kept(key) else response
    }

    private fun Response.kept(key: String): Response {
        val responseBody = body ?: return this
        val contentType = responseBody.contentType()
        val bytes = responseBody.bytes()

        // A copy that cannot be written must never cost the crew the screen they just loaded.
        resultOf { store.put(key, bytes) }
            .onFailure { Timber.w(it, "Could not keep a copy of $request") }

        return newBuilder().body(bytes.toResponseBody(contentType)).build()
    }
}

internal fun Request.isScreenRequest(): Boolean =
    method == "POST" && url.encodedPath.contains(SCREEN_PATH_SEGMENT)

/**
 * Address and body together identify a screen: the body carries the serial, turn, incident or
 * medical history it was asked for, and none of those carry a timestamp, so the same screen
 * always yields the same key.
 */
internal fun Request.cacheKey(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    digest.update(method.toByteArray())
    digest.update(url.toString().toByteArray())
    body?.let { requestBody ->
        digest.update(Buffer().also { requestBody.writeTo(it) }.readByteArray())
    }
    return digest.digest().joinToString(separator = "") { "%02x".format(it) }
}

private fun Request.offlineResponse(body: ByteArray): Response = Response.Builder()
    .request(this)
    .protocol(Protocol.HTTP_1_1)
    .code(HTTP_OK)
    .message("OK")
    .header(OFFLINE_SOURCE_HEADER, "cache")
    .body(body.toResponseBody(JSON))
    .build()

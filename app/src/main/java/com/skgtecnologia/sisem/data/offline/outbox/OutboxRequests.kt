package com.skgtecnologia.sisem.data.offline.outbox

import com.skgtecnologia.sisem.data.offline.screen.OFFLINE_SOURCE_HEADER
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.time.Instant
import java.util.UUID

/**
 * Same value on the first attempt and on every resend, so the server can tell a write it already
 * stored from a new one. A timeout can strike after the server saved the write but before the
 * answer arrived, and only this tells the two apart.
 */
const val IDEMPOTENCY_KEY_HEADER = "Idempotency-Key"

/** When the crew actually made the write, which can be hours before it reaches the server. */
const val CLIENT_CREATED_AT_HEADER = "X-Client-Created-At"

/**
 * Marks a write the outbox is sending again. Every interceptor that would otherwise rewrite the
 * request — audit headers, the role-based token — leaves it as it was, and the server can see it
 * was sent late.
 */
const val OUTBOX_REPLAY_HEADER = "X-Sisem-Replay"

internal const val AUTHORIZATION_HEADER = "Authorization"
private const val BEARER_PREFIX = "Bearer "
private const val SCREEN_PATH_SEGMENT = "/screen/"
private const val HTTP_ACCEPTED = 202

/**
 * The writes a crew makes while working. Every one of them answers with an empty body, so
 * accepting one on the server's behalf hides nothing the caller was waiting for.
 *
 * Left out on purpose: sign-in, password changes and device association, which need the answer
 * to continue; locations and biometrics, which already have their own queues.
 */
private val QUEUEABLE_PATHS = listOf(
    Regex("/aph$"),
    Regex("/aph/files$"),
    Regex("/aph/delete-file$"),
    Regex("/aph/save-stretcher-retention$"),
    Regex("/aph/send-mail$"),
    Regex("/preoperational/[^/]+$"),
    Regex("/novelty/[^/]+/[^/]+$"),
    Regex("/transfer-return$"),
    Regex("/crew$")
)

internal fun Request.isQueueableWrite(): Boolean {
    val path = url.encodedPath
    // "screen/aph" would otherwise match "/aph": a screen is read, never queued.
    return method == "POST" &&
        !path.contains(SCREEN_PATH_SEGMENT) &&
        QUEUEABLE_PATHS.any { it.containsMatchIn(path) }
}

internal fun Request.isOutboxReplay(): Boolean = header(OUTBOX_REPLAY_HEADER) != null

/** Adds the identity and creation time the write keeps from now on, unless it already has them. */
internal fun Request.stamped(now: Instant = Instant.now()): Request = newBuilder().apply {
    if (header(IDEMPOTENCY_KEY_HEADER) == null) header(IDEMPOTENCY_KEY_HEADER, UUID.randomUUID().toString())
    if (header(CLIENT_CREATED_AT_HEADER) == null) header(CLIENT_CREATED_AT_HEADER, now.toString())
}.build()

internal fun Request.bearerToken(): String? =
    header(AUTHORIZATION_HEADER)?.takeIf { it.startsWith(BEARER_PREFIX) }?.removePrefix(BEARER_PREFIX)?.trim()

internal fun Request.bodyBytes(): ByteArray? =
    body?.let { requestBody -> Buffer().also { requestBody.writeTo(it) }.readByteArray() }

/**
 * What the app gets back when a write is kept to send later: a success with no body, the same
 * shape every queueable endpoint answers with, so callers carry on as if it had gone through.
 */
internal fun Request.acceptedOffline(): Response = Response.Builder()
    .request(this)
    .protocol(Protocol.HTTP_1_1)
    .code(HTTP_ACCEPTED)
    .message("Accepted")
    .header(OFFLINE_SOURCE_HEADER, "queued")
    .body(ByteArray(0).toResponseBody(null))
    .build()

/** One "name: value" per line, without the token: header values can never contain a newline. */
internal fun Headers.serialized(): String =
    filter { (name, _) -> !name.equals(AUTHORIZATION_HEADER, ignoreCase = true) }
        .joinToString(separator = "\n") { (name, value) -> "$name: $value" }

internal fun String.deserializedHeaders(): Headers = Headers.Builder().apply {
    lineSequence().filter { it.isNotBlank() }.forEach { add(it) }
}.build()

/** Rebuilds the write as it was made, marked as a resend and not yet signed. */
internal fun OutboxEntity.toReplayRequest(body: ByteArray?): Request = Request.Builder()
    .url(url)
    .headers(headers.deserializedHeaders())
    .header(OUTBOX_REPLAY_HEADER, requestId)
    .method(method, body?.toRequestBody(contentType?.toMediaTypeOrNull()))
    .build()

internal fun String.asBearer(): String = BEARER_PREFIX + this

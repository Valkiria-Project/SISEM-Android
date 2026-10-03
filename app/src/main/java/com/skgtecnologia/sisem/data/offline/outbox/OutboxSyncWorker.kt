package com.skgtecnologia.sisem.data.offline.outbox

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

private const val HTTP_UNAUTHORIZED = 401
private const val HTTP_FORBIDDEN = 403
private const val HTTP_REQUEST_TIMEOUT = 408
private const val HTTP_TOO_MANY_REQUESTS = 429
private const val HTTP_SERVER_ERROR = 500
private const val ERROR_BODY_PREVIEW_BYTES = 512L

/** What happened to one queued write on this run. */
internal sealed interface SendOutcome {
    data object Sent : SendOutcome

    /** The server refused it and would again: set it aside, keep going. */
    data class Rejected(val reason: String) : SendOutcome

    /** Its creator cannot sign it right now: hold their writes, send everyone else's. */
    data class WaitForCreator(val reason: String) : SendOutcome

    /** The network or the server is down: nothing else will get through either. */
    data class Unavailable(val reason: String) : SendOutcome
}

/** How a whole pass over the queue ended. */
internal enum class DrainResult {
    /** Nothing left that could be sent. */
    DONE,

    /** Something is still waiting — for the network, the server, or its creator. */
    TRY_AGAIN
}

/**
 * Sends queued writes in the order they were made.
 *
 * Order is kept per creator: once one of a user's writes has to wait, every later one of theirs
 * waits too, so a medical history is never overtaken by its own photos. Other users' writes are
 * independent and still go out.
 */
class OutboxSender @Inject constructor(
    private val store: OutboxStore,
    private val signer: OutboxSigner,
    @BearerAuthentication private val client: OkHttpClient
) {

    internal suspend fun drain(): DrainResult {
        val held = mutableSetOf<String>()

        for (write in store.pending()) {
            val creator = write.entry.createdBy
            if (creator != null && creator in held) continue

            when (val outcome = attempt(write)) {
                SendOutcome.Sent -> {
                    store.complete(write)
                }

                is SendOutcome.Rejected -> {
                    Timber.w("Outbox rejected ${write.entry.url}: ${outcome.reason}")
                    store.reject(write, outcome.reason)
                }

                is SendOutcome.WaitForCreator -> {
                    store.retryLater(write, outcome.reason)
                    creator?.let(held::add)
                }

                is SendOutcome.Unavailable -> {
                    store.retryLater(write, outcome.reason)
                    return DrainResult.TRY_AGAIN
                }
            }
        }

        return if (held.isEmpty()) DrainResult.DONE else DrainResult.TRY_AGAIN
    }

    private suspend fun attempt(write: QueuedWrite): SendOutcome {
        write.unreadable?.let {
            return SendOutcome.Rejected("the body can no longer be read on this device: ${it::class.simpleName}")
        }

        val replay = write.entry.toReplayRequest(write.body)
        return when (val signing = signer.sign(replay, write.entry.createdBy)) {
            is SigningResult.Signed -> send(signing.request)
            is SigningResult.WaitForCreator -> SendOutcome.WaitForCreator(signing.reason)
            SigningResult.NoCreator -> SendOutcome.Rejected("it was queued with nobody signed in")
        }
    }

    private suspend fun send(request: Request): SendOutcome = withContext(Dispatchers.IO) {
        try {
            client.newCall(request).execute().use { it.toOutcome() }
        } catch (exception: IOException) {
            SendOutcome.Unavailable(exception::class.simpleName ?: "IOException")
        }
    }
}

@HiltWorker
class OutboxSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val sender: OutboxSender
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = when (sender.drain()) {
        DrainResult.DONE -> Result.success()
        DrainResult.TRY_AGAIN -> Result.retry()
    }
}

internal fun Response.toOutcome(): SendOutcome = when {
    isSuccessful -> {
        SendOutcome.Sent
    }

    // The creator's session is the problem, not the write.
    code == HTTP_UNAUTHORIZED || code == HTTP_FORBIDDEN -> {
        SendOutcome.WaitForCreator("HTTP $code")
    }

    code == HTTP_REQUEST_TIMEOUT || code == HTTP_TOO_MANY_REQUESTS || code >= HTTP_SERVER_ERROR -> {
        SendOutcome.Unavailable("HTTP $code")
    }

    else -> {
        SendOutcome.Rejected("HTTP $code: ${peekBody(ERROR_BODY_PREVIEW_BYTES).string()}")
    }
}

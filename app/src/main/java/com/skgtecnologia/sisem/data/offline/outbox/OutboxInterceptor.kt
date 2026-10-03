package com.skgtecnologia.sisem.data.offline.outbox

import com.skgtecnologia.sisem.commons.connectivity.Connectivity
import com.skgtecnologia.sisem.commons.connectivity.NetworkMonitor
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Lets a crew keep working without signal: a write that cannot reach the server is kept on the
 * device and answered as accepted, then sent by [OutboxSyncWorker] once the network is back.
 *
 * It sits after the token interceptor, so the request it keeps is already signed — which is how
 * it learns who made it, and so who it may later be sent as.
 */
@Singleton
class OutboxInterceptor @Inject constructor(
    private val store: OutboxStore,
    private val scheduler: OutboxSyncScheduler,
    private val networkMonitor: NetworkMonitor,
    private val authRepository: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        if (!original.isQueueableWrite() || original.isOutboxReplay()) return chain.proceed(original)

        // Stamped before the first attempt, so a resend carries the same identity and time as the
        // attempt that may have reached the server unseen.
        val request = original.stamped()

        // While anything is queued, new writes queue behind it. Otherwise a write made after the
        // signal came back could overtake one made before — the photos reaching the server ahead
        // of the medical history they belong to.
        val mustQueue = networkMonitor.connectivity.value == Connectivity.OFFLINE ||
            runBlocking { store.hasPending() }

        return if (mustQueue) keep(request) else sendOrKeep(chain, request)
    }

    private fun sendOrKeep(chain: Interceptor.Chain, request: Request): Response = try {
        chain.proceed(request)
    } catch (exception: IOException) {
        keep(request, exception)
    }

    private fun keep(request: Request, networkFailure: IOException? = null): Response {
        runBlocking { resultOf { store.enqueue(request, createdBy = creatorOf(request)) } }
            .onFailure { storageFailure ->
                // Never answer "accepted" for a write that is not safely on the device: the crew
                // would believe it went through. Surface it as the network error it started as.
                throw IOException("Could not keep the write to send later", storageFailure).apply {
                    networkFailure?.let(::addSuppressed)
                }
            }

        scheduler.schedule()
        return request.acceptedOffline()
    }

    private suspend fun creatorOf(request: Request): String? {
        val token = request.bearerToken() ?: return null
        return authRepository.getAllAccessTokens().firstOrNull { it.accessToken == token }?.username
    }
}

package com.skgtecnologia.sisem.data.offline.outbox

import com.skgtecnologia.sisem.commons.connectivity.Connectivity
import com.skgtecnologia.sisem.commons.connectivity.NetworkMonitor
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Starts a send whenever something may have unblocked the queue: coming back online, or someone
 * signing in — writes held for their creator can only go out once that creator has a session
 * again. Each new write schedules a send on its own; this covers what changes around them.
 */
@Singleton
class OutboxSyncTrigger @Inject constructor(
    private val store: OutboxStore,
    private val scheduler: OutboxSyncScheduler,
    private val networkMonitor: NetworkMonitor,
    private val authRepository: AuthRepository
) {

    fun start(scope: CoroutineScope) {
        scope.launch {
            val backOnline = networkMonitor.connectivity
                .map { it != Connectivity.OFFLINE }
                .distinctUntilChanged()
                .filter { it }

            val signedIn = flow { emitAll(authRepository.observeCurrentAccessToken()) }
                .map { it?.username }
                .distinctUntilChanged()

            // Both start with their current value, so a queue left over from before the app was
            // closed is picked up on launch as well.
            merge(backOnline, signedIn).collect {
                if (store.hasPending()) scheduler.schedule()
            }
        }
    }
}

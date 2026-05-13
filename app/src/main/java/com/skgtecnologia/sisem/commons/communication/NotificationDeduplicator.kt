package com.skgtecnologia.sisem.commons.communication

import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Prevents the same push notification payload from being processed more than once
 * within a short window when it arrives via multiple entry points (FCM
 * onMessageReceived AND a tap that re-launches MainActivity carrying the same data).
 *
 * Only DB writes / backend fetches are deduplicated. UI event publishing
 * (NotificationEventHandler.publishNotificationEvent) is intentionally left
 * unguarded so the foreground UI always receives the event even when the
 * initial FCM delivery happened while no screen was subscribed.
 */
@Singleton
class NotificationDeduplicator @Inject constructor() {

    private val fingerprints = ConcurrentHashMap<String, Long>()

    fun shouldProcess(
        notification: NotificationData,
        windowMillis: Long = DEFAULT_WINDOW_MILLIS,
    ): Boolean {
        val now = System.currentTimeMillis()
        pruneExpired(now, windowMillis)

        val key = notification.toString()
        val previous = fingerprints[key]
        return if (previous != null && now - previous <= windowMillis) {
            false
        } else {
            fingerprints[key] = now
            true
        }
    }

    private fun pruneExpired(now: Long, windowMillis: Long) {
        val iterator = fingerprints.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (now - entry.value > windowMillis) {
                iterator.remove()
            }
        }
    }

    companion object {
        const val DEFAULT_WINDOW_MILLIS: Long = 10_000L
    }
}

package com.skgtecnologia.sisem.data.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    private val serviceJob = SupervisorJob()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            handlePushNotification(remoteMessage)
        }
    }

    override fun onNewToken(token: String) {
        Timber.d("Refreshed FCM token: $token")
    }

    private fun handlePushNotification(remoteMessage: RemoteMessage) {
        Timber.d("Message data payload: ${remoteMessage.data}")
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}

package com.skgtecnologia.sisem.data.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.skgtecnologia.sisem.data.notifications.model.NotificationData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationsManager: NotificationsManager

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

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

        notificationsManager.buildNotificationData(remoteMessage.data).also {
            storeNotification(it)
        }
    }

    private fun storeNotification(notificationData: NotificationData) {
        Timber.d("storeNotification with ${notificationData.notificationType.title}")
        serviceScope.launch {
//            storeNotification.invoke(
//                Notification(
//                    title = notificationData.title.orEmpty(),
//                    body = notificationData.body.orEmpty(),
//                    deepLink = notificationData.deepLink
//                )
//            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}

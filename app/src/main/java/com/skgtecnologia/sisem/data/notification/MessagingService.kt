package com.skgtecnologia.sisem.data.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.valkiria.uicomponents.bricks.notification.NotificationData
import com.skgtecnologia.sisem.domain.notification.usecases.StoreNotification
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
    lateinit var storeNotification: StoreNotification

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

        notificationsManager.buildNotificationData(remoteMessage.data)?.also {
            storeNotification(it)
        }
    }

    private fun storeNotification(notificationData: NotificationData) {
        Timber.d("storeNotification with ${notificationData.notificationType.title}")
        serviceScope.launch {
            storeNotification.invoke(notificationData)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}

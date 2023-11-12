package com.skgtecnologia.sisem.data.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.data.notifications.model.IncidentAssignedNotification
import com.skgtecnologia.sisem.data.notifications.model.NOTIFICATION_TYPE_KEY
import com.skgtecnologia.sisem.data.notifications.model.NotificationData
import com.skgtecnologia.sisem.data.notifications.model.NotificationType
import com.skgtecnologia.sisem.ui.MainActivity
import javax.inject.Inject
import timber.log.Timber

class NotificationsManager @Inject constructor(private val context: Context) {

    fun buildNotificationData(notificationDataMap: Map<String, String>): NotificationData {
        lateinit var pendingIntent: PendingIntent

        val notificationType = NotificationType.from(
            notificationDataMap[NOTIFICATION_TYPE_KEY].orEmpty()
        )

        Timber.d("notificationType ${notificationType?.title}")
        when (notificationType) {
            NotificationType.INCIDENT_ASSIGNED -> {
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )
            }

            else -> Timber.d("no-op")
        }

        return IncidentAssignedNotification(data = notificationDataMap).also {
            sendNotification(it)
        }
    }

    private fun sendNotification(notificationData: NotificationData) {
        Timber.d("sendNotification with ${notificationData.notificationType.title}")
        // FIXME: Finish Notifications work
        NotificationEventHandler.publishNotificationEvent(notificationData.notificationType.title)

//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = NotificationCompat.Builder(context, NotificationsConstants.CHANNEL_ID)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentTitle(notificationData.title)
//            .setContentText(notificationData.body)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setSound(defaultSoundUri)
//            .setContentIntent(notificationData.pendingIntent)
//
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                NotificationsConstants.CHANNEL_ID,
//                NotificationsConstants.CHANNEL_NAME,
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(Instant.now().hashCode(), notificationBuilder.build())
    }
}

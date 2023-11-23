package com.skgtecnologia.sisem.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.ui.MainActivity
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.bricks.notification.model.getNotificationDataByType
import java.time.Instant
import javax.inject.Inject
import timber.log.Timber

private const val CHANNEL_ID = "notifications"
private const val CHANNEL_NAME = "Notificaciones"

class NotificationsManager @Inject constructor(private val context: Context) {

    fun buildNotificationData(notificationDataMap: Map<String, String>): NotificationData? {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationData = getNotificationDataByType(notificationDataMap)

        return notificationData?.also {
            sendNotification(it, pendingIntent)
        }
    }

    private fun sendNotification(notificationData: NotificationData, pendingIntent: PendingIntent) {
        Timber.d("sendNotification with ${notificationData.notificationType.title}")
        NotificationEventHandler.publishNotificationEvent(notificationData)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notificationData.notificationType.title)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(Instant.now().hashCode(), notificationBuilder.build())
    }
}

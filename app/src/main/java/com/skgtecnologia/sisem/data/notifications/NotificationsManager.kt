package com.skgtecnologia.sisem.data.notifications

import android.content.Context
import com.skgtecnologia.sisem.data.notifications.model.NotificationData
import javax.inject.Inject

class NotificationsManager @Inject constructor(private val context: Context) {

    fun buildNotificationData(notificationDataMap: Map<String, String>): NotificationData {
//        lateinit var pendingIntent: PendingIntent
//        var deepLink: String? = null
//
//        val notificationType = extraData.getOrDefault(
//            NotificationsConstants.PAYLOAD_NOTIFICATION_TYPE,
//            NotificationsConstants.NOTIFICATION_TYPE_DEFAULT
//        )
//
//        when (notificationType) {
//            NotificationsConstants.NOTIFICATION_TYPE_DEFAULT -> {
//                val intent = Intent(context, AuthActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//                pendingIntent = PendingIntent.getActivity(
//                    context,
//                    0,
//                    intent,
//                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//                )
//            }
//            NotificationsConstants.NOTIFICATION_TYPE_MESSAGE -> {
//                val intent = Intent(context, AuthActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//                pendingIntent = PendingIntent.getActivity(
//                    context,
//                    0,
//                    intent,
//                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//                )
//            }
//            NotificationsConstants.NOTIFICATION_TYPE_REQUEST_CLARIFICATION -> {
//                val ticketNumber = extraData.getOrDefault(NotificationsConstants.PAYLOAD_TICKET_NUMBER, "")
//                val ticketType = when (extraData[NotificationsConstants.PAYLOAD_TICKET_TYPE]) {
//                    NotificationsConstants.TICKET_TYPE_NATIONAL -> NavigationConstants.DEEPLINK_ARG_NATIONAL
//                    else -> NavigationConstants.DEEPLINK_ARG_TRANSPORT
//                }
//
//                deepLink = NavigationConstants.DEEPLINK_VALUE_TICKET_CLARIFICATION
//                    .replace("{ticketType}", ticketType)
//                    .replace("{ticketNumber}", ticketNumber)
//                pendingIntent = TaskStackBuilder.create(context.applicationContext).run {
//                    addNextIntentWithParentStack(
//                        Intent(
//                            Intent.ACTION_VIEW,
//                            deepLink.toUri()
//                        )
//                    )
//                    getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//                }
//            }
//        }
        return NotificationData(mapOf()).also {
            handleNotification(it)
        }
    }

    fun handleNotification(notificationData: NotificationData) {
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

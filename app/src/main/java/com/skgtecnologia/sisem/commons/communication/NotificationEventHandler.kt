package com.skgtecnologia.sisem.commons.communication

import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal object NotificationEventHandler {

    fun publishNotificationEvent(data: NotificationData) {
        CoroutineScope(Dispatchers.IO).launch {
            EventBus.publish(data)
        }
    }

    fun subscribeNotificationEvent(onEvent: (data: NotificationData) -> Unit) {
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            EventBus.subscribe<NotificationData> { data ->
                onEvent(data)
            }
        }
    }
}

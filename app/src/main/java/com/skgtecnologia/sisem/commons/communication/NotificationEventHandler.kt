package com.skgtecnologia.sisem.commons.communication

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal object NotificationEventHandler {

    fun publishNotificationEvent(data: String) {
        CoroutineScope(Dispatchers.IO).launch {
            EventBus.publish(data)
        }
    }

    fun subscribeNotificationEvent(onEvent: (data: String) -> Unit) {
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            EventBus.subscribe<String> { data ->
                onEvent(data)
            }
        }
    }
}

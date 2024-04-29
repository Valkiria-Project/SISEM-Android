package com.skgtecnologia.sisem.commons.communication

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal object UnauthorizedEventHandler {

    fun publishUnauthorizedEvent(username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            EventBus.publish(AppEvent.UnauthorizedSession(username))
        }
    }

    fun subscribeUnauthorizedEvent(onEvent: (AppEvent) -> Unit) {
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            EventBus.subscribe<AppEvent> { appEvent ->
                onEvent(appEvent)
            }
        }
    }
}

sealed class AppEvent {
    data class UnauthorizedSession(val username: String) : AppEvent()
}

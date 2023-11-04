package com.skgtecnologia.sisem.commons.communication

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal object UnauthorizedEventHandler {

    fun publishUnauthorizedEvent() {
        CoroutineScope(Dispatchers.IO).launch {
            EventBus.publish(AppEvent.UNAUTHORIZED_SESSION)
        }
    }

    suspend fun subscribeUnauthorizedEvent(onEvent: (AppEvent) -> Unit) {
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            EventBus.subscribe<AppEvent> { cvvRequestModelEvent ->
                onEvent(cvvRequestModelEvent)
            }
        }
    }
}

enum class AppEvent {
    UNAUTHORIZED_SESSION
}

package com.skgtecnologia.sisem.commons.communication

import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlin.coroutines.coroutineContext

internal object EventBus {
    var events = MutableSharedFlow<Any>()
        private set

    suspend fun publish(event: Any) {
        events.emit(event)
    }

    suspend inline fun <reified T> subscribe(crossinline onEvent: (T) -> Unit) {
        events.filterIsInstance<T>()
            .collect { event ->
                coroutineContext.ensureActive()
                onEvent(event)
            }
    }
}

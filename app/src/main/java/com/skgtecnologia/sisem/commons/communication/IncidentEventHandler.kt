package com.skgtecnologia.sisem.commons.communication

import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal object IncidentEventHandler {

    fun publishIncidentErrorEvent(bannerUiModel: BannerUiModel) {
        CoroutineScope(Dispatchers.IO).launch {
            EventBus.publish(bannerUiModel)
        }
    }

    fun subscribeIncidentErrorEvent(onEvent: (bannerUiModel: BannerUiModel) -> Unit) {
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            EventBus.subscribe<BannerUiModel> { data ->
                onEvent(data)
            }
        }
    }
}

package com.skgtecnologia.sisem.domain.notification.usecases

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.notification.repository.NotificationRepository
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import javax.inject.Inject

class StoreNotification @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(notification: NotificationData): Result<Unit> = resultOf {
        notificationRepository.storeNotification(notification)
    }
}

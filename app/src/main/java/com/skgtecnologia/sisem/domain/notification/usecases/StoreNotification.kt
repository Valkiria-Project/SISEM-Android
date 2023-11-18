package com.skgtecnologia.sisem.domain.notification.usecases

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.notification.model.NotificationData
import com.skgtecnologia.sisem.domain.notification.repository.NotificationRepository
import javax.inject.Inject

class StoreNotification @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    suspend fun invoke(notification: NotificationData): Result<Unit> = resultOf {
        notificationRepository.storeNotification(notification)
    }
}

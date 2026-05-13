package com.skgtecnologia.sisem.domain.notification.usecases

import com.skgtecnologia.sisem.commons.communication.NotificationDeduplicator
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.notification.repository.NotificationRepository
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import timber.log.Timber
import javax.inject.Inject

class StoreNotification @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val deduplicator: NotificationDeduplicator,
) {

    suspend operator fun invoke(notification: NotificationData): Result<Unit> = resultOf {
        if (!deduplicator.shouldProcess(notification)) {
            Timber.tag("PushFlow").d(
                "StoreNotification dedup skip type=${notification.notificationType}"
            )
            return@resultOf
        }
        notificationRepository.storeNotification(notification)
    }
}

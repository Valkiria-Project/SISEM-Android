package com.skgtecnologia.sisem.domain.notification.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.domain.notification.repository.NotificationRepository
import com.valkiria.uicomponents.bricks.notification.NotificationUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNotifications @Inject constructor(
    private val notificationsRepository: NotificationRepository
) {

    @CheckResult
    operator fun invoke(): Flow<List<NotificationUiModel>?>? =
        notificationsRepository.observeNotifications()
}

package com.valkiria.uicomponents.bricks.notification

import androidx.compose.runtime.Composable
import com.valkiria.uicomponents.action.GenericUiAction.NotificationAction
import com.valkiria.uicomponents.mocks.getAssignedIncidentNotificationUiModel
import timber.log.Timber

@Composable
fun OnNotificationHandler(
    hasNotification: Boolean,
    onAction: (action: NotificationAction) -> Unit = {}
) {
    if (hasNotification) {
        NotificationView(uiModel = getAssignedIncidentNotificationUiModel()) {
            Timber.d("Notification action $it")
            onAction(it)
        }
    }
}

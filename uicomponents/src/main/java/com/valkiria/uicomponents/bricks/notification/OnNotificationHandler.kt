package com.valkiria.uicomponents.bricks.notification

import androidx.compose.runtime.Composable
import com.valkiria.uicomponents.action.GenericUiAction.NotificationAction
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import timber.log.Timber

@Composable
fun OnNotificationHandler(
    notificationData: NotificationData?,
    onAction: (action: NotificationAction) -> Unit
) {
    notificationData?.let {
        NotificationView(uiModel = notificationData.mapToUi()) {
            Timber.d("Notification action $it")
            onAction(it)
        }
    }
}

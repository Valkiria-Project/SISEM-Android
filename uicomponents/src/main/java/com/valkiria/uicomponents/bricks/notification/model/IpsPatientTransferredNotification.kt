package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.IPS_PATIENT_TRANSFERRED

data class IpsPatientTransferredNotification(
    override val notificationType: NotificationType = IPS_PATIENT_TRANSFERRED,
    val headquartersName: String,
    val headquartersAddress: String
) : NotificationData

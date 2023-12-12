package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.IPS_PATIENT_TRANSFERRED
import java.time.LocalTime

data class IpsPatientTransferredNotification(
    override val notificationType: NotificationType = IPS_PATIENT_TRANSFERRED,
    override val time: LocalTime,
    val headquartersName: String,
    val headquartersAddress: String
) : NotificationData

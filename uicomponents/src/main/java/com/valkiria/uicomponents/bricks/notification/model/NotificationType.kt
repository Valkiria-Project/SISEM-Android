package com.valkiria.uicomponents.bricks.notification.model

enum class NotificationType(
    val title: String,
    val icon: String,
    val iconColor: String,
    val descriptionDecorator: String? = null,
    val contentLeftDecorator: String? = null
) {
    INCIDENT_ASSIGNED(
        title = "Incidente Asignado",
        icon = "ic_assigned_incident",
        iconColor = "#3CF2DD"
    ),
    TRANSMILENIO_AUTHORIZATION(
        title = "Carril de Transmilenio autorizado",
        icon = "ic_road",
        iconColor = "#3CF2DD",
        descriptionDecorator = "No. ",
        contentLeftDecorator = "Autoriza "
    ),
    TRANSMILENIO_DENIED(
        title = "Carril de Transmilenio no autorizado",
        icon = "ic_road",
        iconColor = "#F55757"
    ),
    NO_PRE_OPERATIONAL_GENERATED_CRUE(
        title = "No se ha generado preoperacional para reportar al CRUE",
        icon = "ic_check",
        iconColor = "#F55757"
    ),
    SUPPORT_REQUEST_ON_THE_WAY(
        title = "Solicitud de apoyo en camino",
        icon = "ic_ambulance",
        iconColor = "#42A4FA"
    ),
    IPS_PATIENT_TRANSFERRED(
        title = "Paciente traslado a IPS",
        icon = "ic_user_2",
        iconColor = "#42A4FA"
    ),
    STRETCHER_RETENTION_ENABLE(
        title = "Retención de camilla habilitada",
        icon = "ic_stretcher",
        iconColor = "#42A4FA"
    ),
    CLOSING_OF_APH(
        title = "No se ha generado preoperacional para reportar al CRUE",
        icon = "ic_check",
        iconColor = "#42A4FA"
    );

    companion object {
        fun from(type: String): NotificationType? = entries.firstOrNull { it.name == type }
    }
}

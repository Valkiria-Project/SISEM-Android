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
    );

    companion object {
        fun from(type: String): NotificationType? = entries.firstOrNull { it.name == type }
    }
}

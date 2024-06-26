package com.valkiria.uicomponents.components.incident.model

data class PatientUiModel(
    val id: Int,
    val fullName: String,
    val idAph: Int,
    val isPendingAph: Boolean = true,
    val disabled: Boolean = false
)

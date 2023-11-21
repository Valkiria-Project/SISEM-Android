package com.skgtecnologia.sisem.domain.incident.model

data class ResourceDetailModel(
    val id: Int,
    val code: String,
    val transitAgency: String,
    val icTransitAgency: String
)

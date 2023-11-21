package com.skgtecnologia.sisem.domain.incident.model

data class ResourceModel(
    val id: Int,
    val resourceId: Int,
    val resource: ResourceDetailModel
)

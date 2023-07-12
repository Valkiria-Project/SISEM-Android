package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.core.model.props.MarginsModel

data class MarginsResponse(
    val top: Int?,
    val bottom: Int?,
    val left: Int?,
    val right: Int?
)

fun MarginsResponse.mapToDomain() : MarginsModel = MarginsModel(
    top = top,
    bottom = bottom,
    left = left,
    right = right
)

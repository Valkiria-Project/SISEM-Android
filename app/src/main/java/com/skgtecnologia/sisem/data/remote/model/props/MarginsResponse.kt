package com.skgtecnologia.sisem.data.remote.model.props

import com.valkiria.uicomponents.props.MarginsUiModel

data class MarginsResponse(
    val top: Int?,
    val bottom: Int?,
    val left: Int?,
    val right: Int?
)

fun MarginsResponse.mapToUi() : MarginsUiModel = MarginsUiModel(
    top = top,
    bottom = bottom,
    left = left,
    right = right
)

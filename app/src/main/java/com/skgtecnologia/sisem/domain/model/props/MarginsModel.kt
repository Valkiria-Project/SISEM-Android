package com.skgtecnologia.sisem.domain.model.props

import android.os.Parcelable
import com.valkiria.uicomponents.props.MarginsUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarginsModel(
    val top: Int?,
    val bottom: Int?,
    val left: Int?,
    val right: Int?
) : Parcelable

// FIXME
fun MarginsModel.mapToUiModel(): MarginsUiModel = MarginsUiModel(
    top = top,
    bottom = bottom,
    left = left,
    right = right
)

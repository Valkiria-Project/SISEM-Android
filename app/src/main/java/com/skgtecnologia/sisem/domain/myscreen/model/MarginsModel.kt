package com.skgtecnologia.sisem.domain.myscreen.model

import android.os.Parcelable
import com.valkiria.uicomponents.components.MarginsUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarginsModel(
    val top: Int?,
    val bottom: Int?,
    val left: Int?,
    val right: Int?
) : Parcelable

fun MarginsModel.mapToUiModel() = MarginsUiModel(
    top = top,
    bottom = bottom,
    left = left,
    right = right
)

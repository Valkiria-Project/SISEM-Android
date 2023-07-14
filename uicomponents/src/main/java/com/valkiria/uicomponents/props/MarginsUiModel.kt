package com.valkiria.uicomponents.props

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarginsUiModel(
    val top: Int?,
    val bottom: Int?,
    val left: Int?,
    val right: Int?
) : Parcelable

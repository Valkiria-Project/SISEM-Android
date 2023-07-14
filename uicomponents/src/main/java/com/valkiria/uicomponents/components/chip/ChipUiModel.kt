package com.valkiria.uicomponents.components.chip

import android.os.Parcelable
import com.valkiria.uicomponents.props.ChipStyle
import com.valkiria.uicomponents.props.MarginsUiModel
import com.valkiria.uicomponents.props.TextStyle
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChipUiModel(
    val icon: String?,
    val text: String?,
    val textStyle: TextStyle,
    val style: ChipStyle,
    val margins: MarginsUiModel?
) : Parcelable

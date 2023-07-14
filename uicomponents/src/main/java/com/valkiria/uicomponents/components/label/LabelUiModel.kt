package com.valkiria.uicomponents.components.label

import android.os.Parcelable
import com.valkiria.uicomponents.props.MarginsUiModel
import com.valkiria.uicomponents.props.TextStyle
import kotlinx.parcelize.Parcelize

@Parcelize
data class LabelUiModel(
    val text: String,
    val textStyle: TextStyle,
    val margins: MarginsUiModel?
) : Parcelable

package com.valkiria.uicomponents.components.richlabel

import android.os.Parcelable
import com.valkiria.uicomponents.props.MarginsUiModel
import com.valkiria.uicomponents.props.TextStyle
import kotlinx.parcelize.Parcelize

@Parcelize
data class RichLabelUiModel(
    val text: String,
    val textStyle: TextStyle,
    val margins: MarginsUiModel?
) : Parcelable

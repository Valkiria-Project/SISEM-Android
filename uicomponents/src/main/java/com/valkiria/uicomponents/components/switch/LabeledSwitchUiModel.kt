package com.valkiria.uicomponents.components.switch

import android.os.Parcelable
import com.valkiria.uicomponents.props.MarginsUiModel
import com.valkiria.uicomponents.props.TextStyle
import kotlinx.parcelize.Parcelize

@Parcelize
data class LabeledSwitchUiModel(
    val text: String,
    val textStyle: TextStyle,
    val margins: MarginsUiModel?
) : Parcelable

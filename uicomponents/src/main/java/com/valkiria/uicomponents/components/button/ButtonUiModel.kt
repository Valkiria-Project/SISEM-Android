package com.valkiria.uicomponents.components.button

import android.os.Parcelable
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle
import com.valkiria.uicomponents.props.MarginsUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ButtonUiModel(
    val label: String,
    val style: ButtonStyle,
    val onClick: OnClick,
    val size: ButtonSize,
    val margins: MarginsUiModel?
) : Parcelable

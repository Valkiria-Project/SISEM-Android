package com.valkiria.uicomponents.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle
import com.valkiria.uicomponents.props.TextStyle

class ButtonUiModel private constructor(
    @DrawableRes val iconResId: Int?,
    val label: String?,
    val textStyle: TextStyle?,
    val style: ButtonStyle,
    val onClick: OnClick,
    val size: ButtonSize,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) {
    constructor(
        label: String,
        textStyle: TextStyle,
        style: ButtonStyle,
        onClick: OnClick,
        size: ButtonSize,
        arrangement: Arrangement.Horizontal,
        modifier: Modifier
    ) : this(null, label, textStyle, style, onClick, size, arrangement, modifier)

    constructor(
        @DrawableRes icon: Int,
        style: ButtonStyle,
        onClick: OnClick,
        size: ButtonSize,
        arrangement: Arrangement.Horizontal,
        modifier: Modifier
    ) : this(icon, null, null, style, onClick, size, arrangement, modifier)
}

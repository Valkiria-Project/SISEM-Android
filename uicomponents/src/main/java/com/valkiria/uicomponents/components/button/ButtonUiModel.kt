package com.valkiria.uicomponents.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle
import com.valkiria.uicomponents.props.TextStyle

@Suppress("LongParameterList")
class ButtonUiModel private constructor(
    val identifier: String,
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
        identifier: String,
        label: String,
        textStyle: TextStyle,
        style: ButtonStyle,
        onClick: OnClick,
        size: ButtonSize,
        arrangement: Arrangement.Horizontal,
        modifier: Modifier
    ) : this(identifier, null, label, textStyle, style, onClick, size, arrangement, modifier)

    constructor(
        identifier: String,
        @DrawableRes icon: Int,
        style: ButtonStyle,
        onClick: OnClick,
        size: ButtonSize,
        arrangement: Arrangement.Horizontal,
        modifier: Modifier
    ) : this(identifier, icon, null, null, style, onClick, size, arrangement, modifier)
}

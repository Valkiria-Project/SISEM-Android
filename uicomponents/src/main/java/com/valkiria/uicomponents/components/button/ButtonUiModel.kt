package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextStyle
import androidx.compose.ui.text.TextStyle as ComposeTextStyle

data class ButtonUiModel(
    override val identifier: String,
    val label: String,
    val leftIcon: String? = null,
    val style: ButtonStyle,
    val textStyle: TextStyle,
    val composeTextStyle: ComposeTextStyle? = null,
    val overrideColor: Color? = null,
    val onClick: OnClick,
    val size: ButtonSize,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.BUTTON
}

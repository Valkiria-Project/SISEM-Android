package com.valkiria.uicomponents.model.ui.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.valkiria.uicomponents.model.props.ButtonSize
import com.valkiria.uicomponents.model.props.ButtonStyle
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.button.OnClick

data class ButtonUiModel(
    val identifier: String,
    val label: String,
    val style: ButtonStyle,
    val textStyle: TextStyle,
    val overrideColor: Color? = null,
    val onClick: OnClick,
    val size: ButtonSize,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.BUTTON
}

package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle
import com.valkiria.uicomponents.props.TextStyle

data class ButtonModel(
    val identifier: String,
    val label: String,
    val style: ButtonStyle,
    val textStyle: TextStyle,
    val onClick: OnClick,
    val size: ButtonSize,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.BUTTON
}

fun ButtonModel.mapToUiModel(): ButtonUiModel {
    return ButtonUiModel(
        label = label,
        style = style,
        textStyle = textStyle,
        onClick = onClick,
        size = size,
        arrangement = arrangement,
        modifier = modifier
    )
}

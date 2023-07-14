package com.skgtecnologia.sisem.domain.model.bodyrow

import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.BUTTON
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle
import com.valkiria.uicomponents.props.MarginsUiModel

data class ButtonModel(
    val identifier: String,
    val label: String,
    val style: ButtonStyle,
    val onClick: OnClick,
    val size: ButtonSize,
    val margins: MarginsUiModel?
) : BodyRowModel {

    override val type: BodyRowType = BUTTON
}

fun ButtonModel.mapToUiModel(): ButtonUiModel {
    return ButtonUiModel(
        label = label,
        style = style,
        onClick = onClick,
        size = size,
        margins = margins
    )
}

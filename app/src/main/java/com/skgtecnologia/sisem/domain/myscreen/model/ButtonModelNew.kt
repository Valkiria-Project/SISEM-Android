package com.skgtecnologia.sisem.domain.myscreen.model

import com.valkiria.uicomponents.components.button.ButtonUiModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ButtonModelNew(
    val identifier: String,
    val label: String,
    val style: ButtonStyle,
    val onClick: OnClickType,
    val size: ButtonSize,
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = BodyRowType.BUTTON
}

fun ButtonModelNew.mapToUiModel(): ButtonUiModel {
    return ButtonUiModel(
        label = label,
        style = style.mapToUiModel(),
        onClick = onClick.mapToUiModel(),
        size = size.mapToUiModel(),
        margins = margins?.mapToUiModel()
    )
}

package com.skgtecnologia.sisem.domain.myscreen.model

import com.valkiria.uicomponents.components.label.RichLabelUiModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class RichLabelModel(
    val text: String,
    val style: LabelStyle,
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = BodyRowType.RICH_LABEL
}

fun RichLabelModel.mapToUiModel() = RichLabelUiModel(
    text = text,
    style = style.mapToUiModel(),
    margins = margins?.mapToUiModel()
)

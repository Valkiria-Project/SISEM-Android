package com.skgtecnologia.sisem.domain.myscreen.model

import com.valkiria.uicomponents.components.label.LabelUiModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LabelModel(
    val text: String,
    val style: LabelStyle,
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = BodyRowType.LABEL
}

fun LabelModel.mapToUiModel() = LabelUiModel(
    text = text,
    style = style.mapToUiModel(),
    margins = margins?.mapToUiModel()
)

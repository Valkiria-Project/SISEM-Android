package com.skgtecnologia.sisem.domain.myscreen.model

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class RichLabelModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = BodyRowType.RICH_LABEL
}

// fun RichLabelModel.mapToUiModel() = RichLabelUiModel(
//    text = text,
//    style = textStyle.mapToUiModel(),
//    margins = margins?.mapToUiModel()
// )

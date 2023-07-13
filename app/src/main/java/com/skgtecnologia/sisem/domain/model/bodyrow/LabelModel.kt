package com.skgtecnologia.sisem.domain.model.bodyrow

import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.RICH_LABEL
import com.skgtecnologia.sisem.domain.model.props.MarginsModel
import com.skgtecnologia.sisem.domain.model.props.TextStyle
import com.skgtecnologia.sisem.domain.model.props.mapToUiModel
import com.valkiria.uicomponents.components.label.LabelUiModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LabelModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = RICH_LABEL
}

fun LabelModel.mapToUiModel() = LabelUiModel(
    text = text,
    textStyle = textStyle.mapToUiModel(),
    margins = margins?.mapToUiModel()
)

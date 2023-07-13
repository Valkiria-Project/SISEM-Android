package com.skgtecnologia.sisem.domain.model.bodyrow

import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.RICH_LABEL
import com.skgtecnologia.sisem.domain.model.props.MarginsModel
import com.skgtecnologia.sisem.domain.model.props.TextStyle
import com.skgtecnologia.sisem.domain.model.props.mapToUiModel
import com.valkiria.uicomponents.components.label.LabeledSwitchUiModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LabeledSwitchModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = RICH_LABEL
}

fun LabeledSwitchModel.mapToUiModel() = LabeledSwitchUiModel(
    text = text,
    style = textStyle.mapToUiModel(),
    margins = margins?.mapToUiModel()
)

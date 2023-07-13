package com.skgtecnologia.sisem.domain.model.bodyrow

import com.skgtecnologia.sisem.domain.model.props.MarginsModel
import com.skgtecnologia.sisem.domain.model.props.TextStyle
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.RICH_LABEL
import com.skgtecnologia.sisem.domain.model.props.mapToUiModel
import com.valkiria.uicomponents.props.label.RichLabelUiModel
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
    override val type: BodyRowType = RICH_LABEL
}

 fun RichLabelModel.mapToUiModel() = RichLabelUiModel(
    text = text,
    textStyle = textStyle.mapToUiModel(),
    margins = margins?.mapToUiModel()
 )

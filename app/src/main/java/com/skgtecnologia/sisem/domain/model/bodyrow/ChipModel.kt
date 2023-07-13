package com.skgtecnologia.sisem.domain.model.bodyrow

import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.CHIP
import com.skgtecnologia.sisem.domain.model.props.ChipStyle
import com.skgtecnologia.sisem.domain.model.props.MarginsModel
import com.skgtecnologia.sisem.domain.model.props.mapToUiModel
import com.valkiria.uicomponents.props.MarginsUiModel
import com.valkiria.uicomponents.props.chip.ChipUiModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChipModel(
    val icon: String?,
    val text: String?,
    val style: ChipStyle,
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = CHIP
}

fun ChipModel.mapToUiModel() = ChipUiModel(
    icon = icon,
    text = text,
    style = style.mapToUiModel(),
    margins = margins?.mapToUiModel()
)

package com.skgtecnologia.sisem.domain.core.model.bodyrow

import com.skgtecnologia.sisem.domain.myscreen.model.ChipStyle
import com.skgtecnologia.sisem.domain.myscreen.model.MarginsModel
import com.skgtecnologia.sisem.domain.core.model.bodyrow.mapToUiModel
import com.valkiria.uicomponents.components.chip.ChipUiModel
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
    override val type: BodyRowType = BodyRowType.CHIP
}

//fun ChipModel.mapToUiModel() = ChipUiModel(
//    icon = icon,
//    text = text,
//    style = style.mapToUiModel(),
//    margins = margins?.mapToUiModel()
//)

package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.chip.ChipOptionUiModel
import com.valkiria.uicomponents.model.ui.chip.ChipOptionsUiModel

data class ChipOptionsModel(
    val identifier: String,
    val title: TextModel,
    val items: List<ChipOptionUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CHIP_OPTIONS
}

fun ChipOptionsModel.mapToUiModel() = ChipOptionsUiModel(
    identifier = identifier,
    title = title.text,
    textStyle = title.textStyle,
    items = items,
    modifier = modifier
)

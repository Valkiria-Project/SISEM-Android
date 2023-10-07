package com.valkiria.uicomponents.model.ui.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextModel
import com.valkiria.uicomponents.model.ui.chip.ChipOptionUiModel

data class ChipOptionsUiModel(
    val identifier: String,
    val title: TextModel? = null,
    val items: List<ChipOptionUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CHIP_OPTIONS
}


package com.valkiria.uicomponents.model.ui.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextModel
import com.valkiria.uicomponents.model.ui.chip.ChipSelectionItemUiModel

data class ChipSelectionUiModel(
    val identifier: String,
    val title: TextModel?,
    val items: List<ChipSelectionItemUiModel>,
    val selected: String?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CHIP_SELECTION
}

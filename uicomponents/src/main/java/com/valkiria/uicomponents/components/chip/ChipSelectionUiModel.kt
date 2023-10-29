package com.valkiria.uicomponents.components.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextUiModel

data class ChipSelectionUiModel(
    override val identifier: String,
    val title: TextUiModel?,
    val items: List<ChipSelectionItemUiModel>,
    val selected: String? = null,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CHIP_SELECTION
}

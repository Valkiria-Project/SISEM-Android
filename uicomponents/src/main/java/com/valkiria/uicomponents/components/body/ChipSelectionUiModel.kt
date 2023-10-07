package com.valkiria.uicomponents.components.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.model.props.TextModel
import com.valkiria.uicomponents.model.ui.chip.ChipSelectionItemUiModel

data class ChipSelectionUiModel(
    val identifier: String,
    val title: TextModel?,
    val items: List<ChipSelectionItemUiModel>,
    val selected: String? = null,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : com.valkiria.uicomponents.components.body.BodyRowModel {

    override val type: BodyRowType = BodyRowType.CHIP_SELECTION
}

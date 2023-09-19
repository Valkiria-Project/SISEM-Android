package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.props.TextModel
import com.valkiria.uicomponents.model.ui.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.model.ui.chip.ChipSelectionUiModel

data class ChipSelectionModel(
    val identifier: String,
    val title: TextModel? = null,
    val items: List<ChipSelectionItemUiModel>,
    val selected: String? = null,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CHIP_SELECTION
}

fun ChipSelectionModel.mapToUiModel(): ChipSelectionUiModel = ChipSelectionUiModel(
    identifier = identifier,
    titleText = title?.text,
    titleTextStyle = title?.textStyle,
    items = items,
    selected = selected,
    arrangement = arrangement,
    modifier = modifier
)

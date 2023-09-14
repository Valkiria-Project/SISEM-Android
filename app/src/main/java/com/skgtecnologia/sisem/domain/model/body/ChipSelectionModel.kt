package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.props.TextModel
import com.valkiria.uicomponents.model.ui.chip.ChipSectionItemUiModel

data class ChipSelectionModel(
    val identifier: String,
    val title: TextModel,
    val items: List<ChipSectionItemUiModel>,
    val selected: String? = null,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CHIP_SELECTION
}

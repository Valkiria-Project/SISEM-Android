package com.valkiria.uicomponents.model.ui.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.dropdown.DropDownItemUiModel

// FIXME: Unused, should be removed
data class DropDownUiModel(
    val identifier: String,
    val label: String,
    val options: List<DropDownItemUiModel>,
    val selected: String?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.DROP_DOWN
}

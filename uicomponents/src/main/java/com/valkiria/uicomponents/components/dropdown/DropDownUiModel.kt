package com.valkiria.uicomponents.components.dropdown

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.header.HeaderUiModel

data class DropDownUiModel(
    override val identifier: String,
    val label: String,
    val items: List<DropDownItemUiModel>,
    val selected: String = "",
    val header: HeaderUiModel,
    val section: String?,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.DROP_DOWN
}

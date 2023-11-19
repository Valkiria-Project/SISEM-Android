package com.valkiria.uicomponents.components.chip

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextStyle

data class FiltersUiModel(
    override val identifier: String,
    val options: List<String>,
    val textStyle: TextStyle,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FILTERS
}

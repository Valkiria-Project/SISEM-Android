package com.valkiria.uicomponents.model.ui.body

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

data class FiltersUiModel(
    val identifier: String,
    val options: List<String>,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FILTERS
}

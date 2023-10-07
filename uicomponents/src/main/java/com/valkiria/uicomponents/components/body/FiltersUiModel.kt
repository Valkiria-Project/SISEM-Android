package com.valkiria.uicomponents.components.body

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.model.props.TextStyle

data class FiltersUiModel(
    val identifier: String,
    val options: List<String>,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
) : com.valkiria.uicomponents.components.body.BodyRowModel {

    override val type: BodyRowType = BodyRowType.FILTERS
}

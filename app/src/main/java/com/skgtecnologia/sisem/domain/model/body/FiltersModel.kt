package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.filters.FiltersUiModel
import com.valkiria.uicomponents.model.props.TextStyle

data class FiltersModel(
    val identifier: String,
    val options: List<String>,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FILTERS
}

fun FiltersModel.mapToUiModel() = FiltersUiModel(
    options = options,
    textStyle = textStyle,
    modifier = modifier
)

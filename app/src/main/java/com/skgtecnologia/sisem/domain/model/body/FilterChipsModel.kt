package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType.FILTER_CHIPS
import com.valkiria.uicomponents.components.filterchips.FilterChipsUiModel
import com.valkiria.uicomponents.props.TextStyle

data class FilterChipsModel(
    val identifier: String,
    val chips: List<String>,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = FILTER_CHIPS
}

fun FilterChipsModel.mapToUiModel() = FilterChipsUiModel(
    chips = chips,
    textStyle = textStyle,
    modifier = modifier
)

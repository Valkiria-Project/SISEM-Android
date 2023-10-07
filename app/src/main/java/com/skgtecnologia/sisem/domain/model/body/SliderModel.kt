package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.body.BodyRowType
import com.valkiria.uicomponents.model.ui.slider.SliderUiModel

data class SliderModel(
    val identifier: String,
    val min: Int,
    val max: Int,
    val selected: Int = 0,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.SLIDER
}

fun SliderModel.mapToUiModel(): SliderUiModel = SliderUiModel(
    identifier = identifier,
    min = min,
    max = max,
    selected = selected,
    arrangement = arrangement,
    modifier = modifier
)

package com.valkiria.uicomponents.components.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowType

data class SliderUiModel(
    val identifier: String,
    val min: Int,
    val max: Int,
    val selected: Int = 0,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : com.valkiria.uicomponents.components.body.BodyRowModel {

    override val type: BodyRowType = BodyRowType.SLIDER
}

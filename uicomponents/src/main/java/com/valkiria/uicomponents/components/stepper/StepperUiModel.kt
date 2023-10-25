package com.valkiria.uicomponents.components.stepper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

data class StepperUiModel(
    val identifier: String,
    val options: Map<String, String>,
    val selected: String = "0",
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.STEPPER
}

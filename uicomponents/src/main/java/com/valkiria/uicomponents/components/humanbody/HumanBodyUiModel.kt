package com.valkiria.uicomponents.components.humanbody

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.header.HeaderUiModel

data class HumanBodyUiModel(
    val identifier: String,
    val header: HeaderUiModel,
    val wounds: List<String>,
    val burningLevel: List<String>,
    val section: String?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.HUMAN_BODY
}
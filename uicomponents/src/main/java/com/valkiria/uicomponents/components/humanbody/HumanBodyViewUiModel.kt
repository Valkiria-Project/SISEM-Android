package com.valkiria.uicomponents.components.humanbody

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

data class HumanBodyViewUiModel(
    override val identifier: String,
    val wounds: List<HumanBodyUi>? = null,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val section: String?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.HUMAN_BODY_VIEW
}

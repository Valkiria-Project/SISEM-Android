package com.valkiria.uicomponents.components.media

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.BodyRowType.MEDIA_ACTIONS

data class MediaActionsUiModel(
    override val identifier: String = "MEDIA_ACTIONS",
    val withinForm: Boolean = false,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = MEDIA_ACTIONS
}

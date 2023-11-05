package com.valkiria.uicomponents.components.media

import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.BodyRowType.MEDIA_ACTIONS

data class MediaActionsUiModel(
    override val identifier: String = "MEDIA_ACTIONS",
    val hasCameraAction: Boolean = true,
    val hasGalleryAction: Boolean = true,
    val hasFileAction: Boolean = false
) : BodyRowModel {

    override val type: BodyRowType = MEDIA_ACTIONS
}

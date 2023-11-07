package com.valkiria.uicomponents.components.media

import android.net.Uri
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.BodyRowType.MEDIA_ACTIONS
import java.io.File

data class MediaActionsUiModel(
    override val identifier: String = "MEDIA_ACTIONS",
    val withinForm: Boolean = false,
    val selectedMediaUris: List<Uri> = listOf(),
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = MEDIA_ACTIONS
    var selectedMediaFiles: List<File>? = null
}

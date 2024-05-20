package com.valkiria.uicomponents.components.media

import java.io.File

data class MediaItemUiModel(
    val uri: String,
    val file: File? = null,
    val name: String,
    val isSizeValid: Boolean
)

fun List<String>.mapToMediaItems(): List<MediaItemUiModel> = this.map {
    MediaItemUiModel(
        uri = it,
        file = null,
        name = it,
        isSizeValid = true
    )
}

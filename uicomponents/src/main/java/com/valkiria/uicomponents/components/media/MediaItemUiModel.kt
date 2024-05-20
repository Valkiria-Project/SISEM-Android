package com.valkiria.uicomponents.components.media

import java.io.File

data class MediaItemUiModel(
    val uri: String,
    val file: File,
    val name: String,
    val isSizeValid: Boolean
)

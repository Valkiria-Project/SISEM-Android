package com.valkiria.uicomponents.model.ui.banner

import com.valkiria.uicomponents.components.body.ButtonUiModel

const val DEFAULT_ICON_COLOR = "#F55757" // FIXME: update with backend

data class BannerUiModel(
    val icon: String? = null,
    val iconColor: String = DEFAULT_ICON_COLOR,
    val title: String,
    val description: String,
    val leftButton: com.valkiria.uicomponents.components.body.ButtonUiModel? = null,
    val rightButton: com.valkiria.uicomponents.components.body.ButtonUiModel? = null
)

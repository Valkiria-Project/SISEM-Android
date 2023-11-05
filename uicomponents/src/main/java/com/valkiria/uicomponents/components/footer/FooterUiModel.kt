package com.valkiria.uicomponents.components.footer

import com.valkiria.uicomponents.components.button.ButtonUiModel

data class FooterUiModel(
    val leftButton: ButtonUiModel,
    val rightButton: ButtonUiModel? = null
)

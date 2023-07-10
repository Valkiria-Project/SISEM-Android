package com.skgtecnologia.sisem.domain.myscreen.model

enum class ButtonStyle {
    LOUD,
    TRANSPARENT;

    fun mapToUiModel(): com.valkiria.uicomponents.components.button.ButtonStyle {
        return when (this) {
            LOUD -> com.valkiria.uicomponents.components.button.ButtonStyle.LOUD
            TRANSPARENT -> com.valkiria.uicomponents.components.button.ButtonStyle.TRANSPARENT
        }
    }
}

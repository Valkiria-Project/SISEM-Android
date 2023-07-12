package com.skgtecnologia.sisem.domain.myscreen.model

enum class ButtonSize {
    DEFAULT,
    FULL_WIDTH;

    fun mapToUiModel(): com.valkiria.uicomponents.components.button.ButtonSize {
        return when (this) {
            DEFAULT -> com.valkiria.uicomponents.components.button.ButtonSize.DEFAULT
            FULL_WIDTH -> com.valkiria.uicomponents.components.button.ButtonSize.FULL_WIDTH
        }
    }
}

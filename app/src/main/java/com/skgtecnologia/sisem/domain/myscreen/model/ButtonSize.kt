package com.skgtecnologia.sisem.domain.myscreen.model

enum class ButtonSize {
    default, // TODO DEFAULT
    fullWidth; // TODO FULL_WIDTH

    fun mapToUiModel(): com.valkiria.uicomponents.components.button.ButtonSize {
        return when (this) {
            default -> com.valkiria.uicomponents.components.button.ButtonSize.DEFAULT
            fullWidth -> com.valkiria.uicomponents.components.button.ButtonSize.FULL_WIDTH
        }
    }
}

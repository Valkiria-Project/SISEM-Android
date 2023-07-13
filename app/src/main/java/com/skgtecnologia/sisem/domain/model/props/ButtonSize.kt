package com.skgtecnologia.sisem.domain.model.props

enum class ButtonSize {
    DEFAULT,
    FULL_WIDTH;

    // FIXME
    fun mapToUiModel(): com.valkiria.uicomponents.components.button.ButtonSize = when (this) {
        DEFAULT -> com.valkiria.uicomponents.components.button.ButtonSize.DEFAULT
        FULL_WIDTH -> com.valkiria.uicomponents.components.button.ButtonSize.FULL_WIDTH
    }
}

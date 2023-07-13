package com.skgtecnologia.sisem.domain.model.props

enum class ButtonSize {
    DEFAULT,
    FULL_WIDTH;

    // FIXME
    fun mapToUiModel(): com.valkiria.uicomponents.props.button.ButtonSize = when (this) {
        DEFAULT -> com.valkiria.uicomponents.props.button.ButtonSize.DEFAULT
        FULL_WIDTH -> com.valkiria.uicomponents.props.button.ButtonSize.FULL_WIDTH
    }
}

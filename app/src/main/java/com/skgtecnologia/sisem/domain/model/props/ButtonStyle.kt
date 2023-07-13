package com.skgtecnologia.sisem.domain.model.props

enum class ButtonStyle {
    LOUD,
    QUIET,
    TRANSPARENT;

    // FIXME
    fun mapToUiModel(): com.valkiria.uicomponents.components.button.ButtonStyle = when (this) {
        LOUD -> com.valkiria.uicomponents.components.button.ButtonStyle.LOUD
        QUIET -> com.valkiria.uicomponents.components.button.ButtonStyle.LOUD
        TRANSPARENT -> com.valkiria.uicomponents.components.button.ButtonStyle.TRANSPARENT
    }
}

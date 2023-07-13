package com.skgtecnologia.sisem.domain.model.props

enum class ButtonStyle {
    LOUD,
    QUIET,
    TRANSPARENT;

    // FIXME
    fun mapToUiModel(): com.valkiria.uicomponents.props.button.ButtonStyle = when (this) {
        LOUD -> com.valkiria.uicomponents.props.button.ButtonStyle.LOUD
        QUIET -> com.valkiria.uicomponents.props.button.ButtonStyle.LOUD
        TRANSPARENT -> com.valkiria.uicomponents.props.button.ButtonStyle.TRANSPARENT
    }
}

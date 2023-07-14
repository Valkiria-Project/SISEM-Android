package com.skgtecnologia.sisem.domain.model.props

import com.valkiria.uicomponents.props.ButtonStyle

enum class ButtonStyle {
    LOUD,
    QUIET,
    TRANSPARENT;

    // FIXME
    fun mapToUiModel(): ButtonStyle = when (this) {
        LOUD -> ButtonStyle.LOUD
        QUIET -> ButtonStyle.LOUD
        TRANSPARENT -> ButtonStyle.TRANSPARENT
    }
}

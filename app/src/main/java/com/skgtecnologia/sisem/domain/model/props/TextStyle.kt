package com.skgtecnologia.sisem.domain.model.props

import com.valkiria.uicomponents.props.TextStyle

enum class TextStyle {
    BODY_1,
    BUTTON_1,
    BUTTON_2,
    HEADLINE_1,
    HEADLINE_2,
    HEADLINE_3,
    HEADLINE_4,
    HEADLINE_5,
    HEADLINE_6,
    HEADLINE_7,
    HEADLINE_8;

    // FIXME
    fun mapToUiModel() = when (this) {
        BODY_1 -> TextStyle.BODY_1
        BUTTON_1 -> TextStyle.BUTTON_1
        BUTTON_2 -> TextStyle.BUTTON_2
        HEADLINE_1 -> TextStyle.HEADLINE_1
        HEADLINE_2 -> TextStyle.HEADLINE_2
        HEADLINE_3 -> TextStyle.HEADLINE_3
        HEADLINE_4 -> TextStyle.HEADLINE_4
        HEADLINE_5 -> TextStyle.HEADLINE_5
        HEADLINE_6 -> TextStyle.HEADLINE_6
        HEADLINE_7 -> TextStyle.HEADLINE_7
        HEADLINE_8 -> TextStyle.HEADLINE_8
    }
}

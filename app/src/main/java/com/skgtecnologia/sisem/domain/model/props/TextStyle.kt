package com.skgtecnologia.sisem.domain.model.props

import com.valkiria.uicomponents.props.TextStyleUi

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
        BODY_1 -> TextStyleUi.BODY_1
        BUTTON_1 -> TextStyleUi.BUTTON_1
        BUTTON_2 -> TextStyleUi.BUTTON_2
        HEADLINE_1 -> TextStyleUi.HEADLINE_1
        HEADLINE_2 -> TextStyleUi.HEADLINE_2
        HEADLINE_3 -> TextStyleUi.HEADLINE_3
        HEADLINE_4 -> TextStyleUi.HEADLINE_4
        HEADLINE_5 -> TextStyleUi.HEADLINE_5
        HEADLINE_6 -> TextStyleUi.HEADLINE_6
        HEADLINE_7 -> TextStyleUi.HEADLINE_7
        HEADLINE_8 -> TextStyleUi.HEADLINE_8
    }
}

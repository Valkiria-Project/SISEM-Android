package com.skgtecnologia.sisem.domain.model.props

import com.valkiria.uicomponents.props.label.LabelStyle

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
        BODY_1 -> LabelStyle.HEADING
        BUTTON_1 -> LabelStyle.PRIMARY
        BUTTON_2 -> LabelStyle.PRIMARY
        HEADLINE_1 -> LabelStyle.PRIMARY
        HEADLINE_2 -> LabelStyle.PRIMARY
        HEADLINE_3 -> LabelStyle.PRIMARY
        HEADLINE_4 -> LabelStyle.PRIMARY
        HEADLINE_5 -> LabelStyle.PRIMARY
        HEADLINE_6 -> LabelStyle.PRIMARY
        HEADLINE_7 -> LabelStyle.PRIMARY
        HEADLINE_8 -> LabelStyle.PRIMARY
    }
}

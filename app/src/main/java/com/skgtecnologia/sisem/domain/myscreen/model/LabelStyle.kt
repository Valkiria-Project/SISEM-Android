package com.skgtecnologia.sisem.domain.myscreen.model

enum class LabelStyle {
    HEADING,
    PRIMARY;

    fun mapToUiModel() = when (this) {
        HEADING -> com.valkiria.uicomponents.components.label.LabelStyle.HEADING
        PRIMARY -> com.valkiria.uicomponents.components.label.LabelStyle.PRIMARY
    }
}
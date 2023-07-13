package com.skgtecnologia.sisem.domain.model.props

enum class LabelStyle {
    HEADING,
    PRIMARY;

    // FIXME
    fun mapToUiModel() = when (this) {
        HEADING -> com.valkiria.uicomponents.components.label.LabelStyle.HEADING
        PRIMARY -> com.valkiria.uicomponents.components.label.LabelStyle.PRIMARY
    }
}

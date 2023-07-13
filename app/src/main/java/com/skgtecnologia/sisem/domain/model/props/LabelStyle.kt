package com.skgtecnologia.sisem.domain.model.props

enum class LabelStyle {
    HEADING,
    PRIMARY;

    // FIXME
    fun mapToUiModel() = when (this) {
        HEADING -> com.valkiria.uicomponents.props.label.LabelStyle.HEADING
        PRIMARY -> com.valkiria.uicomponents.props.label.LabelStyle.PRIMARY
    }
}

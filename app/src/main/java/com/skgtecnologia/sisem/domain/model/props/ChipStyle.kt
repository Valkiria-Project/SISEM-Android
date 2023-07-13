package com.skgtecnologia.sisem.domain.model.props

enum class ChipStyle {
    PRIMARY,
    SECONDARY;

    // FIXME
    fun mapToUiModel() = when (this) {
        PRIMARY -> com.valkiria.uicomponents.props.chip.ChipStyle.PRIMARY
        SECONDARY -> com.valkiria.uicomponents.props.chip.ChipStyle.SECONDARY
    }
}

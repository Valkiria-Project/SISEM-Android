package com.skgtecnologia.sisem.domain.core.model.props

enum class ChipStyle {
    PRIMARY,
    SECONDARY;

    fun mapToUiModel() = when (this) {
        PRIMARY -> com.valkiria.uicomponents.components.chip.ChipStyle.PRIMARY
        SECONDARY -> com.valkiria.uicomponents.components.chip.ChipStyle.SECONDARY
    }
}

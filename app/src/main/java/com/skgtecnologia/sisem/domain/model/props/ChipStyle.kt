package com.skgtecnologia.sisem.domain.model.props

import com.valkiria.uicomponents.props.ChipStyle

enum class ChipStyle {
    PRIMARY,
    SECONDARY;

    // FIXME
    fun mapToUiModel() = when (this) {
        PRIMARY -> ChipStyle.PRIMARY
        SECONDARY -> ChipStyle.SECONDARY
    }
}

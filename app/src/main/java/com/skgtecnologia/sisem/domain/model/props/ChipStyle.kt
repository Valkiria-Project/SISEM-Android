package com.skgtecnologia.sisem.domain.model.props

import com.valkiria.uicomponents.props.ChipStyleUi

enum class ChipStyle {
    PRIMARY,
    SECONDARY;

    // FIXME
    fun mapToUiModel() = when (this) {
        PRIMARY -> ChipStyleUi.PRIMARY
        SECONDARY -> ChipStyleUi.SECONDARY
    }
}

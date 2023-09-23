package com.skgtecnologia.sisem.ui.humanbody

import com.skgtecnologia.sisem.ui.humanbody.area.BackArea
import com.skgtecnologia.sisem.ui.humanbody.area.FrontArea

data class HumanBodyUiState(
    val selectedFrontAreas: List<FrontArea> = emptyList(),
    val selectedBackAreas: List<BackArea> = emptyList(),
    val onSelectWound: Boolean = false
)

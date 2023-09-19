package com.skgtecnologia.sisem.ui.humanbody

data class HumanBodyUiState(
    val selectedFrontAreas: List<FrontArea> = emptyList(),
    val selectedBackAreas: List<BackArea> = emptyList(),
    val onSelectWound: Boolean = false
)

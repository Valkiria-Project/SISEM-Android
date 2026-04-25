package com.skgtecnologia.sisem.ui.humanbody.wounds

data class WoundsUiState(
    val selectedWounds: List<String> = emptyList(),
    val onBurnSelected: Boolean = false
)

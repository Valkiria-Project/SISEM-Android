package com.skgtecnologia.sisem.ui.humanbody.bottomsheet

data class WoundsUiState(
    val selectedWounds: List<String> = emptyList(),
    val onBurnSelected: Boolean = false
)

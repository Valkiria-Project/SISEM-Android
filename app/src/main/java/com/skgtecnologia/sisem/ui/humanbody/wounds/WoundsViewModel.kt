package com.skgtecnologia.sisem.ui.humanbody.wounds

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val BURN_WOUND = "Quemadura"

@HiltViewModel
class WoundsViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(WoundsUiState())
        private set

    private val burnList = mutableListOf<String>()
    private var isBurnSelected = false

    fun setBurnList(list: List<String>) {
        burnList.clear()
        burnList.addAll(list)
    }

    fun updateWoundsList(selectedWound: String, isSelected: Boolean) {
        var selectWound = false

        val list = buildList {
            addAll(uiState.selectedWounds)

            if (contains(selectedWound)) {
                remove(selectedWound)
            } else {
                selectWound = when {
                    selectedWound == BURN_WOUND && isSelected -> {
                        isBurnSelected = true
                        true
                    }

                    selectedWound == BURN_WOUND && !isSelected -> {
                        isBurnSelected = false
                        removeAll(burnList)
                        false
                    }

                    else -> {
                        add(selectedWound)
                        false
                    }
                }
            }
        }

        uiState = uiState.copy(
            selectedWounds = list,
            onBurnSelected = selectWound || isBurnSelected
        )
    }

    fun updateBurnList(selectedBurn: String) {
        val list = buildList {
            addAll(uiState.selectedWounds)
            removeAll(burnList)
            add(selectedBurn)
        }

        uiState = uiState.copy(
            selectedWounds = list
        )
    }

    fun onSelectedWoundsHandled() {
        isBurnSelected = false

        uiState = uiState.copy(
            selectedWounds = emptyList(),
            onBurnSelected = false
        )
    }
}

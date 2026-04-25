package com.skgtecnologia.sisem.ui.humanbody

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.skgtecnologia.sisem.ui.humanbody.area.BackArea
import com.skgtecnologia.sisem.ui.humanbody.area.FrontArea
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HumanBodyViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(HumanBodyUiState())
        private set

    fun updateFrontList(selectedArea: FrontArea) {
        var selectWound = false
        val frontAreas = buildList {
            addAll(uiState.selectedFrontAreas)

            if (contains(selectedArea)) {
                remove(selectedArea)
            } else {
                selectWound = true
            }
        }
        uiState = uiState.copy(
            selectedFrontAreas = frontAreas,
            onSelectWound = selectWound
        )
    }

    fun saveFrontList(selectedArea: FrontArea) {
        val frontAreas = buildList {
            addAll(uiState.selectedFrontAreas)
            add(selectedArea)
        }

        uiState = uiState.copy(selectedFrontAreas = frontAreas)
    }

    fun consumeWoundSelectedEvent() {
        uiState = uiState.copy(onSelectWound = false)
    }

    fun updateBackList(selectedArea: BackArea) {
        var selectWound = false
        val backAreas = buildList {
            addAll(uiState.selectedBackAreas)

            if (contains(selectedArea)) {
                remove(selectedArea)
            } else {
                selectWound = true
            }
        }
        uiState = uiState.copy(
            selectedBackAreas = backAreas,
            onSelectWound = selectWound
        )
    }

    fun saveBackList(selectedArea: BackArea) {
        val backAreas = buildList {
            addAll(uiState.selectedBackAreas)
            add(selectedArea)
        }

        uiState = uiState.copy(selectedBackAreas = backAreas)
    }
}

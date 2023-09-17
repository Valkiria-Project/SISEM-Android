package com.skgtecnologia.sisem.ui

import androidx.compose.runtime.Composable
import com.valkiria.uicomponents.components.humanbody.HumanBodyComponent

@Composable
fun InventoryScreen() {

    val selectedAreas = mutableListOf<Map<String, List<String>>>()
    HumanBodyComponent { identifier, selectedArea ->
        if (selectedAreas.contains(selectedArea)) {
            selectedAreas.remove(selectedArea)
        } else {
            selectedAreas.add(selectedArea)
        }
    }

}

package com.skgtecnologia.sisem.ui

import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.skgtecnologia.sisem.ui.bottomsheet.WoundsContent
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import com.valkiria.uicomponents.components.humanbody.Area
import com.valkiria.uicomponents.components.humanbody.HumanBodyComponent

@Composable
fun InventoryScreen() {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    //var showBottomSheet by remember { mutableStateOf(false) }

    val selectedAreas = mutableListOf<Area>()
    HumanBodyComponent { selectedArea ->
        if (selectedAreas.contains(selectedArea)) {
            selectedAreas.remove(selectedArea)
        } else {
            selectedAreas.add(selectedArea)
            //showBottomSheet = true
        }

    }

    /*if (showBottomSheet) {
        BottomSheetComponent(
            sheetState = sheetState,
            scope = scope,
            content = {
                WoundsContent() {}
            }
        ) {}
    }*/

}

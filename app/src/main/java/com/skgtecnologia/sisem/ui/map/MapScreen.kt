package com.skgtecnologia.sisem.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.map.MapComponent

@Composable
fun MapScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MapComponent(
            modifier = Modifier.fillMaxSize(),
            coordinates = -75.5657751 to 6.2082622
        )
    }
}

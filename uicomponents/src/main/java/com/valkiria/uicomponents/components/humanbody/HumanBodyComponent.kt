package com.valkiria.uicomponents.components.humanbody

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import kotlinx.coroutines.launch

private const val DEFAULT_DISPLAY_WIDTH = 1440
private const val DEFAULT_DISPLAY_HEIGHT = 2900

@Composable
fun HumanBodyComponent(
    onClick: (String, Map<String, List<String>>) -> Unit
) {
    val selectedAreas by remember { mutableStateOf(mutableListOf<Area>()) }
    var selectedArea by remember { mutableStateOf(Area.NONE) }
    var showBottomSheet: Boolean? by remember { mutableStateOf(null) }

    val width = LocalContext.current.display?.width ?: DEFAULT_DISPLAY_WIDTH
    val height = LocalContext.current.display?.height ?: DEFAULT_DISPLAY_HEIGHT

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    selectedArea = Area.fromPosition(it.x, it.y, width, height)
                    if (selectedArea == Area.NONE) return@detectTapGestures

                    if (selectedAreas.contains(selectedArea)) {
                        selectedAreas.remove(selectedArea)
                        showBottomSheet = null
                    } else {
                        showBottomSheet = true
                        scope.launch { sheetState.show() }
                    }
                }
            }
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            painter = painterResource(id = R.drawable.ic_human_body_background),
            contentDescription = null
        )

        if (showBottomSheet == true) {
            BottomSheetComponent(
                content = {
                    WoundsContent { wounds ->
                        selectedAreas.add(selectedArea)
                        selectedArea = Area.NONE
                        showBottomSheet = false
                        onClick("identifier", mapOf(selectedArea.name to wounds))
                    }
                },
                sheetState = sheetState,
                scope = scope,
            ) {
                showBottomSheet = null
            }
        }

        selectedAreas.forEach {
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                painter = painterResource(id = it.image),
                contentDescription = null
            )
        }
    }
}

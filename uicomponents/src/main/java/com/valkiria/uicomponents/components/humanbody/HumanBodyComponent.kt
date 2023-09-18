package com.valkiria.uicomponents.components.humanbody

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import kotlinx.coroutines.launch

@Composable
fun HumanBodyComponent(
    onClick: (String, Map<String, List<String>>) -> Unit
) {
    val width = LocalContext.current.display?.width ?: BASE_WIDTH
    val height = LocalContext.current.display?.height ?: BASE_HEIGHT

    Box(modifier = Modifier.fillMaxSize()) {
        var isFront by remember { mutableStateOf(true) }

        if (isFront) {
            HumanBodyFrontComponent(width, height) { identifier, wounds ->
                onClick(identifier, wounds)
            }
        } else {
            HumanBodyBackComponent(width, height) { identifier, wounds ->
                onClick(identifier, wounds)
            }
        }

        Icon(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
                .clickable { isFront = !isFront }
                .size(32.dp),
            painter = painterResource(id = R.drawable.ic_change),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun HumanBodyFrontComponent(
    width: Int,
    height: Int,
    onClick: (String, Map<String, List<String>>) -> Unit
) {
    val selectedFrontAreas by rememberSaveable { mutableStateOf(mutableListOf<FrontArea>()) }
    var selectedFrontArea by remember { mutableStateOf(FrontArea.NONE) }
    var showBottomSheet: Boolean? by remember { mutableStateOf(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    selectedFrontArea = FrontArea.fromPosition(it.x, it.y, width, height)
                    if (selectedFrontArea == FrontArea.NONE) return@detectTapGestures

                    if (selectedFrontAreas.contains(selectedFrontArea)) {
                        selectedFrontAreas.remove(selectedFrontArea)
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
            painter = painterResource(id = R.drawable.ic_front_human_body_background),
            contentDescription = null
        )

        if (showBottomSheet == true) {
            BottomSheetComponent(
                content = {
                    WoundsContent { wounds ->
                        selectedFrontAreas.add(selectedFrontArea)
                        onClick("identifier", mapOf(selectedFrontArea.name to wounds))
                        selectedFrontArea = FrontArea.NONE
                        showBottomSheet = false
                    }
                },
                sheetState = sheetState,
                scope = scope,
            ) {
                showBottomSheet = null
            }
        }

        selectedFrontAreas.forEach {
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

@Composable
private fun HumanBodyBackComponent(
    width: Int,
    height: Int,
    onClick: (String, Map<String, List<String>>) -> Unit
) {
    val selectedBackAreas by remember { mutableStateOf(mutableListOf<BackArea>()) }
    var selectedBackArea by remember { mutableStateOf(BackArea.NONE) }
    var showBottomSheet: Boolean? by remember { mutableStateOf(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    selectedBackArea = BackArea.fromPosition(it.x, it.y, width, height)
                    if (selectedBackArea == BackArea.NONE) return@detectTapGestures

                    if (selectedBackAreas.contains(selectedBackArea)) {
                        selectedBackAreas.remove(selectedBackArea)
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
            painter = painterResource(id = R.drawable.ic_back_human_body_background),
            contentDescription = null
        )

        if (showBottomSheet == true) {
            BottomSheetComponent(
                content = {
                    WoundsContent { wounds ->
                        selectedBackAreas.add(selectedBackArea)
                        onClick("identifier", mapOf(selectedBackArea.name to wounds))
                        selectedBackArea = BackArea.NONE
                        showBottomSheet = false
                    }
                },
                sheetState = sheetState,
                scope = scope,
            ) {
                showBottomSheet = null
            }
        }

        selectedBackAreas.forEach {
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

package com.skgtecnologia.sisem.ui.humanbody

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.ui.humanbody.area.BackArea
import com.skgtecnologia.sisem.ui.humanbody.wounds.WoundsContent
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import kotlinx.coroutines.launch

@Suppress("LongMethod")
@Composable
fun HumanBodyBackComponent(
    viewModel: HumanBodyViewModel,
    width: Int,
    height: Int,
    onAction: (id: String, wounds: Map<String, List<String>>) -> Unit
) {
    val uiState = viewModel.uiState
    val selectedBackAreas = uiState.selectedBackAreas
    var selectedBackArea by remember { mutableStateOf(BackArea.NONE) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.onSelectWound -> {
                    scope.launch { sheetState.show() }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    selectedBackArea = BackArea.fromPosition(it.x, it.y, width, height)
                    if (selectedBackArea == BackArea.NONE) return@detectTapGestures

                    viewModel.updateBackList(selectedBackArea)
                }
            }
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            painter = painterResource(id = R.drawable.img_back_human_body),
            contentDescription = null
        )

        if (uiState.onSelectWound) {
            BottomSheetComponent(
                content = {
                    WoundsContent { wounds ->
                        viewModel.saveBackList(selectedBackArea)
                        onAction("identifier", mapOf(selectedBackArea.name to wounds))
                        selectedBackArea = BackArea.NONE
                        viewModel.handledOnWoundSelected()
                    }
                },
                sheetState = sheetState,
                scope = scope,
            ) {
                viewModel.handledOnWoundSelected()
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
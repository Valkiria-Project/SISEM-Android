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
import com.skgtecnologia.sisem.ui.humanbody.area.FrontArea
import com.skgtecnologia.sisem.ui.humanbody.wounds.WoundsContent
import com.valkiria.uicomponents.bricks.bottomsheet.BottomSheetView
import com.valkiria.uicomponents.components.humanbody.HumanBodyType
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import com.valkiria.uicomponents.components.humanbody.HumanBodyUiModel
import kotlinx.coroutines.launch

@Suppress("LongMethod")
@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun HumanBodyFrontComponent(
    uiModel: HumanBodyUiModel,
    viewModel: HumanBodyViewModel,
    width: Int,
    height: Int,
    onAction: (humanBodyUi: HumanBodyUi) -> Unit
) {
    val uiState = viewModel.uiState
    val selectedFrontAreas = uiState.selectedFrontAreas
    var selectedFrontArea by remember { mutableStateOf(FrontArea.NONE) }

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
                    selectedFrontArea = FrontArea.fromPosition(it.x, it.y, width, height)
                    if (selectedFrontArea == FrontArea.NONE) return@detectTapGestures

                    viewModel.updateFrontList(selectedFrontArea)
                }
            }
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            painter = painterResource(id = R.drawable.img_front_human_body),
            contentDescription = null
        )

        if (uiState.onSelectWound) {
            BottomSheetView(
                content = {
                    WoundsContent(
                        header = uiModel.header,
                        wounds = uiModel.wounds,
                        burningLevel = uiModel.burningLevel
                    ) { wounds ->
                        viewModel.saveFrontList(selectedFrontArea)
                        onAction(
                            HumanBodyUi(
                                type = HumanBodyType.FRONT.name,
                                area = selectedFrontArea.name,
                                areaName = selectedFrontArea.name,
                                wounds = wounds
                            )
                        )
                        selectedFrontArea = FrontArea.NONE
                        viewModel.consumeWoundSelectedEvent()
                    }
                },
                sheetState = sheetState,
                scope = scope,
            ) {
                viewModel.consumeWoundSelectedEvent()
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

package com.skgtecnologia.sisem.ui.humanbody

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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.humanbody.bottomsheet.WoundsContent
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import kotlinx.coroutines.launch

@Composable
fun HumanBodyComponent(
    onClick: (String, Map<String, List<String>>) -> Unit
) {
    val viewModel = hiltViewModel<HumanBodyViewModel>()
    val width = LocalContext.current.display?.width ?: BASE_WIDTH
    val height = LocalContext.current.display?.height ?: BASE_HEIGHT

    Box(modifier = Modifier.fillMaxSize()) {
        var isFront by rememberSaveable { mutableStateOf(true) }

        if (isFront) {
            HumanBodyFrontComponent(viewModel, width, height) { identifier, wounds ->
                onClick(identifier, wounds)
            }
        } else {
            HumanBodyBackComponent(viewModel, width, height) { identifier, wounds ->
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
    viewModel: HumanBodyViewModel,
    width: Int,
    height: Int,
    onClick: (String, Map<String, List<String>>) -> Unit
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
            painter = painterResource(id = R.drawable.ic_front_human_body_background),
            contentDescription = null
        )

        if (viewModel.uiState.onSelectWound) {
            BottomSheetComponent(
                content = {
                    WoundsContent { wounds ->
                        viewModel.saveFrontList(selectedFrontArea)
                        onClick("identifier", mapOf(selectedFrontArea.name to wounds))
                        selectedFrontArea = FrontArea.NONE
                        viewModel.handledOnWoundSelected()
                    }
                },
                sheetState = sheetState,
                scope = scope,
            ) {
                viewModel.handledOnWoundSelected()
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
    viewModel: HumanBodyViewModel,
    width: Int,
    height: Int,
    onClick: (String, Map<String, List<String>>) -> Unit
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
            painter = painterResource(id = R.drawable.ic_back_human_body_background),
            contentDescription = null
        )

        if (uiState.onSelectWound) {
            BottomSheetComponent(
                content = {
                    WoundsContent { wounds ->
                        viewModel.saveBackList(selectedBackArea)
                        onClick("identifier", mapOf(selectedBackArea.name to wounds))
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

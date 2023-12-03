package com.skgtecnologia.sisem.ui.humanbody

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.humanbody.area.BASE_HEIGHT
import com.skgtecnologia.sisem.ui.humanbody.area.BASE_WIDTH
import com.skgtecnologia.sisem.ui.humanbody.view.HumanBodyViewComponent
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import com.valkiria.uicomponents.components.humanbody.HumanBodyUiModel

@Composable
fun HumanBodyComponent(
    uiModel: HumanBodyUiModel,
    onAction: (HumanBodyUi) -> Unit
) {
    if (uiModel.values != null && uiModel.values?.isNotEmpty() == true) {
        HumanBodyViewComponent(values = uiModel.values)
    } else {
        val viewModel = hiltViewModel<HumanBodyViewModel>()
        val width = LocalContext.current.display?.width ?: BASE_WIDTH
        val height = LocalContext.current.display?.height ?: BASE_HEIGHT

        Box(modifier = uiModel.modifier.fillMaxSize()) {
            var isFront by rememberSaveable { mutableStateOf(true) }

            if (isFront) {
                HumanBodyFrontComponent(uiModel, viewModel, width, height) { humanBodyUi ->
                    onAction(humanBodyUi)
                }
            } else {
                HumanBodyBackComponent(uiModel, viewModel, width, height) { humanBodyUi ->
                    onAction(humanBodyUi)
                }
            }

            SwitchBodyType(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(36.dp)
            ) {
                isFront = !isFront
            }
        }
    }
}

package com.skgtecnologia.sisem.ui.humanbody

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.model.body.HumanBodyModel
import com.skgtecnologia.sisem.ui.humanbody.area.BASE_HEIGHT
import com.skgtecnologia.sisem.ui.humanbody.area.BASE_WIDTH
import com.valkiria.uicomponents.R

@Composable
fun HumanBodyComponent(
    model: HumanBodyModel,
    onAction: (id: String, wounds: Map<String, List<String>>) -> Unit
) {
    val viewModel = hiltViewModel<HumanBodyViewModel>()
    val width = LocalContext.current.display?.width ?: BASE_WIDTH
    val height = LocalContext.current.display?.height ?: BASE_HEIGHT

    Box(modifier = model.modifier.fillMaxSize()) {
        var isFront by rememberSaveable { mutableStateOf(true) }

        if (isFront) {
            HumanBodyFrontComponent(viewModel, width, height) { identifier, wounds ->
                onAction(identifier, wounds)
            }
        } else {
            HumanBodyBackComponent(viewModel, width, height) { identifier, wounds ->
                onAction(identifier, wounds)
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_flip),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
                .clickable { isFront = !isFront }
                .size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

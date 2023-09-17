package com.valkiria.uicomponents.components.humanbody

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R

private const val DEFAULT_DISPLAY_WIDTH = 1440
private const val DEFAULT_DISPLAY_HEIGHT = 2900

@Composable
fun HumanBodyComponent(
    onClick: (Area) -> Unit = {}
) {
    var x by remember { mutableFloatStateOf(0f) }
    var y by remember { mutableFloatStateOf(0f) }
    val selectedAreas by remember { mutableStateOf(mutableListOf<Area>()) }

    val width = LocalContext.current.display?.width ?: DEFAULT_DISPLAY_WIDTH
    val height = LocalContext.current.display?.height ?: DEFAULT_DISPLAY_HEIGHT

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    x = it.x
                    y = it.y

                    val selectedArea = Area.fromPosition(x, y, width, height)
                    if (selectedArea == Area.NONE) return@detectTapGestures

                    onClick(selectedArea)

                    if (selectedAreas.contains(selectedArea)) {
                        selectedAreas.remove(selectedArea)
                    } else {
                        selectedAreas.add(selectedArea)
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



    Text(text = "x: $x, y: $y")
    Text(modifier = Modifier.padding(30.dp),
        text = "width: ${LocalContext.current.display?.width}, height: ${LocalContext.current.display?.height}")
}

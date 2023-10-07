package com.valkiria.uicomponents.components.slider

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.model.ui.body.SliderUiModel
import kotlin.math.roundToInt

private const val COLOR_0 = "#56ADB2"
private const val COLOR_1 = "#395146"
private const val COLOR_2 = "#415341"
private const val COLOR_3 = "#466742"
private const val COLOR_4 = "#655C46"
private const val COLOR_5 = "#6C573D"
private const val COLOR_6 = "#74553E"
private const val COLOR_7 = "#655146"
private const val COLOR_8 = "#815359"
private const val COLOR_9 = "#8A3C47"
private const val COLOR_10 = "#862936"

@Composable
fun SliderComponent(
    uiModel: SliderUiModel,
    onAction: (id: String, value: Int) -> Unit
) {
    var value by rememberSaveable { mutableFloatStateOf(uiModel.selected.toFloat()) }

    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement
    ) {
        Slider(
            value = value,
            steps = uiModel.max - uiModel.min,
            valueRange = uiModel.min.toFloat()..uiModel.max.toFloat(),
            onValueChange = { value = it },
            onValueChangeFinished = {
                onAction(uiModel.identifier, value.toInt())
            },
            thumb = {
                SliderThumb(value = value.roundToInt())
            },
            colors = SliderDefaults.colors(
                thumbColor = getColorFromValue(value.roundToInt()),
                activeTrackColor = getColorFromValue(value.roundToInt())
            )
        )
    }
}

@Composable
private fun SliderThumb(value: Int) {
    Box(
        modifier = Modifier.padding(bottom = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_slider_shape),
            contentDescription = null,
            tint = getColorFromValue(value)
        )
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = value.toString(),
            color = Color.White
        )
    }
}

@Suppress("MagicNumber")
private fun getColorFromValue(value: Int): Color = when (value) {
    0 -> Color(parseColor(COLOR_0))
    1 -> Color(parseColor(COLOR_1))
    2 -> Color(parseColor(COLOR_2))
    3 -> Color(parseColor(COLOR_3))
    4 -> Color(parseColor(COLOR_4))
    5 -> Color(parseColor(COLOR_5))
    6 -> Color(parseColor(COLOR_6))
    7 -> Color(parseColor(COLOR_7))
    8 -> Color(parseColor(COLOR_8))
    9 -> Color(parseColor(COLOR_9))
    10 -> Color(parseColor(COLOR_10))
    else -> Color(parseColor(COLOR_0))
}

@Preview(showBackground = true)
@Composable
fun SliderComponentPreview() {
    SliderComponent(
        uiModel = SliderUiModel(
            identifier = "slider",
            min = 0,
            max = 100,
            selected = 50,
            modifier = Modifier,
            arrangement = Arrangement.Center
        ),
        onAction = { _, _ -> }
    )
}

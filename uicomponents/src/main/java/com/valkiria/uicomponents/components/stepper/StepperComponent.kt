package com.valkiria.uicomponents.components.stepper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.mapToColors
import com.valkiria.uicomponents.components.button.mapToTextColor
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle
import timber.log.Timber

@Composable
fun StepperComponent(
    uiModel: StepperUiModel,
    onAction: (id: String) -> Unit
) {
    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement
    ) {
        Button(
            onClick = {
                onAction(uiModel.identifier)
            },
            colors = ButtonStyle.LOUD.mapToColors(),
            modifier = uiModel.modifier.fillMaxWidth()
        ) {
            Text(
                text = "FINALIZADO",
                color = ButtonStyle.LOUD.mapToTextColor(),
                style = TextStyle.HEADLINE_5.toTextStyle()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StepperComponentPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        StepperComponent(
            uiModel = StepperUiModel(
                identifier = "APH_STEPPER",
                options = emptyMap(),
                arrangement = Arrangement.Center,
                modifier = Modifier
            )
        ) {
            Timber.d("Stepper clicked")
        }
    }
}

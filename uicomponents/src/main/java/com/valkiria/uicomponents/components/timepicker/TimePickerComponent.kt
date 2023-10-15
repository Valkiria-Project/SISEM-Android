package com.valkiria.uicomponents.components.timepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle

@Composable
fun TimePickerComponent(
    uiModel: TimePickerUiModel,
    onAction: (id: String, value: String) -> Unit
) {
    onAction(uiModel.identifier, "${uiModel.hour.text} : ${uiModel.minute.text}")

    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = uiModel.title.text,
                color = Color.White,
                style = uiModel.title.textStyle.toTextStyle()
            )

            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TimeBox(
                    label = stringResource(id = R.string.time_picker_hour),
                    value = uiModel.hour
                )

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = ":",
                    color = Color.White,
                    style = TextStyle.HEADLINE_1.toTextStyle()
                )

                TimeBox(
                    label = stringResource(id = R.string.time_picker_minutes),
                    value = uiModel.minute
                )
            }
        }
    }
}

@Composable
private fun TimeBox(label: String, value: TextUiModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = value.text,
                color = Color.White,
                style = value.textStyle.toTextStyle()
            )
        }

        Text(
            text = label,
            color = Color.White,
            style = TextStyle.HEADLINE_8.toTextStyle()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimePickerComponentPreview() {
    TimePickerComponent(
        uiModel = TimePickerUiModel(
            identifier = "mock",
            modifier = Modifier,
            arrangement = Arrangement.Center,
            title = TextUiModel(
                text = "Hora de la aplicación",
                textStyle = TextStyle.HEADLINE_4
            ),
            hour = TextUiModel(
                text = "20",
                textStyle = TextStyle.HEADLINE_1
            ),
            minute = TextUiModel(
                text = "10",
                textStyle = TextStyle.HEADLINE_1
            )
        ),
        onAction = { _, _ -> }
    )
}

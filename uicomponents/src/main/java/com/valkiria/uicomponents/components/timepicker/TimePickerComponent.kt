package com.valkiria.uicomponents.components.timepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalTimeFromHhMmString
import com.valkiria.uicomponents.utlis.TimeUtils.isSameDay

@Suppress("LongMethod")
@Composable
fun TimePickerComponent(
    uiModel: TimePickerUiModel,
    onAction: (id: String, hour: String, minutes: String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var hour = uiModel.hour
    var minute = uiModel.minute

    Row(
        modifier = uiModel.modifier
            .clickable {
                showDialog = true
            }
            .fillMaxWidth(),
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
                    label = stringResource(id = R.string.time_box_hour),
                    value = hour
                )

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = ":",
                    color = Color.White,
                    style = TextStyle.HEADLINE_1.toTextStyle()
                )

                TimeBox(
                    label = stringResource(id = R.string.time_box_minutes),
                    value = minute
                )
            }
        }
    }

    if (showDialog) {
        val pickerState = rememberTimePickerState(
            initialHour = uiModel.hour.text.toInt(),
            initialMinute = uiModel.minute.text.toInt(),
            is24Hour = true
        )

        TimePickerDialog(
            onDismissRequest = {
                showDialog = false
                showError = false
                errorMessage = ""
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        uiModel.date?.let {
                            val pickerHour = pickerState.hour
                                .toString()
                                .padStart(2, '0')
                                .padEnd(2, '0')
                            val pickerMinute = pickerState.minute
                                .toString()
                                .padStart(2, '0')
                                .padEnd(2, '0')

                            val pickerTime = getLocalTimeFromHhMmString(
                                "$pickerHour:$pickerMinute"
                            )
                            val systemTime = getLocalTimeFromHhMmString(
                                "${uiModel.hour.text}:${uiModel.minute.text}"
                            )
                            if (isSameDay(it) && systemTime.isBefore(pickerTime)) {
                                showError = true
                                errorMessage =
                                    "La hora seleccionada supera la hora actual del sistema."
                            } else {
                                hour = uiModel.hour.copy(text = pickerHour)
                                minute = uiModel.minute.copy(text = pickerMinute)

                                showDialog = false
                                onAction(uiModel.identifier, pickerHour, pickerMinute)
                            }
                        } ?: run {
                            showError = true
                            errorMessage = "Seleccione una fecha."
                        }
                    }
                ) {
                    Text(stringResource(R.string.date_picker_select))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        showError = false
                        errorMessage = ""
                    }
                ) {
                    Text(stringResource(R.string.date_picker_cancel))
                }
            },
            showError = showError,
            errorMessage = errorMessage
        ) {
            TimeInput(state = pickerState)
        }
    }
}

@Suppress("LongParameterList")
@Composable
fun TimePickerDialog(
    title: String = stringResource(R.string.time_picker_title),
    errorMessage: String = "",
    showError: Boolean = false,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = containerColor
                ),
            color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
                if (showError) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
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
                .padding(8.dp)
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
        onAction = { _, _, _ -> }
    )
}

package com.valkiria.uicomponents.bricks.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.model.mocks.getPreOpDriverAuxGuardianTextFieldUiModel
import com.valkiria.uicomponents.model.ui.textfield.TextFieldUiModel
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalDateFromInstant
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalDateInMillis
import java.time.Instant

// FIXME: Pass data to upper level and use given data
@Suppress("LongMethod", "UnusedPrivateMember")
@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun DatePickerTextFieldView(
    uiModel: TextFieldUiModel,
    validateFields: Boolean,
    onAction: (id: String, updatedValue: String, fieldValidated: Boolean) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val startLabel = buildString {
        val today = getLocalDateFromInstant(Instant.now())
        val dayOfMonth = today.dayOfMonth.toString().padStart(2, '0')
        val month = today.monthValue.toString().padStart(2, '0')
        val year = today.year.toString()

        append("$dayOfMonth/$month/$year")
    }

    var selectedDate by remember {
        val today = getLocalDateFromInstant(Instant.now())
        mutableStateOf(today)
    }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = startLabel,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .onFocusChanged {
                    showDialog = it.isFocused
                }
                .widthIn(max = 320.dp),
            label = { Text("Check–in date") },
            trailingIcon = {
                Icon(Icons.Filled.Event, contentDescription = "Select date")
            }
        )
    }
    if (showDialog) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = getLocalDateInMillis(selectedDate)
        )
        DatePickerDialog(
            onDismissRequest = {
                showDialog = false
                focusManager.clearFocus()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val instant = Instant.ofEpochMilli(pickerState.selectedDateMillis!!)
                        selectedDate = getLocalDateFromInstant(instant)
                        instant.toEpochMilli()
                        showDialog = false
                        focusManager.clearFocus()
                    }
                ) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        focusManager.clearFocus()
                    }
                ) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(state = pickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DatePickerTextFieldViewPreview() {
    Column(
        modifier = Modifier.background(Color.DarkGray)
    ) {
        DatePickerTextFieldView(
            uiModel = getPreOpDriverAuxGuardianTextFieldUiModel(),
            onAction = { _, _, _ -> },
            validateFields = true
        )
    }
}

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.extensions.toFailedValidation
import com.valkiria.uicomponents.mocks.getPreOpDriverAuxGuardianTextFieldUiModel
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalDateFromInstant
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalDateInMillis
import java.time.Instant
import java.time.LocalDate

@Suppress("LongMethod")
@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun DatePickerTextFieldView(
    uiModel: TextFieldUiModel,
    validateFields: Boolean,
    onAction: (id: String, updatedValue: String, fieldValidated: Boolean) -> Unit
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var showDialog by remember { mutableStateOf(false) }

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    fun getLabelDate() = buildString {
        val date = selectedDate
        val dayOfMonth = date.dayOfMonth.toString().padStart(2, '0')
        val month = date.monthValue.toString().padStart(2, '0')
        val year = date.year.toString()

        append("$dayOfMonth/$month/$year")
    }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .onFocusChanged {
                    showDialog = it.isFocused
                }
                .widthIn(max = 320.dp),
            label = { uiModel.placeholder?.let { Text(it) } },
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
                        selectedDate = getLocalDateFromInstant(instant).plusDays(1L)
                        text = TextFieldValue(getLabelDate())
                        showDialog = false
                        focusManager.clearFocus()

                        onAction(
                            uiModel.identifier,
                            selectedDate.toString(),
                            text.toFailedValidation(uiModel.validations, true) == null
                        )
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

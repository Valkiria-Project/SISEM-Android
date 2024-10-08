package com.valkiria.uicomponents.bricks.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.R.string
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.extensions.toFailedValidation
import com.valkiria.uicomponents.mocks.getPreOpDriverAuxGuardianTextFieldUiModel
import com.valkiria.uicomponents.utlis.TimeUtils
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalDate
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalDateInMillis
import java.time.Instant
import java.time.LocalDate

private const val DATE_TIME_DELIMITER = "T"

@Suppress("LongMethod")
@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun DatePickerTextFieldView(
    uiModel: TextFieldUiModel,
    validateFields: Boolean,
    onAction: (id: String, updatedValue: String, fieldValidated: Boolean, required: Boolean) -> Unit
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(uiModel.text))
    }

    val inputError = remember(text, validateFields) {
        text.toFailedValidation(uiModel.validations, validateFields)
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
                .fillMaxWidth(),
            label = { uiModel.placeholder?.let { Text(it) } },
            trailingIcon = {
                Icon(
                    Icons.Filled.Event,
                    contentDescription = stringResource(string.date_picker_select)
                )
            },
            supportingText = {
                if (inputError != null) {
                    Text(
                        text = inputError.message,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            isError = inputError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedTrailingIconColor = Color.White,
                unfocusedBorderColor = Color.White,
                unfocusedLabelColor = Color.White,
                unfocusedTrailingIconColor = Color.White
            )
        )
    }

    if (showDialog) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = getLocalDateInMillis(selectedDate),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                    if (uiModel.maxDate != null) {
                        val maxDate = uiModel.maxDate.substringBefore(DATE_TIME_DELIMITER)

                        if (uiModel.minDate == null) {
                            utcTimeMillis < TimeUtils.getEpochMillis(maxDate)
                        } else {
                            val minDate = uiModel.minDate.substringBefore(DATE_TIME_DELIMITER)
                            val minMillis = TimeUtils.getEpochMillis(minDate)
                            val maxMillis = TimeUtils.getEpochMillis(maxDate)
                            utcTimeMillis in minMillis..maxMillis
                        }
                    } else {
                        true
                    }
            }
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
                        selectedDate = getLocalDate(instant).plusDays(1L)
                        text = TextFieldValue(getLabelDate())
                        showDialog = false
                        focusManager.clearFocus()

                        onAction(
                            uiModel.identifier,
                            text.text,
                            text.toFailedValidation(uiModel.validations, true) == null,
                            uiModel.required
                        )
                    }
                ) {
                    Text(stringResource(string.date_picker_select))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        focusManager.clearFocus()
                    }
                ) {
                    Text(stringResource(string.date_picker_cancel))
                }
            },
            colors = DatePickerDefaults.colors()
        ) {
            DatePicker(
                state = pickerState,
                colors = DatePickerDefaults.colors(
                    titleContentColor = Color.White,
                    headlineContentColor = Color.White,
                    weekdayContentColor = Color.White,
                    subheadContentColor = Color.White,
                    yearContentColor = Color.White,
                    currentYearContentColor = Color.White,
                    selectedYearContentColor = Color.White
                )
            )
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
            onAction = { _, _, _, _ -> },
            validateFields = true
        )
    }
}

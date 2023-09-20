package com.valkiria.uicomponents.bricks.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.model.mocks.getPreOpDriverAuxGuardianTextFieldUiModel
import com.valkiria.uicomponents.model.ui.textfield.TextFieldUiModel
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalDateFromInstant
import java.time.Instant

@Composable
fun DateTextFieldView(
    uiModel: TextFieldUiModel,
    validateFields: Boolean,
    onAction: (id: String, updatedValue: String, fieldValidated: Boolean) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    var selectedStartDate by remember {
        val today = getLocalDateFromInstant(Instant.now())
        mutableStateOf(today)
    }

    val startLabel = buildString {
        // you might want to update the formatting, depending on your locale
        val dayOfMonth = selectedStartDate.dayOfMonth.toString().padStart(2, '0')
        val month = selectedStartDate.monthValue.toString().padStart(2, '0')
        val year = (selectedStartDate.year % 100).toString().padStart(2, '0')
        append("$dayOfMonth/$month/$year")
    }

    OutlinedTextField(
        value = startLabel,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .onFocusChanged {
                showDialog = it.isFocused
            },
        label = {
            uiModel.label?.let { label ->
                Text(text = label)
            } ?: uiModel.placeholder?.let { label ->
                Text(text = label)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DateTextFieldViewPreview() {
    Column(
        modifier = Modifier.background(Color.DarkGray)
    ) {
        OutlinedTextFieldView(
            uiModel = getPreOpDriverAuxGuardianTextFieldUiModel(),
            onAction = { _, _, _ -> },
            validateFields = true
        )
    }
}

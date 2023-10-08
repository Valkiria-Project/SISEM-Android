package com.valkiria.uicomponents.bricks.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.mocks.getPreOpDriverAuxGuardianTextFieldUiModel
import com.valkiria.uicomponents.utlis.TimeUtils.getFormattedLocalTimeAsString
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalDateFromInstant
import java.time.Instant

@Suppress("UnusedPrivateMember")
@Composable
fun FixedDateTextFieldView(
    uiModel: TextFieldUiModel,
    validateFields: Boolean,
    onAction: (id: String, updatedValue: String, fieldValidated: Boolean) -> Unit
) {
    val startLabel = buildString {
        val today = getLocalDateFromInstant(Instant.now())
        val dayOfMonth = today.dayOfMonth.toString().padStart(2, '0')
        val month = today.monthValue.toString().padStart(2, '0')
        val year = today.year.toString()

        append("$dayOfMonth/$month/$year ${getFormattedLocalTimeAsString()}")
    }

    val date by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(startLabel))
    }

    // FIXME: Do this at IconButton onClick level
    onAction(
        uiModel.identifier,
        date.text,
        true
    )

    OutlinedTextField(
        value = date,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        label = {
            uiModel.label?.let { label ->
                Text(text = label)
            } ?: uiModel.placeholder?.let { label ->
                Text(text = label)
            }
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    // FIXME: Update the date time
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.ic_update_time
                    ),
                    contentDescription = null
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FixedDateTextFieldViewPreview() {
    Column(
        modifier = Modifier.background(Color.DarkGray)
    ) {
        FixedDateTextFieldView(
            uiModel = getPreOpDriverAuxGuardianTextFieldUiModel(),
            onAction = { _, _, _ -> },
            validateFields = true
        )
    }
}

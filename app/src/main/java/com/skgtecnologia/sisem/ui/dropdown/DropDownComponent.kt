package com.skgtecnologia.sisem.ui.dropdown

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.valkiria.uicomponents.bricks.bottomsheet.BottomSheetView
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownItemUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownUiModel
import com.valkiria.uicomponents.components.header.HeaderUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import com.valkiria.uicomponents.extensions.toFailedValidation
import kotlinx.coroutines.launch

private const val EMPTY_REGEX = "^(?!\\s*$).+"

@Suppress("LongMethod")
@Composable
fun DropDownComponent(
    uiModel: DropDownUiModel,
    validateFields: Boolean,
    onAction: (dropDownInputUiModel: DropDownInputUiModel) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val focusManager = LocalFocusManager.current

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()

    LaunchedEffect(showDialog) {
        launch {
            when {
                showDialog -> {
                    scope.launch { sheetState.show() }
                }
            }
        }
    }

    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement,
        verticalAlignment = Alignment.CenterVertically
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
            label = { Text(text = uiModel.label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Select ${uiModel.label}"
                )
            },
            supportingText = {
                if (validateFields) {
                    Text(
                        text = text.toFailedValidation(
                            listOf(
                                ValidationUiModel(
                                    regex = EMPTY_REGEX,
                                    message = stringResource(
                                        id = R.string.field_empty_validation_message
                                    )
                                )
                            )
                        )?.message.orEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            isError = text.toFailedValidation(
                listOf(
                    ValidationUiModel(
                        regex = EMPTY_REGEX,
                        message = stringResource(id = R.string.field_empty_validation_message)
                    )
                ),
                validateFields
            ) != null
        )
    }

    if (showDialog) {
        BottomSheetView(
            content = {
                DropDownContent(
                    headerModel = uiModel.header,
                    defaultSelected = uiModel.selected,
                    itemList = uiModel.items,
                    onAction = { selectedItem ->
                        focusManager.clearFocus()
                        showDialog = false
                        text = TextFieldValue(selectedItem.name)
                        onAction(
                            DropDownInputUiModel(
                                identifier = uiModel.identifier,
                                id = selectedItem.id,
                                name = selectedItem.name,
                                fieldValidated = selectedItem.name.isNotEmpty()
                            )
                        )
                    }
                )
            },
            sheetState = sheetState,
            scope = scope
        ) {
            focusManager.clearFocus()
            showDialog = false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DropDownComponentPreview() {
    DropDownComponent(
        uiModel = DropDownUiModel(
            identifier = "id",
            label = "Label",
            items = listOf(DropDownItemUiModel("1", "Option 1")),
            selected = "1",
            header = HeaderUiModel(
                identifier = "TEST",
                title = TextUiModel(
                    text = "Nacionalidad",
                    textStyle = TextStyle.HEADLINE_1
                ),
                subtitle = TextUiModel(
                    text = "Escriba la nacionalidad a la que pertenece el paciente.",
                    textStyle = TextStyle.HEADLINE_5
                ),
                leftIcon = "ic_message",
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
            ),
            modifier = Modifier,
            arrangement = Arrangement.Center,
            section = null
        ),
        validateFields = true,
        onAction = { _ -> }
    )
}

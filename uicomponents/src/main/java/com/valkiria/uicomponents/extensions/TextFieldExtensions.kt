package com.valkiria.uicomponents.extensions

import androidx.compose.ui.text.input.TextFieldValue
import com.valkiria.uicomponents.components.textfield.ValidationUiModel

fun TextFieldValue.toFailedValidation(
    validations: List<ValidationUiModel>,
    validateFields: Boolean = true
): ValidationUiModel? {
    if (validateFields.not()) {
        return null
    }

    val invalidRegex = validations.find {
        text.matches(it.regex.toRegex()).not()
    }

    return invalidRegex
}

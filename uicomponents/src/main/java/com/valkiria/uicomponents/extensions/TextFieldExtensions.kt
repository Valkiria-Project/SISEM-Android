package com.valkiria.uicomponents.extensions

import androidx.compose.ui.text.input.TextFieldValue
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import timber.log.Timber

fun TextFieldValue.toFailedValidation(
    validations: List<ValidationUiModel>,
    validateFields: Boolean = true
): ValidationUiModel? {
    if (validateFields.not()) {
        Timber.d("Validacion: validar falso $validateFields")
        return null
    }

    val invalidRegex = validations.find {
        text.matches(it.regex.toRegex()).not()
    }

    if (invalidRegex != null) {
        Timber.d("Validacion: falla con ${invalidRegex.message}")
    }
    return invalidRegex
}

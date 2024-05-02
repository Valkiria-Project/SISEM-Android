package com.valkiria.uicomponents.extensions

import androidx.compose.ui.text.input.TextFieldValue
import com.valkiria.uicomponents.components.textfield.ValidationUiModel

private const val QUANTITY_MESSAGE = "La cantidad que se está registrando" +
    " es mayor a la cantidad en inventario."

fun TextFieldValue.toFailedValidation(
    validations: List<ValidationUiModel>,
    validateFields: Boolean = true,
    quantity: Int? = null
): ValidationUiModel? {
    if (validateFields.not()) {
        return null
    }

    return if (quantity != null) {
        runCatching {
            text.toInt()
        }.fold(
            onSuccess = {
                if (it > quantity) {
                    ValidationUiModel(
                        message = QUANTITY_MESSAGE,
                        regex = ""
                    )
                } else {
                    null
                }
            },
            onFailure = {
                validations.find {
                    text.matches(it.regex.toRegex()).not()
                }
            }
        )
    } else {
        validations.find {
            text.matches(it.regex.toRegex()).not()
        }
    }
}

fun String.toFailedValidation(
    validations: List<ValidationUiModel>,
    validateFields: Boolean = true,
    quantity: Int? = null
): ValidationUiModel? {
    if (validateFields.not()) {
        return null
    }

    return if (quantity != null) {
        runCatching {
            this.toInt()
        }.fold(
            onSuccess = {
                if (it > quantity) {
                    ValidationUiModel(
                        message = QUANTITY_MESSAGE,
                        regex = ""
                    )
                } else {
                    null
                }
            },
            onFailure = {
                validations.find {
                    this.matches(it.regex.toRegex()).not()
                }
            }
        )
    } else {
        validations.find {
            this.matches(it.regex.toRegex()).not()
        }
    }
}

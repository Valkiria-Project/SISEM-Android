package com.valkiria.uicomponents.extensions

import com.valkiria.uicomponents.components.chip.ChipOptionUiModel

fun String?.toFailedValidation(
    validateFields: Boolean = true
): Boolean {
    if (validateFields.not()) {
        return false
    }

    return this.isNullOrEmpty()
}

fun List<ChipOptionUiModel>.toFailedValidation(
    validateFields: Boolean = true
): Boolean {
    if (validateFields.not()) {
        return false
    }

    return this.none { it.selected }
}

package com.valkiria.uicomponents.extensions

fun String?.toFailedValidation(
    validateFields: Boolean = true
): Boolean {
    if (validateFields.not()) {
        return false
    }

    return this.isNullOrEmpty()
}

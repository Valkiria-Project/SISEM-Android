package com.valkiria.uicomponents.props

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

enum class KeyBoardUiType {
    TEXT,
    NUMBER,
    EMAIL,
    PHONE,
    PASSWORD
}

fun KeyBoardUiType.toKeyBoardOption() : KeyboardOptions {
    return when (this) {
        KeyBoardUiType.TEXT -> KeyboardOptions.Default
        KeyBoardUiType.NUMBER -> KeyboardOptions(keyboardType = KeyboardType.Number)
        KeyBoardUiType.EMAIL -> KeyboardOptions(keyboardType = KeyboardType.Email)
        KeyBoardUiType.PHONE -> KeyboardOptions(keyboardType = KeyboardType.Phone)
        KeyBoardUiType.PASSWORD -> KeyboardOptions(keyboardType = KeyboardType.Password)
    }
}

fun KeyBoardUiType.isPassword() : Boolean {
    return this == KeyBoardUiType.PASSWORD
}

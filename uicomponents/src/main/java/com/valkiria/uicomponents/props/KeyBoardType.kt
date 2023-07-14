package com.valkiria.uicomponents.props

import android.os.Parcelable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.parcelize.Parcelize

@Parcelize
enum class KeyBoardType : Parcelable {
    TEXT,
    NUMBER,
    EMAIL,
    PHONE,
    PASSWORD
}

fun KeyBoardType.toKeyBoardOption(): KeyboardOptions {
    return when (this) {
        KeyBoardType.TEXT -> KeyboardOptions.Default
        KeyBoardType.NUMBER -> KeyboardOptions(keyboardType = KeyboardType.Number)
        KeyBoardType.EMAIL -> KeyboardOptions(keyboardType = KeyboardType.Email)
        KeyBoardType.PHONE -> KeyboardOptions(keyboardType = KeyboardType.Phone)
        KeyBoardType.PASSWORD -> KeyboardOptions(keyboardType = KeyboardType.Password)
    }
}

fun KeyBoardType.isPassword(): Boolean {
    return this == KeyBoardType.PASSWORD
}

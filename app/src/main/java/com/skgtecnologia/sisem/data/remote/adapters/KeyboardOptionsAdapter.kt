package com.skgtecnologia.sisem.data.remote.adapters

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson

const val TEXT_KEYBOARD = "TEXT"
const val TEXT_NUMBER = "NUMBER"
const val TEXT_EMAIL = "EMAIL"
const val TEXT_PHONE = "PHONE"
const val TEXT_PASSWORD = "PASSWORD"

class KeyboardOptionsAdapter {

    @FromJson
    fun fromJson(jsonReader: JsonReader): KeyboardOptions {
        val value = jsonReader.nextString()

        return when {
            value.startsWith(TEXT_KEYBOARD) -> KeyboardOptions(keyboardType = KeyboardType.Text)
            value.startsWith(TEXT_NUMBER) -> KeyboardOptions(keyboardType = KeyboardType.Number)
            value.startsWith(TEXT_EMAIL) -> KeyboardOptions(keyboardType = KeyboardType.Email)
            value.startsWith(TEXT_PHONE) -> KeyboardOptions(keyboardType = KeyboardType.Phone)
            value.startsWith(TEXT_PASSWORD) -> KeyboardOptions(keyboardType = KeyboardType.Password)
            else -> KeyboardOptions(keyboardType = KeyboardType.Text)
        }
    }

    @ToJson
    fun toJson(keyboardOptions: KeyboardOptions): String {
        return when (keyboardOptions.keyboardType) {
            KeyboardType.Text -> TEXT_KEYBOARD
            KeyboardType.Number -> TEXT_NUMBER
            KeyboardType.Email -> TEXT_EMAIL
            KeyboardType.Phone -> TEXT_PHONE
            KeyboardType.Password -> TEXT_PASSWORD
            else -> TEXT_KEYBOARD
        }
    }
}

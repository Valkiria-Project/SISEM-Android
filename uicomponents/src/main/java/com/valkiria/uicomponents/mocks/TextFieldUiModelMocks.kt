@file:Suppress("MagicNumber")

package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.textfield.TextFieldStyle
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import kotlin.random.Random

fun getLoginUserTextFieldUiModel(): TextFieldUiModel {
    return TextFieldUiModel(
        identifier = Random(100).toString(),
        icon = "ic_user",
        placeholder = "Usuario",
        label = "Ingresar usuario",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text
        ),
        textStyle = TextStyle.HEADLINE_5,
        charLimit = 100,
        validations = listOf(
            ValidationUiModel(
                regex = "^(?!\\s*$).+",
                message = "El campo no debe estar vacío"
            )
        ),
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 20.dp,
            top = 20.dp,
            end = 20.dp,
            bottom = 0.dp
        )
    )
}

fun getPreOpDriverAuxGuardianTextFieldUiModel(): TextFieldUiModel {
    return TextFieldUiModel(
        identifier = "OPENING_DATE_N1",
        label = "Fecha de apertura - Guardián N1",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        textStyle = TextStyle.HEADLINE_5,
        style = TextFieldStyle.FIXED_DATE,
        charLimit = 100,
        validations = listOf(
            ValidationUiModel(
                regex = "^(?!\\s*$).+",
                message = "El campo no debe estar vacío"
            )
        ),
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 20.dp,
            top = 20.dp,
            end = 20.dp,
            bottom = 0.dp
        )
    )
}

fun getPreOpDriverVehicleKMTextFieldUiModel(): TextFieldUiModel {
    return TextFieldUiModel(
        identifier = Random(100).toString(),
        label = "Kilometraje del vehículo*",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        textStyle = TextStyle.HEADLINE_5,
        charLimit = 100,
        validations = listOf(
            ValidationUiModel(
                regex = "^(?!\\s*$).+",
                message = "El campo no debe estar vacío"
            )
        ),
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 20.dp,
            top = 20.dp,
            end = 20.dp,
            bottom = 0.dp
        )
    )
}

package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.TextStyle

fun getDeviceAuthSerialLabelUiModel(): LabelUiModel {
    return LabelUiModel(
        identifier = "DEVICE_AUTH_SERIAL",
        text = "Serial dispositivo",
        textStyle = TextStyle.BUTTON_1,
        arrangement = Arrangement.Center,
        modifier = Modifier
    )
}

fun getDeviceAuthLicensePlateLabelUiModel(): LabelUiModel {
    return LabelUiModel(
        identifier = "DEVICE_AUTH_PLATE",
        text = "Placa vehículo",
        textStyle = TextStyle.BUTTON_1,
        arrangement = Arrangement.Center,
        modifier = Modifier
    )
}

package com.valkiria.uicomponents.mocks

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.props.TextStyle

fun getDeviceAuthSerialLabelUiModel(): LabelUiModel {
    return LabelUiModel(
        text = "Serial dispositivo",
        textStyle = TextStyle.BUTTON_1,
        modifier = Modifier
    )
}

fun getDeviceAuthLicensePlateLabelUiModel(): LabelUiModel {
    return LabelUiModel(
        text = "Placa vehiculo",
        textStyle = TextStyle.BUTTON_1,
        modifier = Modifier
    )
}

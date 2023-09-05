package com.valkiria.uicomponents.model.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.model.props.TextStyle

fun getDeviceAuthSerialLabelUiModel(): LabelUiModel {
    return LabelUiModel(
        text = "Serial dispositivo",
        textStyle = TextStyle.BUTTON_1,
        arrangement = Arrangement.Center,
        modifier = Modifier
    )
}

fun getDeviceAuthLicensePlateLabelUiModel(): LabelUiModel {
    return LabelUiModel(
        text = "Placa vehiculo",
        textStyle = TextStyle.BUTTON_1,
        arrangement = Arrangement.Center,
        modifier = Modifier
    )
}

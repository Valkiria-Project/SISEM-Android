package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

@Composable
fun getMediaActionsFileUiModel(): LabelUiModel {
    return LabelUiModel(
        identifier = "MEDIA_ACTIONS_IMAGE_NAME",
        text = "file name is very very very large.jpg",
        textStyle = TextStyle.HEADLINE_6,
        rightIcon = "ic_trash",
        arrangement = Arrangement.Start,
        modifier = Modifier
            .padding(14.dp)
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                RoundedCornerShape(90)
            )
            .padding(horizontal = 20.dp, vertical = 4.dp)
    )
}

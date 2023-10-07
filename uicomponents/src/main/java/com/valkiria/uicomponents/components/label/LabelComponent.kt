package com.valkiria.uicomponents.components.label

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.body.LabelUiModel
import com.valkiria.uicomponents.model.mocks.getDeviceAuthLicensePlateLabelUiModel
import com.valkiria.uicomponents.model.mocks.getDeviceAuthSerialLabelUiModel
import com.valkiria.uicomponents.model.props.toTextStyle

@Composable
fun LabelComponent(
    uiModel: LabelUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = uiModel.modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = uiModel.arrangement
    ) {
        Text(
            text = uiModel.text,
            color = Color(parseColor(uiModel.textColor)),
            style = uiModel.textStyle.toTextStyle()
        )
    }
}

@Preview(showBackground = false)
@Composable
fun LabelComponentPreview() {
    Column {
        LabelComponent(
            uiModel = getDeviceAuthSerialLabelUiModel(),
            modifier = Modifier.padding(16.dp)
        )
        LabelComponent(
            uiModel = getDeviceAuthLicensePlateLabelUiModel(),
            modifier = Modifier.padding(0.dp)
        )
    }
}

package com.valkiria.uicomponents.components.label

import android.text.Html
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun LabelComponent(
    labelUiModel: LabelUiModel,
    modifier: Modifier = Modifier
) {
    when(labelUiModel.style) {
        LabelStyle.HEADING -> LabelHeading(
            labelUiModel = labelUiModel,
            modifier = modifier
        )
        LabelStyle.PRIMARY -> LabelPrimary(
            labelUiModel = labelUiModel,
            modifier = modifier
        )
    }
}

@Composable
private fun LabelHeading(
    labelUiModel: LabelUiModel,
    modifier: Modifier = Modifier
) {
    Text(
        text = Html.fromHtml(labelUiModel.text).toString(),
        color = Color.White,
        fontSize = 24.sp
    )
}

@Composable
private fun LabelPrimary(
    labelUiModel: LabelUiModel,
    modifier: Modifier = Modifier
) {
    Text(
        text = Html.fromHtml(labelUiModel.text).toString(),
        color = Color.White,
        fontSize = 12.sp
    )
}
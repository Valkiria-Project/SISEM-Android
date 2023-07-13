package com.valkiria.uicomponents.props.label

import android.text.Html
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.valkiria.uicomponents.props.TextStyleUi

@Composable
fun LabelComponent(
    richLabelUiModel: RichLabelUiModel,
    modifier: Modifier = Modifier
) {
    when (richLabelUiModel.textStyle) {
        TextStyleUi.HEADLINE_1 -> LabelHeading(
            richLabelUiModel = richLabelUiModel,
            modifier = modifier
        )

        TextStyleUi.HEADLINE_2 -> LabelPrimary(
            richLabelUiModel = richLabelUiModel,
            modifier = modifier
        )

        else -> {}
    }
}

@Composable
private fun LabelHeading(
    richLabelUiModel: RichLabelUiModel,
    modifier: Modifier = Modifier
) {
    Text(
        text = Html.fromHtml(richLabelUiModel.text).toString(),
        color = Color.White,
        fontSize = 24.sp
    )
}

@Composable
private fun LabelPrimary(
    richLabelUiModel: RichLabelUiModel,
    modifier: Modifier = Modifier
) {
    Text(
        text = Html.fromHtml(richLabelUiModel.text).toString(),
        color = Color.White,
        fontSize = 12.sp
    )
}

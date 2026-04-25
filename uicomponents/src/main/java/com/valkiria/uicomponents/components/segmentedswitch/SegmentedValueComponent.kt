package com.valkiria.uicomponents.components.segmentedswitch

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle

private const val DEFAULT_COLOR = "#3CF2DD"

@Suppress("LongMethod", "MagicNumber", "UnusedPrivateMember")
@Composable
fun SegmentedValueComponent(
    modifier: Modifier = Modifier,
    uiModel: SegmentedValueUiModel,
    hasFinding: Boolean,
    onAction: (title: String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = uiModel.title.text,
            style = uiModel.title.textStyle.toTextStyle(),
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .padding(end = 8.dp),
            color = Color.White
        )

        if (hasFinding) {
            Text(
                text = "Hallazgo",
                style = uiModel.title.textStyle.toTextStyle(),
                modifier = Modifier
                    .clickable { onAction(uiModel.title.text) }
                    .background(
                        color = Color(parseColor(uiModel.color)),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(8.dp),
                color = Color.Black
            )
        } else {
            Text(
                text = "Óptimo",
                style = uiModel.title.textStyle.toTextStyle(),
                modifier = Modifier
                    .padding(8.dp),
                color = Color(parseColor(DEFAULT_COLOR))
            )
        }
    }
}

@Preview
@Composable
fun SegmentedValueRedComponentPreview() {
    SegmentedValueComponent(
        uiModel = SegmentedValueUiModel(
            title = TextUiModel(
                text = "Bomba de infusión N1",
                textStyle = TextStyle.HEADLINE_5
            ),
            color = "#f55757"
        ),
        hasFinding = true
    ) {
    }
}

@Preview
@Composable
fun SegmentedValueGreenComponentPreview() {
    SegmentedValueComponent(
        uiModel = SegmentedValueUiModel(
            title = TextUiModel(
                text = "Bomba de infusión N1",
                textStyle = TextStyle.HEADLINE_5
            ),
            color = "#3CF2DD"
        ),
        hasFinding = false
    ) {
    }
}

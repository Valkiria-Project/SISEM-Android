package com.valkiria.uicomponents.components.obstetrician

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle

@Composable
fun ObstetricianDataComponent(
    uiModel: ObstetricianDataUiModel
) {
    Column(
        modifier = uiModel.modifier
    ) {
        Row {
            uiModel.data.forEach { obsDataUiModel ->
                ObstetricianDataView(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    obsDataUiModel = obsDataUiModel
                )
            }
        }

        ObstetricianDataView(
            modifier = Modifier
                .padding(top = 10.dp)
                .weight(1f),
            obsDataUiModel = ObsDataUiModel(
                letter = TextUiModel(
                    text = "FUR",
                    textStyle = TextStyle.HEADLINE_5
                ),
                result = uiModel.fur
            )
        )
    }
}

@Composable
private fun ObstetricianDataView(
    modifier: Modifier = Modifier,
    obsDataUiModel: ObsDataUiModel
) {
    Column(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                )
            )
            .background(color = Color.DarkGray)
    ) {
        Text(
            text = obsDataUiModel.letter.text,
            style = obsDataUiModel.letter.textStyle.toTextStyle(),
        )

        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = obsDataUiModel.result.text,
            style = obsDataUiModel.result.textStyle.toTextStyle()
        )
    }
}

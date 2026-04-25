package com.valkiria.uicomponents.components.obstetrician

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle

private const val FUR = "FUR"

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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomEnd = 10.dp,
                        bottomStart = 10.dp
                    )
                )
                .background(color = Color.DarkGray),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = FUR,
                style = TextStyle.HEADLINE_5.toTextStyle(),
            )

            Text(
                modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                text = uiModel.fur.text,
                style = uiModel.fur.textStyle.toTextStyle()
            )
        }
    }
}

@Composable
private fun ObstetricianDataView(
    modifier: Modifier = Modifier,
    obsDataUiModel: ObsDataUiModel,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    Column(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp,
                    bottomEnd = 10.dp,
                    bottomStart = 10.dp
                )
            )
            .background(color = Color.DarkGray),
        horizontalAlignment = alignment
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

@Suppress("LongMethod")
@Preview
@Composable
fun ObstetricianDataComponentPreview() {
    ObstetricianDataComponent(
        uiModel = ObstetricianDataUiModel(
            identifier = "12345",
            data = listOf(
                ObsDataUiModel(
                    letter = TextUiModel(
                        text = "G",
                        textStyle = TextStyle.HEADLINE_5
                    ),
                    result = TextUiModel(
                        text = "1",
                        textStyle = TextStyle.HEADLINE_5
                    )
                ),
                ObsDataUiModel(
                    letter = TextUiModel(
                        text = "P",
                        textStyle = TextStyle.HEADLINE_5
                    ),
                    result = TextUiModel(
                        text = "1",
                        textStyle = TextStyle.HEADLINE_5
                    )
                ),
                ObsDataUiModel(
                    letter = TextUiModel(
                        text = "A",
                        textStyle = TextStyle.HEADLINE_5
                    ),
                    result = TextUiModel(
                        text = "1",
                        textStyle = TextStyle.HEADLINE_5
                    )
                ),
                ObsDataUiModel(
                    letter = TextUiModel(
                        text = "C",
                        textStyle = TextStyle.HEADLINE_5
                    ),
                    result = TextUiModel(
                        text = "1",
                        textStyle = TextStyle.HEADLINE_5
                    )
                ),
                ObsDataUiModel(
                    letter = TextUiModel(
                        text = "V",
                        textStyle = TextStyle.HEADLINE_5
                    ),
                    result = TextUiModel(
                        text = "1",
                        textStyle = TextStyle.HEADLINE_5
                    )
                )
            ),
            fur = TextUiModel(
                text = "20/08/2023",
                textStyle = TextStyle.HEADLINE_5
            ),
            arrangement = Arrangement.Center,
            modifier = Modifier
        )
    )
}

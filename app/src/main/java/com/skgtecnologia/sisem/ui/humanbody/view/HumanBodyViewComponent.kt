package com.skgtecnologia.sisem.ui.humanbody.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.humanbody.HumanBodyType
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle

private const val WOUND_CARD_MAX_WIDTH = 0.33f
private const val MAX_WIDTH = 0.45f

@Composable
fun HumanBodyViewComponent(
    values: Map<String, List<HumanBodyUi>>?
) {
    var isFront by rememberSaveable { mutableStateOf(true) }

    if (isFront) {
        val wounds = values?.get(HumanBodyType.FRONT.name)

        Column {
            HumanBodyFrontViewComponent(
                wounds = wounds
            ) {
                isFront = !isFront
            }

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(MAX_WIDTH)
            ) {
                wounds?.forEach { humanBodyUi ->
                    WoundCard(humanBodyUi = humanBodyUi)
                }
            }
        }
    } else {
        val wounds = values?.get(HumanBodyType.BACK.name)

        Row {
            HumanBodyBackViewComponent(
                modifier = Modifier
                    .weight(1f),
                wounds = wounds
            ) {
                isFront = !isFront
            }

            Column {
                wounds?.forEach { humanBodyUi ->
                    WoundCard(
                        modifier = Modifier
                            .fillMaxWidth(WOUND_CARD_MAX_WIDTH),
                        humanBodyUi = humanBodyUi
                    )
                }
            }
        }
    }
}

@Composable
fun WoundCard(
    modifier: Modifier = Modifier,
    humanBodyUi: HumanBodyUi
) {
    Box(
        modifier = modifier
            .padding(start = 10.dp, bottom = 15.dp)
            .clip(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                )
            )
            .background(color = Color.DarkGray),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 10.dp),
                text = humanBodyUi.areaName,
                style = TextStyle.HEADLINE_4.toTextStyle(),
            )

            humanBodyUi.wounds.forEach { wound ->
                Text(
                    text = wound,
                    style = TextStyle.HEADLINE_6.toTextStyle(),
                )
            }
        }
    }
}

@Preview
@Composable
fun HumanBodyViewComponentPreview() {
    HumanBodyViewComponent(
        values = mapOf(
            "FRONT" to listOf(
                HumanBodyUi(
                    type = "FRONT",
                    area = "HEAD",
                    areaName = "Cabeza",
                    wounds = listOf("Herida 1", "Herida 2")
                ),
                HumanBodyUi(
                    type = "FRONT",
                    area = "RIGHT_FOREARM",
                    areaName = "Antebrazo Derecho",
                    wounds = listOf("Herida 1", "Herida 2")
                )
            )
        )
    )
}

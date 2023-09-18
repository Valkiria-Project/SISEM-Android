package com.valkiria.uicomponents.components.humanbody

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.bricks.chip.SuggestionChipView
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.props.toTextStyle

private const val BURN_WOUND = "Quemadura"

@Composable
fun WoundsContent(
    onClick: (List<String>) -> Unit
) {
    val selectedWounds = mutableListOf<String>()
    var isBurnWoundSelected by remember { mutableStateOf(false) }

    /*HeaderSection(
        headerModel = HeaderModel(
            title = TextModel(
                text = stringResource(R.string.wounds_title),
                textStyle = TextStyle.HEADLINE_1
            ),
            subtitle = TextModel(
                text = stringResource(R.string.wounds_subtitle),
                textStyle = TextStyle.HEADLINE_5
            ),
            leftIcon = stringResource(R.string.wounds_left_icon),
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
        )
    )*/
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        FlowRow(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val woundsList = stringArrayResource(id = R.array.wounds_list)
            woundsList.forEach { text ->
                SuggestionChipView(text = text, textStyle = TextStyle.HEADLINE_5) { selected ->
                    if (text != BURN_WOUND && selected) {
                        selectedWounds.add(text)
                    } else {
                        selectedWounds.remove(text)
                        isBurnWoundSelected = !isBurnWoundSelected
                    }
                }
            }
        }

        if (isBurnWoundSelected) {
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(R.string.wounds_burn_description),
                style = TextStyle.HEADLINE_5.toTextStyle()
            )

            FlowRow(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val burnTypes = stringArrayResource(id = R.array.wounds_burn_list)
                burnTypes.forEach { text ->
                    SuggestionChipView(text = text, textStyle = TextStyle.HEADLINE_5) { selected ->
                        if (selected) {
                            selectedWounds.add(text)
                        } else {
                            selectedWounds.remove(text)
                        }
                    }
                }
            }
        }

        Button(
            onClick = { onClick(selectedWounds) },
            modifier = Modifier.padding(top = 32.dp).fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.wounds_save_cta),
                style = TextStyle.HEADLINE_3.toTextStyle()
            )
        }
    }
}

package com.skgtecnologia.sisem.ui.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.bricks.chip.SuggestionChipView
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.props.toTextStyle

private const val BURN_WOUND = "Quemadura"

@Composable
fun WoundsContent(
    onClick: (List<String>) -> Unit
) {
    val selectedWounds = mutableListOf<String>()

    HeaderSection(
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
    )

    FlowRow(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val woundsList = stringArrayResource(id = R.array.wounds_list)
        woundsList.forEach { text ->
            SuggestionChipView(text = text, textStyle = TextStyle.HEADLINE_5) { selected ->
                if (selected) {
                    selectedWounds.add(text)
                } else {
                    selectedWounds.remove(text)
                }
            }
        }
    }

    if (selectedWounds.contains(BURN_WOUND)) {
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
                    val burnText = "$BURN_WOUND $text"
                    if (selected) {
                        selectedWounds.add(burnText)
                    } else {
                        selectedWounds.remove(burnText)
                    }
                }
            }
        }
    }

    Button(onClick = { onClick(selectedWounds) }) {
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(R.string.wounds_burn_description),
            style = TextStyle.HEADLINE_5.toTextStyle()
        )
    }
}

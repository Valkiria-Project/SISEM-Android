package com.skgtecnologia.sisem.ui.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.bricks.ChipSectionModel
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.bricks.chip.SuggestionChipView
import com.valkiria.uicomponents.props.TextStyle

@Composable
fun FindingsContent(chipSection: ChipSectionModel) {
    HeaderSection(
        headerModel = HeaderModel(
            title = TextModel(
                text = chipSection.title.text,
                textStyle = TextStyle.HEADLINE_1
            ),
            subtitle = TextModel(
                text = stringResource(R.string.finding_subtitle),
                textStyle = TextStyle.HEADLINE_5
            ),
            leftIcon = stringResource(R.string.finding_left_icon),
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
        )
    )

    FlowRow(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        chipSection.listText.texts.forEach { text ->
            SuggestionChipView(text = text, textStyle = chipSection.listText.textStyle)
        }
    }
}

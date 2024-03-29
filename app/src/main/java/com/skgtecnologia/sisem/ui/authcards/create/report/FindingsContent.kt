package com.skgtecnologia.sisem.ui.authcards.create.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.header.findingsHeader
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.bricks.chip.SuggestionChipView

@Composable
fun FindingsContent(chipSection: ChipSectionUiModel) {
    HeaderSection(
        headerUiModel = findingsHeader(
            titleText = chipSection.title.text,
            subtitleText = stringResource(id = R.string.finding_subtitle),
            leftIcon = stringResource(id = R.string.finding_left_icon)
        )
    )

    FlowRow(
        modifier = Modifier
            .padding(start = 20.dp, top = 8.dp, end = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        chipSection.listText?.let {
            it.texts.forEach { text ->
                SuggestionChipView(text = text, textStyle = it.textStyle)
            }
        }
    }
}

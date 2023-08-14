package com.skgtecnologia.sisem.ui.authcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.model.bricks.ChipSectionModel
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.components.crewmembercard.ChipView
import com.valkiria.uicomponents.props.TextStyle

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FindingsContent(chipSection: ChipSectionModel) {

    HeaderSection(
        headerModel = HeaderModel(
            title = TextModel(
                text = chipSection.title.text,
                textStyle = TextStyle.HEADLINE_1
            ),
            subtitle = TextModel(
                text = "Revise los hallazgos registrados en el preoperacional del turno anterior.",
                textStyle = TextStyle.HEADLINE_5
            ),
            leftIcon = "ic_finding",
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)
        )
    )

    FlowRow(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        chipSection.listText.texts.forEach { text ->
            ChipView(text = text, textStyle = chipSection.listText.textStyle)
        }
    }
}

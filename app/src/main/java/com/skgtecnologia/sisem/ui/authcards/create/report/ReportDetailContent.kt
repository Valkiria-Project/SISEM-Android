package com.skgtecnologia.sisem.ui.authcards.create.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel

@Composable
fun ReportDetailContent(
    model: ReportsDetailUiModel
) {
    HeaderSection(headerUiModel = model.header, modifier = Modifier)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        model.details.forEachIndexed { index, detail ->
            ReportContent(model = detail)

            if (index != model.details.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

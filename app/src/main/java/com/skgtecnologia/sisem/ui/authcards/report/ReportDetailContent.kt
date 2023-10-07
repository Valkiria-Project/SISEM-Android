package com.skgtecnologia.sisem.ui.authcards.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.model.ui.report.ReportsDetailModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection

@Composable
fun ReportDetailContent(
    model: ReportsDetailModel
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
                Divider(
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

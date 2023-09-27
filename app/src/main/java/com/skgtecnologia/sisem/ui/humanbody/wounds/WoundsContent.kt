package com.skgtecnologia.sisem.ui.humanbody.wounds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.header.woundsHeader
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.components.chip.ChipOptionsComponent
import com.valkiria.uicomponents.components.chip.ChipSelectionComponent
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.props.toTextStyle
import com.valkiria.uicomponents.model.ui.chip.ChipOptionUiModel
import com.valkiria.uicomponents.model.ui.chip.ChipOptionsUiModel
import com.valkiria.uicomponents.model.ui.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.model.ui.chip.ChipSelectionUiModel

@Composable
fun WoundsContent(
    onAction: (wounds: List<String>) -> Unit
) {
    val viewModel = hiltViewModel<WoundsViewModel>()
    val uiState = viewModel.uiState

    viewModel.setBurnList(stringArrayResource(id = R.array.wounds_burn_grade_list).toList())

    HeaderSection(
        headerModel = woundsHeader(
            titleText = stringResource(R.string.wounds_title),
            subtitleText = stringResource(R.string.wounds_subtitle),
            leftIcon = stringResource(R.string.wounds_left_icon)
        )
    )

    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        ChipOptionsComponent(
            uiModel = ChipOptionsUiModel(
                identifier = "wounds",
                items = stringArrayResource(id = R.array.wounds_list).mapIndexed { index, text ->
                    ChipOptionUiModel(id = index.toString(), name = text, selected = false)
                },
                modifier = Modifier
            )
        ) { _, text, isSelected ->
            viewModel.updateWoundsList(text, isSelected)
        }

        if (uiState.onBurnSelected) {
            ChipSelectionComponent(
                uiModel = ChipSelectionUiModel(
                    identifier = "burn_type",
                    titleText = stringResource(R.string.wounds_burn_description),
                    titleTextStyle = TextStyle.HEADLINE_5,
                    items = stringArrayResource(
                        id = R.array.wounds_burn_grade_list
                    ).mapIndexed { index, text ->
                        ChipSelectionItemUiModel(id = index.toString(), name = text)
                    },
                    arrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            ) { _, text, _ ->
                viewModel.updateBurnList(text)
            }
        }

        Button(
            enabled = uiState.selectedWounds.isNotEmpty(),
            onClick = { onAction(uiState.selectedWounds) },
            modifier = Modifier
                .padding(top = 32.dp, bottom = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.wounds_save_cta),
                style = TextStyle.HEADLINE_3.toTextStyle()
            )
        }
    }
}
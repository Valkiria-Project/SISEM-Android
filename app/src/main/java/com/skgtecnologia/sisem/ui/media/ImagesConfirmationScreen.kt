package com.skgtecnologia.sisem.ui.media

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.bricks.ReportDetailModel
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.skgtecnologia.sisem.ui.bottomsheet.ReportContent
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.ReportNavigationModel
import com.skgtecnologia.sisem.ui.report.ReportViewModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.model.props.TabletWidth
import com.valkiria.uicomponents.model.props.TextStyle

@Composable
fun ImagesConfirmationScreen(
    viewModel: ReportViewModel,
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (imageSelectionNavigationModel: NavigationModel?) -> Unit
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        when {
            uiState.onGoBack -> {
                viewModel.handleGoBack()
                onNavigation(ReportNavigationModel(goBack = true))
            }
        }
    }

    Column(
        modifier = if (isTablet) {
            modifier.width(TabletWidth)
        } else {
            modifier.fillMaxWidth()
        },
    ) {
        HeaderSection(
            headerModel = getImagesConfirmationHeaderModel()
        ) { uiAction ->
            if (uiAction is HeaderUiAction.GoBack) {
                viewModel.goBack()
            }
        }

        ReportContent(
            model = ReportDetailModel(
                images = listOf(
                    "data:image/jpeg;base64," +
                            "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",
                    "data:image/jpeg;base64," +
                            "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",
                    "data:image/jpeg;base64,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"
                ),
                title = TextModel("Test", TextStyle.HEADLINE_6),
                subtitle = TextModel("Test", TextStyle.HEADLINE_6),
                description = TextModel("Test", TextStyle.HEADLINE_6),
                modifier = Modifier
            )
        )
    }
}

@Composable
private fun getImagesConfirmationHeaderModel() = HeaderModel(
    title = TextModel(
        stringResource(id = R.string.images_confirmation_title),
        TextStyle.HEADLINE_1
    ),
    subtitle = TextModel(
        stringResource(R.string.images_confirmation_subtitle),
        TextStyle.HEADLINE_5
    ),
    leftIcon = stringResource(R.string.images_confirmation_left_icon),
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)

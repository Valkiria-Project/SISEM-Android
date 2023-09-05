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
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
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

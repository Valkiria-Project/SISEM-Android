package com.skgtecnologia.sisem.ui.news

import HideKeyboard
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.news.model.NewsIdentifier
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.NewsUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.errorbanner.OnErrorHandler
import com.valkiria.uicomponents.components.loader.LoaderComponent

@Composable
fun AddReportRoleScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (role: String) -> Unit,
    onCancel: () -> Unit
) {
    val viewModel = hiltViewModel<AddReportRoleViewModel>()
    val uiState = viewModel.uiState

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body, footer) = createRefs()

        uiState.screenModel?.header?.let {
            HeaderSection(
                headerModel = it,
                modifier = modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }

        BodySection(
            body = uiState.screenModel?.body,
            isTablet = isTablet,
            modifier = modifier
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(footer.top)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp)
        ) { uiAction ->
            handleUiAction(uiAction, onNavigation)
        }

        uiState.screenModel?.footer?.let {
            FooterSection(
                footerModel = it,
                modifier = modifier.constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                }
            ) { uiAction ->
                handleFooterUiAction(uiAction, onCancel)
            }
        }
    }

    OnErrorHandler(uiState.errorModel) {
        viewModel.handleShownError()
    }

    if (uiState.isLoading) {
        HideKeyboard()
        LoaderComponent(modifier)
    }
}

private fun handleUiAction(
    uiAction: UiAction,
    onNavigation: (role: String) -> Unit
) {
    (uiAction as? NewsUiAction)?.let {
        when (uiAction) {
            is NewsUiAction.NewsStepOneOnChipClick -> onNavigation(uiAction.text)
        }
    }
}

fun handleFooterUiAction(
    uiAction: UiAction,
    onCancel: () -> Unit
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            NewsIdentifier.ADD_REPORT_CANCEL_BUTTON.name -> onCancel()
        }
    }
}

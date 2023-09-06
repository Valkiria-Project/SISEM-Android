package com.skgtecnologia.sisem.ui.recordnews

import HideKeyboard
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.recordnews.model.RecordNewsIdentifier
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.RecordNewsUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.errorbanner.OnErrorHandler
import com.valkiria.uicomponents.components.loader.LoaderComponent

@Composable
fun RecordNewsScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    role: String,
    onNavigation: () -> Unit,
    onCancel: () -> Unit
) {
    val viewModel = hiltViewModel<RecordNewsViewModel>()
    val uiState = viewModel.uiState

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

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
            ) {
                onCancel()
            }
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
                .padding(top = 20.dp),
            validateFields = uiState.validateFields
        ) { uiAction ->
            handleUiAction(uiAction, viewModel, onNavigation)
        }

        uiState.screenModel?.footer?.let {
            FooterSection(
                footerModel = it,
                modifier = modifier.constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                }
            ) { uiAction ->
                handleFooterUiAction(uiAction, viewModel, onCancel)
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
    viewModel: RecordNewsViewModel,
    onNavigation: () -> Unit
) {
    (uiAction as? RecordNewsUiAction)?.let {
        when (uiAction) {
            is RecordNewsUiAction.TopicInput -> {
                viewModel.topic = uiAction.updatedValue
                viewModel.isValidTopic = uiAction.fieldValidated
            }

            is RecordNewsUiAction.DescriptionInput -> {
                viewModel.description = uiAction.updatedValue
                viewModel.isValidDescription = uiAction.fieldValidated
            }
        }
    }
}

private fun handleFooterUiAction(
    uiAction: UiAction,
    viewModel: RecordNewsViewModel,
    onCancel: () -> Unit
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            RecordNewsIdentifier.ADD_REPORT_ENTRY_CANCEL_BUTTON.name -> onCancel()
            RecordNewsIdentifier.ADD_REPORT_ENTRY_GALLERY_BUTTON.name -> {} // FIXME: finish this work
        }
    }
}

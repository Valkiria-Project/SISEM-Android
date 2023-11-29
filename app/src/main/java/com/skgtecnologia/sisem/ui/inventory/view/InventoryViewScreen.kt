package com.skgtecnologia.sisem.ui.inventory.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.inventory.model.TransferReturnIdentifiers
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun InventoryViewScreen(
    modifier: Modifier = Modifier,
    onNavigation: (inventoryViewNavigationModel: NavigationModel?) -> Unit
) {
    val viewModel = hiltViewModel<InventoryViewViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.navigationModel != null -> {
                    viewModel.consumeNavigationEvent()
                    onNavigation(uiState.navigationModel)
                }
            }
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body, footer) = createRefs()

        uiState.screenModel?.header?.let {
            HeaderSection(
                headerUiModel = it,
                modifier = modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) { uiAction ->
                if (uiAction is HeaderUiAction.GoBack) {
                    viewModel.goBack()
                }
            }
        }

        BodySection(
            body = uiState.screenModel?.body,
            modifier = modifier.constrainAs(body) {
                top.linkTo(header.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            },
            validateFields = uiState.validateFields
        ) { uiAction ->
            handleAction(uiAction, viewModel)
        }

        uiState.screenModel?.footer?.let {
            FooterSection(
                footerModel = it,
                modifier = modifier.constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                }
            ) { uiAction ->
                handleFooterAction(uiAction, viewModel)
            }
        }
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.handleEvent(it)
    }

    OnBannerHandler(uiModel = uiState.infoEvent) { uiAction ->
        handleFooterAction(uiAction, viewModel)
        viewModel.handleEvent(uiAction)
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

fun handleAction(uiAction: UiAction, viewModel: InventoryViewViewModel) {
    when (uiAction) {
        is GenericUiAction.DropDownAction ->
            viewModel.dropDownValue.value = DropDownInputUiModel(
                uiAction.identifier,
                uiAction.id,
                uiAction.name,
                uiAction.fieldValidated
            )

        is GenericUiAction.InputAction ->
            viewModel.fieldsValues[uiAction.identifier] = InputUiModel(
                uiAction.identifier,
                uiAction.updatedValue,
                uiAction.fieldValidated
            )

        is GenericUiAction.ChipSelectionAction ->
            viewModel.chipValues[uiAction.identifier] = uiAction.chipSelectionItemUiModel

        else -> Timber.d("no-op")
    }
}

fun handleFooterAction(uiAction: UiAction, viewModel: InventoryViewViewModel) {
    (uiAction as? FooterUiAction)?.let {
        when (it.identifier) {
            TransferReturnIdentifiers.TRANSFER_RETURN_SEND_BUTTON.name -> viewModel.save()

            TransferReturnIdentifiers.TRANSFER_RETURN_CANCEL_BANNER.name ->
                viewModel.consumeInfoEvent()

            TransferReturnIdentifiers.TRANSFER_RETURN_SAVE_BANNER.name ->
                viewModel.saveTransferReturn()
        }
    }
}

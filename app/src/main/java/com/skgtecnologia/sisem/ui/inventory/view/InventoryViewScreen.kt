package com.skgtecnologia.sisem.ui.inventory.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.inventory.model.TransferReturnIdentifiers
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun InventoryViewScreen(
    modifier: Modifier = Modifier,
    inventoryName: String,
    onNavigation: (inventoryViewNavigationModel: InventoryViewNavigationModel) -> Unit
) {
    val viewModel = hiltViewModel<InventoryViewViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.initScreen(inventoryName)
    }

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
        is GenericUiAction.ButtonAction -> viewModel.save()

        is GenericUiAction.DropDownAction -> viewModel.handleDropDownAction(uiAction)

        is GenericUiAction.InputAction -> viewModel.handleInputAction(uiAction)

        is GenericUiAction.ChipSelectionAction -> viewModel.handleChipSelectionAction(uiAction)

        else -> Timber.d("no-op")
    }
}

fun handleFooterAction(uiAction: UiAction, viewModel: InventoryViewViewModel) {
    (uiAction as? FooterUiAction)?.let {
        when (it.identifier) {
            TransferReturnIdentifiers.TRANSFER_RETURN_CANCEL_BANNER.name ->
                viewModel.consumeInfoEvent()

            TransferReturnIdentifiers.TRANSFER_RETURN_SAVE_BANNER.name ->
                viewModel.saveTransferReturn()
        }
    }
}

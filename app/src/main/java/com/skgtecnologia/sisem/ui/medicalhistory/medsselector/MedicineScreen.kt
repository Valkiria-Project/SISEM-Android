package com.skgtecnologia.sisem.ui.medicalhistory.medsselector

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MedicineScreen(
    modifier: Modifier = Modifier,
    onNavigation: (medicineNavigationModel: NavigationModel?) -> Unit
) {

    val viewModel = hiltViewModel<MedicineViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.navigationModel) {
        launch {
            with(uiState.navigationModel) {
                if (this?.goBack == true || this?.medicine != null) {
                    onNavigation(uiState.navigationModel)
                    viewModel.consumeNavigationEvent()
                }
            }
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body) = createRefs()

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
                    viewModel.navigateBack()
                }
            }
        }

        BodySection(
            body = uiState.screenModel?.body,
            modifier = modifier
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp),
            validateFields = uiState.validateFields
        ) { uiAction ->
            handleAction(uiAction, viewModel)
        }
    }

    OnBannerHandler(uiModel = uiState.infoEvent) {
        viewModel.consumeInfoEvent()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

fun handleAction(
    uiAction: UiAction,
    viewModel: MedicineViewModel
) {
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

        is GenericUiAction.TimePickerAction -> viewModel.timePickerValue.value = uiAction.value

        is GenericUiAction.ChipSelectionAction ->
            viewModel.chipValues[uiAction.identifier] = uiAction.text

        is GenericUiAction.ButtonAction -> viewModel.saveMedicine()

        else -> { Timber.d("no-op") }
    }
}

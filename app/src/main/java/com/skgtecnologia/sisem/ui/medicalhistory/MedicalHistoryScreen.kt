package com.skgtecnologia.sisem.ui.medicalhistory

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.GenericUiAction.StepperAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.media.MediaAction
import com.valkiria.uicomponents.components.media.MediaAction.Camera
import com.valkiria.uicomponents.components.media.MediaAction.Gallery
import com.valkiria.uicomponents.components.media.MediaAction.MediaFile
import com.valkiria.uicomponents.extensions.storeUriAsFileToCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod", "LongParameterList")
@Composable
fun MedicalHistoryScreen(
    viewModel: MedicalHistoryViewModel,
    modifier: Modifier = Modifier,
    vitalSigns: Map<String, String>?,
    medicine: Map<String, String>?,
    signature: String?,
    photoTaken: Boolean = false,
    onNavigation: (medicalHistoryNavigationModel: NavigationModel?) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.navigationModel) {
        launch {
            uiState.navigationModel?.let {
                onNavigation(uiState.navigationModel)
                viewModel.consumeNavigationEvent()
            }
        }
    }

    LaunchedEffect(vitalSigns) {
        launch {
            vitalSigns?.let { viewModel.updateVitalSignsInfoCard(vitalSigns) }
        }
    }

    LaunchedEffect(medicine) {
        launch {
            medicine?.let { viewModel.updateMedicineInfoCard(medicine) }
        }
    }

    LaunchedEffect(signature) {
        launch {
            signature?.let { viewModel.updateSignature(signature) }
        }
    }

    LaunchedEffect(photoTaken) {
        launch {
            if (photoTaken) {
                viewModel.updateMediaActions()
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
                    viewModel.goBack()
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
                },
            validateFields = uiState.validateFields
        ) { uiAction ->
            handleAction(uiAction, viewModel, context, scope)
        }
    }

    OnBannerHandler(uiModel = uiState.infoEvent) {
        viewModel.consumeShownInfoEvent()
    }

    OnBannerHandler(uiModel = uiState.errorEvent) { uiAction ->
        viewModel.handleEvent(uiAction)
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

@Suppress("ComplexMethod", "LongMethod")
fun handleAction(
    uiAction: UiAction,
    viewModel: MedicalHistoryViewModel,
    context: Context,
    scope: CoroutineScope
) {
    when (uiAction) {
        is GenericUiAction.ChipOptionAction -> viewModel.handleChipOptionAction(uiAction)

        is GenericUiAction.ChipSelectionAction -> viewModel.handleChipSelectionAction(uiAction)

        is GenericUiAction.DropDownAction -> viewModel.handleDropDownAction(uiAction)

        is GenericUiAction.HumanBodyAction -> viewModel.handleHumanBodyAction(uiAction)

        is GenericUiAction.ImageButtonAction -> viewModel.handleImageButtonAction(uiAction)

        is GenericUiAction.InfoCardAction -> viewModel.showVitalSignsForm(uiAction.identifier)

        is GenericUiAction.InputAction -> viewModel.handleInputAction(uiAction)

        is GenericUiAction.MediaItemAction -> when (uiAction.mediaAction) {
            Camera -> viewModel.showCamera()
            is MediaFile -> viewModel.updateMediaActions(
                selectedMedia = (uiAction.mediaAction as MediaFile).uris,
            )

            is Gallery -> viewModel.updateMediaActions(
                selectedMedia = (uiAction.mediaAction as Gallery).uris,
            )

            is MediaAction.RemoveFile -> viewModel.removeMediaActionsImage(
                (uiAction.mediaAction as MediaAction.RemoveFile).index
            )
        }

        is GenericUiAction.MedsSelectorAction -> viewModel.showMedicineForm(uiAction.identifier)

        is GenericUiAction.SegmentedSwitchAction -> viewModel.handleSegmentedSwitchAction(uiAction)

        is GenericUiAction.SignatureAction -> viewModel.showSignaturePad(uiAction.identifier)

        is GenericUiAction.SliderAction -> viewModel.handleSliderAction(uiAction)

        is StepperAction -> {
            scope.launch {
                val images = viewModel.uiState.selectedMediaUris.mapNotNull { uri ->
                    runCatching {
                        context.storeUriAsFileToCache(
                            uri,
                            viewModel.uiState.operationConfig?.maxFileSizeKb
                        )
                    }.fold(
                        onSuccess = { it },
                        onFailure = { null }
                    )
                }
                viewModel.sendMedicalHistory(images)
            }
        }

        else -> Timber.d("no-op")
    }
}

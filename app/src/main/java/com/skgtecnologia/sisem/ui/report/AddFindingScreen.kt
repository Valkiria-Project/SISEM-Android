package com.skgtecnologia.sisem.ui.report

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.footer.findingsFooter
import com.skgtecnologia.sisem.domain.model.header.addFindingHeader
import com.skgtecnologia.sisem.domain.model.label.addFilesHint
import com.skgtecnologia.sisem.domain.report.model.AddFindingIdentifier
import com.skgtecnologia.sisem.domain.report.model.ImagesConfirmationIdentifier
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.media.MediaAction.Camera
import com.valkiria.uicomponents.components.media.MediaAction.File
import com.valkiria.uicomponents.components.media.MediaAction.Gallery
import com.valkiria.uicomponents.components.media.MediaActionsComponent
import com.valkiria.uicomponents.components.media.MediaActionsUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldComponent
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import kotlin.random.Random
import kotlinx.coroutines.launch
import timber.log.Timber

private const val DESCRIPTION_INPUT_MIN_LINES = 3

@Suppress("LongMethod")
@Composable
fun AddFindingScreen(
    viewModel: ReportViewModel,
    findingId: String,
    modifier: Modifier = Modifier,
    onNavigation: (findingsNavigationModel: NavigationModel?) -> Unit
) {
    val uiState = viewModel.uiState

    BackHandler {
        Timber.d("Navigate back")
        viewModel.navigateBackFromReport()
    }

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.navigationModel != null && uiState.cancelInfoModel == null &&
                    uiState.confirmInfoModel == null && uiState.successInfoModel == null -> {
                    onNavigation(uiState.navigationModel)
                    viewModel.consumeNavigationEvent()
                }
            }
        }
    }

    LaunchedEffect(findingId) {
        launch {
            viewModel.findingId = findingId
        }
    }

    SideEffect {
        Timber.d("Finding id: $findingId")
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HeaderSection(
            headerUiModel = addFindingHeader(
                titleText = stringResource(id = R.string.findings_title),
                subtitleText = stringResource(id = R.string.findings_subtitle),
                leftIcon = stringResource(id = R.string.back_icon)
            )
        ) { uiAction ->
            if (uiAction is HeaderUiAction.GoBack) {
                viewModel.navigateBackFromReport()
            }
        }

        TextFieldComponent(
            uiModel = getFindingsDescriptionModel(),
            validateFields = uiState.validateFields
        ) { inputUiModel ->
            viewModel.description = inputUiModel.updatedValue
            viewModel.isValidDescription = inputUiModel.fieldValidated
        }

        LabelComponent(
            uiModel = addFilesHint(stringResource(id = R.string.findings_add_files_label))
        )

        Text(
            text = stringResource(
                id = R.string.findings_selected_files_label,
                viewModel.uiState.selectedImageUris.size.toString()
            ),
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
            )
        )

        MediaActionsComponent(uiModel = MediaActionsUiModel()) { mediaAction ->
            when (mediaAction) {
                Camera -> viewModel.showCamera(isFromPreOperational = true)
                is File -> Timber.d("no-op")
                is Gallery -> viewModel.updateSelectedImages(
                    selectedImages = mediaAction.uris,
                    isFromPreOperational = true
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        FooterSection(
            footerModel = findingsFooter(
                leftButtonText = stringResource(id = R.string.cancel_cta),
                rightButtonText = stringResource(id = R.string.save_cta),
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        ) { uiAction ->
            handleFooterAction(uiAction, viewModel)
        }
    }

    OnBannerHandler(uiState.confirmInfoModel) {
        handleFooterAction(it, viewModel)
    }

    OnBannerHandler(uiState.successInfoModel) {
        onNavigation(uiState.navigationModel)
    }

    OnBannerHandler(uiState.cancelInfoModel) {
        handleFooterAction(it, viewModel)
    }

    OnBannerHandler(uiState.infoEvent) {
        viewModel.consumeShownError()
    }

    LocalFocusManager.current.clearFocus()

    OnLoadingHandler(uiState.isLoading, modifier)
}

@Suppress("MagicNumber")
@Composable
private fun getFindingsDescriptionModel() = TextFieldUiModel(
    identifier = Random(100).toString(),
    label = stringResource(id = R.string.findings_description_label),
    keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Text
    ),
    textStyle = TextStyle.HEADLINE_5,
    charLimit = 600,
    validations = listOf(
        ValidationUiModel(
            regex = "^(?!\\s*$).+",
            message = "El campo no debe estar vacío"
        ),
        ValidationUiModel(
            regex = "^(?!.*[^,.:A-Za-z0-9 A-zÀ-ú\\r\\n].*).+",
            message = "El campo no debe tener caracteres especiales"
        )
    ),
    singleLine = false,
    minLines = DESCRIPTION_INPUT_MIN_LINES,
    arrangement = Arrangement.Center,
    modifier = Modifier.padding(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = 0.dp
    )
)

private fun handleFooterAction(
    uiAction: UiAction,
    viewModel: ReportViewModel
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            AddFindingIdentifier.ADD_FINDING_CANCEL_BUTTON.name -> viewModel.cancelFinding()
            AddFindingIdentifier.ADD_FINDING_SAVE_BUTTON.name -> viewModel.saveFinding()

            AddFindingIdentifier.ADD_FINDING_CANCEL_BANNER.name ->
                viewModel.consumeNavigationEvent()

            AddFindingIdentifier.ADD_FINDING_CONTINUE_BANNER.name ->
                viewModel.navigateBackFromReport()

            ImagesConfirmationIdentifier.IMAGES_CONFIRMATION_CANCEL_BANNER.name ->
                viewModel.consumeNavigationEvent()

            ImagesConfirmationIdentifier.IMAGES_CONFIRMATION_SEND_BANNER.name -> {
                viewModel.confirmFinding()
                viewModel.consumeShownConfirm()
            }
        }
    }
}

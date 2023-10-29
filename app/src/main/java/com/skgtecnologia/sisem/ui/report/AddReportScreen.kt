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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.label.addFilesHint
import com.skgtecnologia.sisem.domain.report.model.AddReportIdentifier
import com.skgtecnologia.sisem.domain.report.model.ImagesConfirmationIdentifier
import com.skgtecnologia.sisem.ui.media.MediaActions
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
import com.valkiria.uicomponents.components.textfield.TextFieldComponent
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.random.Random

private const val DESCRIPTION_INPUT_MIN_LINES = 3

@Suppress("LongParameterList", "LongMethod")
@Composable
fun AddReportScreen(
    viewModel: ReportViewModel,
    modifier: Modifier = Modifier,
    onNavigation: (addReportNavigationModel: NavigationModel?) -> Unit
) {
    val addReportViewModel = hiltViewModel<AddReportViewModel>()
    val uiState = viewModel.uiState
    val addReportUiState = addReportViewModel.uiState

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

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        addReportUiState.screenModel?.header?.let {
            HeaderSection(headerUiModel = it) { uiAction ->
                if (uiAction is HeaderUiAction.GoBack) {
                    viewModel.navigateBackFromReport()
                }
            }

            TextFieldComponent(
                uiModel = getReportTopicModel(),
                validateFields = uiState.validateFields
            ) { inputUiModel ->
                viewModel.topic = inputUiModel.updatedValue
                viewModel.isValidTopic = inputUiModel.fieldValidated
            }

            TextFieldComponent(
                uiModel = getReportDescriptionModel(),
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

            MediaActions(viewModel, false)

            Spacer(modifier = Modifier.weight(1f))
        }

        addReportUiState.screenModel?.footer?.let {
            FooterSection(footerModel = it) { uiAction ->
                handleFooterAction(uiAction, viewModel)
            }
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

    OnBannerHandler(addReportUiState.errorModel) {
        addReportViewModel.handleShownError()
    }

    OnBannerHandler(uiState.infoEvent) {
        viewModel.consumeShownError()
    }

    LocalFocusManager.current.clearFocus()

    OnLoadingHandler(addReportUiState.isLoading, modifier)
    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleFooterAction(
    uiAction: UiAction,
    viewModel: ReportViewModel
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            AddReportIdentifier.ADD_REPORT_ENTRY_CANCEL_BUTTON.name -> viewModel.cancelReport()
            AddReportIdentifier.SEND_REPORT_ENTRY_SEND_BUTTON.name -> viewModel.saveReport()

            AddReportIdentifier.ADD_REPORT_CANCEL_BANNER.name -> viewModel.consumeNavigationEvent()
            AddReportIdentifier.ADD_REPORT_CONTINUE_BANNER.name ->
                viewModel.navigateBackFromReport()

            ImagesConfirmationIdentifier.IMAGES_CONFIRMATION_CANCEL_BANNER.name ->
                viewModel.consumeNavigationEvent()

            ImagesConfirmationIdentifier.IMAGES_CONFIRMATION_SEND_BANNER.name -> {
                viewModel.confirmReport()
                viewModel.consumeShownConfirm()
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun getReportTopicModel() = TextFieldUiModel(
    identifier = Random(100).toString(),
    label = stringResource(id = R.string.findings_topic_label),
    keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Text
    ),
    textStyle = TextStyle.HEADLINE_5,
    charLimit = 50,
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
    singleLine = true,
    minLines = 1,
    arrangement = Arrangement.Center,
    modifier = Modifier.padding(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = 0.dp
    )
)

@Suppress("MagicNumber")
@Composable
private fun getReportDescriptionModel() = TextFieldUiModel(
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

package com.skgtecnologia.sisem.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.report.model.AddReportIdentifier
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.banner.OnBannerHandler
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.textfield.TextFieldComponent
import com.valkiria.uicomponents.model.props.TextFieldStyle
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.label.LabelUiModel
import com.valkiria.uicomponents.model.ui.textfield.TextFieldUiModel
import com.valkiria.uicomponents.model.ui.textfield.ValidationUiModel
import kotlin.random.Random

@Suppress("LongParameterList", "LongMethod")
@Composable
fun AddReportScreen(
    viewModel: ReportViewModel,
    role: String,
    modifier: Modifier = Modifier,
    onNavigation: (addReportNavigationModel: NavigationModel?) -> Unit
) {
    val addReportViewModel = hiltViewModel<AddReportViewModel>()
    val uiState = viewModel.uiState
    val addReportUiState = addReportViewModel.uiState

    LaunchedEffect(uiState) {
        when {
            uiState.navigationModel != null && uiState.cancelInfoModel == null -> {
                viewModel.handleNavigation()
                onNavigation(uiState.navigationModel)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        addReportUiState.screenModel?.header?.let {
            HeaderSection(headerModel = it) { uiAction ->
                if (uiAction is HeaderUiAction.GoBack) {
                    viewModel.goBack()
                }
            }
        }

        LabelComponent(uiModel = getRecordNewsTopicModel())

        TextFieldComponent(
            uiModel = getFindingsTopicModel(),
            validateFields = uiState.validateFields
        ) { _, updatedValue, fieldValidated ->
            viewModel.topic = updatedValue
            viewModel.isValidTopic = fieldValidated
        }

        LabelComponent(uiModel = getRecordNewsDescriptionModel())

        TextFieldComponent(
            uiModel = getFindingsDescriptionModel(),
            validateFields = uiState.validateFields
        ) { _, updatedValue, fieldValidated ->
            viewModel.description = updatedValue
            viewModel.isValidDescription = fieldValidated
        }

        LabelComponent(uiModel = getFindingsAddFilesModel())

        MediaActions(viewModel, role)

        Spacer(modifier = Modifier.weight(1f))

        addReportUiState.screenModel?.footer?.let {
            FooterSection(footerModel = it) { uiAction ->
                handleFooterAction(uiAction, viewModel)
            }
        }
    }

    OnBannerHandler(uiState.cancelInfoModel) {
        handleFooterAction(it, viewModel)
    }

    OnBannerHandler(addReportUiState.errorModel) {
        addReportViewModel.handleShownError()
    }

    OnBannerHandler(uiState.errorModel) {
        viewModel.handleShownError()
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
            AddReportIdentifier.ADD_REPORT_CANCEL_BANNER.name -> viewModel.goBack()
            AddReportIdentifier.ADD_REPORT_CONTINUE_BANNER.name -> viewModel.handleNavigation()
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun getFindingsTopicModel() = TextFieldUiModel(
    identifier = Random(100).toString(),
    keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Text
    ),
    textStyle = TextStyle.HEADLINE_5,
    validations = listOf(
        ValidationUiModel(
            regex = "^(?!\\s*$).+",
            message = "El campo no debe estar vacío"
        )
    ),
    style = TextFieldStyle.FILLED,
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
private fun getFindingsDescriptionModel() = TextFieldUiModel(
    identifier = Random(100).toString(),
    keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Text
    ),
    textStyle = TextStyle.HEADLINE_5,
    validations = listOf(
        ValidationUiModel(
            regex = "^(?!\\s*$).+",
            message = "El campo no debe estar vacío"
        )
    ),
    style = TextFieldStyle.FILLED,
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

@Composable
private fun getRecordNewsTopicModel() = LabelUiModel(
    text = stringResource(id = R.string.record_news_topic_label),
    textStyle = TextStyle.HEADLINE_3,
    arrangement = Arrangement.Start,
    modifier = Modifier.padding(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = 0.dp
    )
)

@Composable
private fun getRecordNewsDescriptionModel() = LabelUiModel(
    text = stringResource(id = R.string.record_news_description_label),
    textStyle = TextStyle.HEADLINE_3,
    arrangement = Arrangement.Start,
    modifier = Modifier.padding(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = 0.dp
    )
)

@Composable
private fun getFindingsAddFilesModel() = LabelUiModel(
    text = stringResource(id = R.string.findings_add_files_label),
    textStyle = TextStyle.HEADLINE_3,
    arrangement = Arrangement.Start,
    modifier = Modifier.padding(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = 0.dp
    )
)

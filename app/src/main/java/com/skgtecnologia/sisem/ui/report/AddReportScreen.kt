package com.skgtecnologia.sisem.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
import com.valkiria.uicomponents.components.errorbanner.OnErrorHandler
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.textfield.TextFieldComponent
import com.valkiria.uicomponents.model.props.TabletWidth
import com.valkiria.uicomponents.model.props.TextFieldStyle
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.label.LabelUiModel
import com.valkiria.uicomponents.model.ui.textfield.TextFieldUiModel
import com.valkiria.uicomponents.model.ui.textfield.ValidationUiModel
import kotlin.random.Random

@Suppress("LongParameterList", "LongMethod")
@Composable
fun AddReportScreen(
    reportViewModel: ReportViewModel,
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    role: String,
    onNavigation: (addReportNavigationModel: NavigationModel?) -> Unit,
    onCancel: () -> Unit
) {
    val viewModel = hiltViewModel<AddReportViewModel>()
    val uiState = viewModel.uiState
    val uiReportState = reportViewModel.uiState

    LaunchedEffect(uiReportState) {
        when {
            uiReportState.navigationModel != null -> {
                reportViewModel.handleNavigation()
                onNavigation(uiReportState.navigationModel)
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
        uiState.screenModel?.header?.let {
            HeaderSection(headerModel = it) { uiAction ->
                if (uiAction is HeaderUiAction.GoBack) {
                    reportViewModel.goBack()
                }
            }
        }

        LabelComponent(uiModel = getRecordNewsTopicModel())

        TextFieldComponent(
            uiModel = getFindingsTopicModel()
        ) { _, updatedValue, fieldValidated ->
            reportViewModel.topic = updatedValue
            reportViewModel.isValidTopic = fieldValidated
        }

        LabelComponent(uiModel = getRecordNewsDescriptionModel())

        TextFieldComponent(
            uiModel = getFindingsDescriptionModel()
        ) { _, updatedValue, fieldValidated ->
            reportViewModel.description = updatedValue
            reportViewModel.isValidDescription = fieldValidated
        }

        LabelComponent(uiModel = getFindingsAddFilesModel())

        MediaActions(reportViewModel, role)

        Spacer(modifier = Modifier.weight(1f))

        uiState.screenModel?.footer?.let {
            FooterSection(footerModel = it) { uiAction ->
                handleFooterUiAction(uiAction, reportViewModel, onCancel)
            }
        }

        OnErrorHandler(uiState.errorModel) {
            viewModel.handleShownError()
        }

        OnErrorHandler(
            uiReportState.errorModel,
            onAction = {
                reportViewModel.handleShownError()
            }
        )

        OnLoadingHandler(uiState.isLoading, modifier)
        OnLoadingHandler(uiReportState.isLoading, modifier)
    }
}

private fun handleFooterUiAction(
    uiAction: UiAction,
    viewModel: ReportViewModel,
    onCancel: () -> Unit
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            AddReportIdentifier.ADD_REPORT_ENTRY_CANCEL_BUTTON.name -> onCancel()
            AddReportIdentifier.SEND_REPORT_ENTRY_SEND_BUTTON.name -> viewModel.saveRecordNews()
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

package com.skgtecnologia.sisem.ui.report

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.button.ImageButtonView
import com.valkiria.uicomponents.components.errorbanner.OnErrorHandler
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.textfield.TextFieldComponent
import com.valkiria.uicomponents.model.props.ButtonSize
import com.valkiria.uicomponents.model.props.ButtonStyle
import com.valkiria.uicomponents.model.props.TabletWidth
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.button.ImageButtonUiModel
import com.valkiria.uicomponents.model.ui.button.OnClick
import com.valkiria.uicomponents.model.ui.label.LabelUiModel
import com.valkiria.uicomponents.model.ui.textfield.TextFieldUiModel
import com.valkiria.uicomponents.model.ui.textfield.ValidationUiModel
import timber.log.Timber
import kotlin.random.Random

const val DESCRIPTION_INPUT_MIN_LINES = 3

@Suppress("LongMethod")
@Composable
fun FindingsScreen(
    viewModel: ReportViewModel,
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (findingsNavigationModel: NavigationModel?) -> Unit
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        when {
            uiState.navigationModel != null -> {
                viewModel.handleNavigation()
                onNavigation(uiState.navigationModel)
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
        HeaderSection(
            headerModel = getFindingsHeaderModel()
        ) { uiAction ->
            if (uiAction is HeaderUiAction.GoBack) {
                viewModel.goBack()
            }
        }

        TextFieldComponent(
            uiModel = getFindingsDescriptionModel()
        ) { id, updatedValue, fieldValidated ->
            Timber.d("Finish this")
        }

        LabelComponent(
            uiModel = getFindingsAddFilesModel()
        )

        MediaActions(viewModel)

        Spacer(modifier = Modifier.weight(1f))

        FooterSection(
            footerModel = getFindingsFooterModel(),
            modifier = Modifier.padding(bottom = 20.dp)
        ) { uiAction ->
            handleFooterAction(uiAction, viewModel) {
                viewModel.goBack()
            }
        }
    }

    OnErrorHandler(uiState.errorModel) {
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

@Composable
private fun getFindingsHeaderModel() = HeaderModel(
    title = TextModel(
        stringResource(id = R.string.findings_title),
        TextStyle.HEADLINE_1
    ),
    subtitle = TextModel(
        stringResource(R.string.findings_subtitle),
        TextStyle.HEADLINE_5
    ),
    leftIcon = stringResource(R.string.findings_left_icon),
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)

@Suppress("MagicNumber")
@Composable
private fun getFindingsDescriptionModel() = TextFieldUiModel(
    identifier = Random(100).toString(),
    label = stringResource(id = R.string.findings_input_label),
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

@Composable
fun MediaActions(viewModel: ReportViewModel, role: String? = null) {
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> viewModel.updateSelectedImages(uris, role) }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
            ),
        horizontalArrangement = Arrangement.Start
    ) {
        ImageButtonView(
            uiModel = ImageButtonUiModel(
                iconResId = com.valkiria.uicomponents.R.drawable.ic_camera,
                label = stringResource(id = R.string.findings_take_picture_label),
                textStyle = TextStyle.HEADLINE_6,
                alignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
            )
        ) {
            viewModel.showCamera()
        }

        ImageButtonView(
            uiModel = ImageButtonUiModel(
                iconResId = com.valkiria.uicomponents.R.drawable.ic_image,
                label = stringResource(id = R.string.findings_select_pictures),
                textStyle = TextStyle.HEADLINE_6,
                alignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
            )
        ) {
            multiplePhotoPickerLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    }
}

private fun getFindingsFooterModel() = FooterModel(
    leftButton = ButtonModel(
        identifier = "FINDING_CANCEL_BUTTON",
        label = "CANCELAR",
        style = ButtonStyle.LOUD,
        textStyle = TextStyle.HEADLINE_4,
        onClick = OnClick.DISMISS,
        size = ButtonSize.DEFAULT,
        arrangement = Arrangement.Center,
        modifier = Modifier
    ),
    rightButton = ButtonModel(
        identifier = "FINDING_SAVE_BUTTON",
        label = "GUARDAR",
        style = ButtonStyle.LOUD,
        textStyle = TextStyle.HEADLINE_4,
        onClick = OnClick.DISMISS,
        size = ButtonSize.DEFAULT,
        arrangement = Arrangement.Center,
        modifier = Modifier
    )
)

private fun handleFooterAction(
    uiAction: UiAction,
    viewModel: ReportViewModel,
    onCancel: () -> Unit
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            "FINDING_CANCEL_BUTTON" -> onCancel()
            "FINDING_SAVE_BUTTON" -> viewModel.saveFinding()
        }
    }
}

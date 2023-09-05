package com.skgtecnologia.sisem.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.skgtecnologia.sisem.ui.navigation.model.ImageSelectionNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.bricks.button.ImageButtonView
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.model.ui.label.LabelUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldComponent
import com.valkiria.uicomponents.model.ui.textfield.TextFieldUiModel
import com.valkiria.uicomponents.model.ui.textfield.ValidationUiModel
import com.valkiria.uicomponents.model.props.TabletWidth
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.button.ImageButtonUiModel
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
            uiState.onGoBack -> {
                viewModel.handleGoBack()
                onNavigation(ImageSelectionNavigationModel(goBack = true))
            }

            uiState.onShowCamera -> {
                viewModel.handleShowCamera()
                onNavigation(ImageSelectionNavigationModel(showCamera = true))
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

        ActionsGrid(viewModel)
    }
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
fun ActionsGrid(viewModel: ReportViewModel) {
    val actionItems = listOf(
        com.valkiria.uicomponents.R.drawable.ic_camera to R.string.findings_take_picture_label,
        com.valkiria.uicomponents.R.drawable.ic_image to R.string.findings_select_pictures
    )
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        items(actionItems) { image ->
            ImageButtonView(
                uiModel = ImageButtonUiModel(
                    iconResId = image.first,
                    label = stringResource(id = image.second),
                    textStyle = TextStyle.HEADLINE_6,
                    arrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
                        .height(100.dp)
                )
            ) {
                viewModel.showCamera()
            }
        }
    }
}

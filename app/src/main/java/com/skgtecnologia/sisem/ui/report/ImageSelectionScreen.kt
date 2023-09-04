package com.skgtecnologia.sisem.ui.report

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.skgtecnologia.sisem.ui.navigation.model.ImageSelectionNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.props.TabletWidth
import com.valkiria.uicomponents.props.TextStyle

const val PHOTO_GRID_ROWS = 3

@Suppress("LongMethod")
@Composable
fun ImageSelectionScreen(
    viewModel: ReportViewModel,
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (imageSelectionNavigationModel: NavigationModel?) -> Unit
) {
    val uiState = viewModel.uiState

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> viewModel.updateSelectedImages(uris) }
    )

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
            headerModel = getImageSelectionHeaderModel()
        ) { uiAction ->
            if (uiAction is HeaderUiAction.GoBack) {
                viewModel.goBack()
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp, end = 20.dp)
        ) {
            Button(
                onClick = {
                    viewModel.showCamera()
                },
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Text(text = stringResource(id = R.string.image_selection_take_picture))
            }

            Button(
                onClick = {
                    multiplePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            ) {
                Text(text = stringResource(id = R.string.image_selection_select_pictures))
            }
        }

        PhotoGrid(uiState.selectedImageUris)
    }
}

@Composable
private fun getImageSelectionHeaderModel() = HeaderModel(
    title = TextModel(
        stringResource(id = R.string.image_selection_title),
        TextStyle.HEADLINE_1
    ),
    subtitle = TextModel(
        stringResource(R.string.image_selection_subtitle),
        TextStyle.HEADLINE_5
    ),
    leftIcon = stringResource(R.string.image_selection_left_icon),
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)

@Composable
fun PhotoGrid(selectedImageUris: List<Uri>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(PHOTO_GRID_ROWS),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp)
    ) {
        items(selectedImageUris) { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

package com.skgtecnologia.sisem.ui.media

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.ui.report.ReportViewModel
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.bricks.button.ImageButtonUiModel
import com.valkiria.uicomponents.bricks.button.ImageButtonView
import com.valkiria.uicomponents.components.label.TextStyle

@Composable
fun MediaActions(viewModel: ReportViewModel, isFromPreOperational: Boolean) {
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> viewModel.updateSelectedImages(uris, isFromPreOperational) }
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
                identifier = "CAMERA",
                iconResId = R.drawable.ic_camera,
                label = stringResource(
                    id = com.skgtecnologia.sisem.R.string.findings_take_picture_label
                ),
                textStyle = TextStyle.HEADLINE_6,
                alignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
            )
        ) {
            viewModel.showCamera(isFromPreOperational)
        }

        ImageButtonView(
            uiModel = ImageButtonUiModel(
                identifier = "GALLERY",
                iconResId = R.drawable.ic_image,
                label = stringResource(
                    id = com.skgtecnologia.sisem.R.string.findings_select_pictures
                ),
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

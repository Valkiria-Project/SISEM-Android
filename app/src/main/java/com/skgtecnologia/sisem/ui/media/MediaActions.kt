package com.skgtecnologia.sisem.ui.media

import android.net.Uri
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.bricks.button.ImageButtonUiModel
import com.valkiria.uicomponents.bricks.button.ImageButtonView
import com.valkiria.uicomponents.components.label.TextStyle

@Suppress("LongMethod")
@Composable
fun MediaActions(
    hasFileAction: Boolean = false,
    onMediaAction: (mediaAction: MediaAction) -> Unit
) {
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> onMediaAction(MediaAction.Gallery(uris)) }
    )

    val multipleFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uris -> onMediaAction(MediaAction.File(uris)) }
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
                    id = com.skgtecnologia.sisem.R.string.media_action_take_picture_label
                ),
                textStyle = TextStyle.HEADLINE_6,
                size = 81.dp,
                alignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
            )
        ) {
            onMediaAction(MediaAction.Camera)
        }

        ImageButtonView(
            uiModel = ImageButtonUiModel(
                identifier = "GALLERY",
                iconResId = R.drawable.ic_image,
                label = stringResource(
                    id = com.skgtecnologia.sisem.R.string.media_action_select_pictures
                ),
                textStyle = TextStyle.HEADLINE_6,
                size = 81.dp,
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

        if (hasFileAction) {
            ImageButtonView(
                uiModel = ImageButtonUiModel(
                    identifier = "FILE",
                    iconResId = R.drawable.ic_file,
                    label = stringResource(
                        id = com.skgtecnologia.sisem.R.string.media_action_select_files
                    ),
                    textStyle = TextStyle.HEADLINE_6,
                    size = 81.dp,
                    alignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(8.dp)
                )
            ) {
                val mimeTypes = arrayOf("*/*")
                multipleFilePickerLauncher.launch(mimeTypes)
            }
        }
    }
}

sealed class MediaAction {
    data object Camera : MediaAction()
    data class File(val uris: List<Uri>) : MediaAction()
    data class Gallery(val uris: List<Uri>) : MediaAction()
}

@Preview
@Composable
fun MediaActionsPreview() {
    MediaActions(hasFileAction = true) { _ -> }
}

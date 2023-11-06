package com.valkiria.uicomponents.components.media

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.bricks.button.ImageButtonUiModel
import com.valkiria.uicomponents.bricks.button.ImageButtonView
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.media.MediaAction.Camera
import com.valkiria.uicomponents.components.media.MediaAction.File
import com.valkiria.uicomponents.components.media.MediaAction.Gallery

@Suppress("LongMethod")
@Composable
fun MediaActionsComponent(
    uiModel: MediaActionsUiModel,
    onMediaAction: (id: String, mediaAction: MediaAction) -> Unit
) {
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> onMediaAction(uiModel.identifier, Gallery(uris)) }
    )

    val multipleFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uris -> onMediaAction(uiModel.identifier, File(uris)) }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
            ),
        horizontalAlignment = Alignment.Start
    ) {
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
                        id = R.string.media_action_take_picture_label
                    ),
                    textStyle = TextStyle.HEADLINE_6,
                    size = 81.dp,
                    alignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(8.dp)
                )
            ) {
                onMediaAction(uiModel.identifier, Camera)
            }

            ImageButtonView(
                uiModel = ImageButtonUiModel(
                    identifier = "GALLERY",
                    iconResId = R.drawable.ic_image,
                    label = stringResource(
                        id = R.string.media_action_select_pictures
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

            if (uiModel.withinForm) {
                ImageButtonView(
                    uiModel = ImageButtonUiModel(
                        identifier = "FILE",
                        iconResId = R.drawable.ic_file,
                        label = stringResource(
                            id = R.string.media_action_select_files
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

        if (uiModel.withinForm) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                    ),
                horizontalArrangement = Arrangement.Start
            ) {
                LabelComponent(
                    uiModel = LabelUiModel(
                        identifier = "MEDIA_ACTIONS",
                        text = "Archivos adjuntos",
                        textStyle = TextStyle.HEADLINE_2,
                        arrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(14.dp)
                    )
                )
            }

            val selectedMedia = uiModel.selectedMediaUris

            if (selectedMedia.isNotEmpty()) {
                selectedMedia.forEach { uri ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                            ),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        LabelComponent(
                            uiModel = LabelUiModel(
                                identifier = "MEDIA_ACTIONS",
                                text = "$uri",
                                textStyle = TextStyle.HEADLINE_2,
                                arrangement = Arrangement.Start,
                                modifier = Modifier
                                    .padding(14.dp)
                            )
                        )
                    }
                }
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
    Column {
        MediaActionsComponent(uiModel = MediaActionsUiModel(withinForm = true)) { _, _ -> }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(color = Color.White)
        )
        MediaActionsComponent(uiModel = MediaActionsUiModel(withinForm = false)) { _, _ -> }
    }
}

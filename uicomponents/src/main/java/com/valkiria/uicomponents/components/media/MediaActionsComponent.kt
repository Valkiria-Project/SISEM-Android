package com.valkiria.uicomponents.components.media

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.valkiria.uicomponents.components.media.MediaAction.Gallery
import com.valkiria.uicomponents.components.media.MediaAction.MediaFile
import com.valkiria.uicomponents.extensions.storeUriAsFileToCache
import java.io.File
import kotlinx.coroutines.launch

private const val ROUNDED_CORNER_SHAPE_PERCENTAGE = 90

@Suppress("LongMethod")
@Composable
fun MediaActionsComponent(
    uiModel: MediaActionsUiModel,
    listState: LazyListState = rememberLazyListState(),
    mediaActionsIndex: Int = 0,
    onMediaAction: (id: String, mediaAction: MediaAction) -> Unit
) {
    var selectedMedia by remember { mutableStateOf(listOf<File>()) }
    val context = LocalContext.current

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> onMediaAction(uiModel.identifier, Gallery(uris)) }
    )

    val multipleMediaFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uris -> onMediaAction(uiModel.identifier, MediaFile(uris)) }
    )

    LaunchedEffect(uiModel.selectedMediaUris) {
        launch {
            if (uiModel.selectedMediaUris.isNotEmpty()) {
                selectedMedia = uiModel.selectedMediaUris.map { uri ->
                    context.storeUriAsFileToCache(
                        uri
                    )
                }
            }
        }
    }

    LaunchedEffect(selectedMedia) {
        listState.animateScrollToItem(
            index = mediaActionsIndex + 1
        )
    }

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
                    multipleMediaFilePickerLauncher.launch(mimeTypes)
                }
            }
        }

        if (uiModel.withinForm && selectedMedia.isNotEmpty()) {
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

            if (selectedMedia.isNotEmpty()) {
                selectedMedia.forEach { media ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 20.dp
                            ),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        LabelComponent(
                            uiModel = LabelUiModel(
                                identifier = media.absolutePath,
                                text = media.name,
                                textStyle = TextStyle.HEADLINE_6,
                                arrangement = Arrangement.Start,
                                modifier = Modifier
                                    .padding(14.dp)
                                    .border(
                                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                        RoundedCornerShape(ROUNDED_CORNER_SHAPE_PERCENTAGE)
                                    )
                                    .padding(horizontal = 20.dp, vertical = 4.dp)
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
    data class MediaFile(val uris: List<Uri>) : MediaAction()
    data class Gallery(val uris: List<Uri>) : MediaAction()
}

@Preview
@Composable
fun MediaActionsPreview() {
    Column {
        MediaActionsComponent(
            uiModel = MediaActionsUiModel(
                withinForm = true,
                selectedMediaUris = listOf(
                    Uri.parse("10.jpg"),
                    Uri.parse("20.jpg")
                )
            ),
            listState = rememberLazyListState(),
            mediaActionsIndex = 1
        ) { _, _ -> }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(color = Color.White)
        )
        MediaActionsComponent(
            uiModel = MediaActionsUiModel(withinForm = false),
            listState = rememberLazyListState(),
            mediaActionsIndex = 1
        ) { _, _ -> }
    }
}

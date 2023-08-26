package com.skgtecnologia.sisem.ui.imageselection

import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.props.TabletWidth
import com.valkiria.uicomponents.props.TextStyle
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@Suppress("LongMethod")
@Composable
fun ImageSelectionScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier
) {
    var takePicture by remember { mutableStateOf(false) }

    var selectedImageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> selectedImageUris = uris }
    )

    Column(
        modifier = if (isTablet) {
            modifier.width(TabletWidth)
        } else {
            modifier.fillMaxWidth()
        },
    ) {
        HeaderSection(
            headerModel = getImageSelectionHeaderModel()
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp, end = 20.dp)
        ) {
            item {
                Row {
                    Button(
                        onClick = {
                            takePicture = true
                        }
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
            }

            items(selectedImageUris) { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        if (takePicture) {
            ExampleCameraScreen()
        }
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ExampleCameraScreen() {
    val cameraPermissionState: PermissionState =
        rememberPermissionState(android.Manifest.permission.CAMERA)

    LaunchedEffect(key1 = Unit) {
        if (!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    if (cameraPermissionState.status.isGranted) {
        // Permission is granted, we can show the camera preview
        ExampleCameraPreview()
    } else {
        // In this screen you should notify the user that the permission
        // is required and maybe offer a button to start another
        // camera perission request
//        NoCameraPermissionScreen(
//            cameraPermissionState = cameraPermissionState
//        )
    }
}

@Composable
private fun ExampleCameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)
                    cameraController.takePicture(
                        mainExecutor,
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                // Process the captured image here
                            }
                        })
                }
            ) {
                Text(text = "Take photo")
            }
        }
    ) { innerPadding: PaddingValues ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            factory = { context ->
                PreviewView(context).apply {
                    setBackgroundColor(Color.White.toArgb())
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    scaleType = PreviewView.ScaleType.FILL_START
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }.also { previewView ->
                    previewView.controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            },
            onRelease = {
                cameraController.unbind()
            }
        )
    }
}

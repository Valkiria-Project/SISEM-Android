package com.skgtecnologia.sisem.ui.medicalhistory.camera

import android.Manifest
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.ui.commons.utils.CameraUtils
import com.skgtecnologia.sisem.ui.commons.utils.MediaStoreUtils
import com.skgtecnologia.sisem.ui.medicalhistory.view.MedicalHistoryViewNavigationModel
import com.skgtecnologia.sisem.ui.medicalhistory.view.MedicalHistoryViewViewModel
import com.valkiria.uicomponents.bricks.notification.OnNotificationHandler
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.extensions.handleMediaUris
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraViewScreen(
    viewModel: MedicalHistoryViewViewModel,
    modifier: Modifier = Modifier,
    onNavigation: (medicalHistoryNavigationModel: MedicalHistoryViewNavigationModel) -> Unit
) {
    val uiState = viewModel.uiState

    var notificationData by remember { mutableStateOf<NotificationData?>(null) }
    NotificationEventHandler.subscribeNotificationEvent {
        notificationData = it
    }

    val cameraPermissionState: PermissionState =
        rememberPermissionState(Manifest.permission.CAMERA)
    val cameraPermission = cameraPermissionState.status

    LaunchedEffect(uiState) {
        if (!cameraPermission.isGranted && !cameraPermission.shouldShowRationale) {
            cameraPermissionState.launchPermissionRequest()
        }

        if (uiState.navigationModel != null) {
            onNavigation(uiState.navigationModel)
            viewModel.consumeNavigationEvent()
        }
    }

    if (cameraPermission.isGranted) {
        Timber.d("Show Camera")
        CameraViewPreview(viewModel, modifier)

        OnNotificationHandler(notificationData) {
            notificationData = null
            if (it.isDismiss.not()) {
                // TECH-DEBT: Navigate to MapScreen if is type INCIDENT_ASSIGNED
                Timber.d("Navigate to MapScreen")
            }
        }
    } else if (cameraPermission.shouldShowRationale) {
        Timber.d("Show rationale")
    }
}

@Composable
private fun CameraViewPreview(
    viewModel: MedicalHistoryViewViewModel,
    modifier: Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraUtils = CameraUtils(context)
    val mediaStoreUtils = MediaStoreUtils(context)

    LaunchedEffect(cameraSelector) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = modifier.fillMaxSize())

        IconButton(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .size(92.dp),
            onClick = {
                imageCapture.takePicture(
                    cameraUtils.getOutputOptions(),
                    Executors.newSingleThreadExecutor(),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            Timber.e("Photo capture failed: ${exc.message}")
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            lifecycleOwner.lifecycleScope.launch {
                                val savedUri = output.savedUri
                                Timber.d(
                                    """"Photo capture succeeded: $savedUri with file name 
                                        |${mediaStoreUtils.getLatestImageFilename()}
                                        """.trimMargin()
                                )

                                if (savedUri != null) {
                                    val mediaItems = context.handleMediaUris(
                                        listOf(savedUri.toString()),
                                        viewModel.uiState.operationConfig?.maxFileSizeKb
                                    )

                                    viewModel.onPhotoTaken(mediaItems.first())
                                }
                            }
                        }
                    }
                )
            },
            content = {
                Icon(
                    imageVector = Icons.Sharp.Lens,
                    contentDescription = stringResource(id = R.string.camera_take_picture),
                    tint = Color.White,
                    modifier = Modifier
                        .size(92.dp)
                        .padding(1.dp)
                        .border(1.dp, Color.White, CircleShape)
                )
            }
        )
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

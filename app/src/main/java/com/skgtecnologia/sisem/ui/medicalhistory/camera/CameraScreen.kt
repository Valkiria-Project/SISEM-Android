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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.ui.commons.utils.CameraUtils
import com.skgtecnologia.sisem.ui.commons.utils.MediaStoreUtils
import com.skgtecnologia.sisem.ui.medicalhistory.MedicalHistoryViewModel
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    viewModel: MedicalHistoryViewModel,
    onNavigation: (reportNavigationModel: NavigationModel?) -> Unit
) {
    val uiState = viewModel.uiState

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
        CameraPreview(viewModel, modifier)
    } else if (cameraPermission.shouldShowRationale) {
        Timber.d("Show rationale")
    }
}

@Composable
private fun CameraPreview(
    viewModel: MedicalHistoryViewModel,
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
                            val savedUri = output.savedUri
                            lifecycleOwner.lifecycleScope.launch {
                                Timber.d(
                                    """"Photo capture succeeded: $savedUri with file name 
                                        |${mediaStoreUtils.getLatestImageFilename()}""".trimMargin()
                                )
                            }

                            if (savedUri != null) {
                                viewModel.onPhotoTaken(savedUri)
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

package com.skgtecnologia.sisem.ui.authcards.face

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.commons.biometric.rotateTo
import timber.log.Timber
import java.util.concurrent.Executors
import androidx.compose.ui.geometry.Size as ComposeSize

private const val CAMERA_WIDTH = 640
private const val CAMERA_HEIGHT = 480
private const val OVAL_WIDTH_FRACTION = 0.65f
private const val OVAL_HEIGHT_RATIO = 1.35f
private const val OVERLAY_ALPHA = 0.55f
private const val BORDER_WIDTH_DP = 3
private const val ENROLL_SUCCESS_DELAY_MS = 1_500L
private const val MIN_FACE_SIZE = 0.25f

@Suppress("MagicNumber")
private val GREEN = Color(0xFF4CAF50)

@Suppress("MagicNumber")
private val RED = Color(0xFFF44336)

@Suppress("MagicNumber")
private val YELLOW = Color(0xFFFFEB3B)

@SuppressLint("UnsafeOptInUsageError")
@Suppress("LongMethod")
@Composable
fun FaceCameraScreen(
    onNavigate: (FaceNavigationModel) -> Unit,
    onBack: () -> Unit,
    viewModel: FaceCameraViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsState()

    val detector = remember {
        FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                // Required for liveness: provides eye-open probability for blink detection
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .setMinFaceSize(MIN_FACE_SIZE)
                .build()
        )
    }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    LaunchedEffect(state) {
        when (val s = state) {
            is FaceCameraState.Success -> onNavigate(s.navigationModel)

            // After enrollment show success briefly then go back automatically
            FaceCameraState.Enrolled -> kotlinx.coroutines.delay(ENROLL_SUCCESS_DELAY_MS).also { onBack() }

            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        CameraPreview(
            lifecycleOwner = lifecycleOwner,
            detector = detector,
            executor = analysisExecutor,
            onFaceDetected = viewModel::onFaceDetected
        )
        FaceOvalOverlay(state = state)
        FaceStatusControls(
            state = state,
            mode = viewModel.mode,
            onRetry = viewModel::reset,
            onBack = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
        )
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
private fun CameraPreview(
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    detector: com.google.mlkit.vision.face.FaceDetector,
    executor: java.util.concurrent.Executor,
    onFaceDetected: (com.google.mlkit.vision.face.Face, Bitmap) -> Unit
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }
                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(CAMERA_WIDTH, CAMERA_HEIGHT))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                imageAnalysis.setAnalyzer(executor) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val rotation = imageProxy.imageInfo.rotationDegrees
                        // Capture bitmap before async detection so pixel data remains
                        // valid after imageProxy.close(). rotateTo() aligns the bitmap
                        // to ML Kit's coordinate space (same rotation applied internally).
                        val frameBitmap = imageProxy.toBitmap().rotateTo(rotation)
                        val inputImage = InputImage.fromMediaImage(mediaImage, rotation)
                        detector.process(inputImage)
                            .addOnSuccessListener { faces ->
                                faces.firstOrNull()?.let { onFaceDetected(it, frameBitmap) }
                            }
                            .addOnFailureListener { Timber.w(it, "Face detection error") }
                            .addOnCompleteListener { imageProxy.close() }
                    } else {
                        imageProxy.close()
                    }
                }
                runCatching {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_FRONT_CAMERA,
                        preview,
                        imageAnalysis
                    )
                }.onFailure { Timber.e(it, "Camera bind failed") }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        }
    )
}

@Composable
private fun FaceOvalOverlay(state: FaceCameraState) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val ovalW = size.width * OVAL_WIDTH_FRACTION
        val ovalH = ovalW * OVAL_HEIGHT_RATIO
        val left = (size.width - ovalW) / 2f
        val top = (size.height - ovalH) / 2f
        drawRect(color = Color.Black.copy(alpha = OVERLAY_ALPHA))
        drawOval(
            color = Color.Black,
            topLeft = Offset(left, top),
            size = ComposeSize(ovalW, ovalH),
            blendMode = BlendMode.Clear
        )
        val borderColor = when (state) {
            is FaceCameraState.Enrolling, FaceCameraState.Processing,
            is FaceCameraState.Success, FaceCameraState.Enrolled -> GREEN

            is FaceCameraState.NoMatch -> RED

            else -> Color.White
        }
        drawOval(
            color = borderColor,
            topLeft = Offset(left, top),
            size = ComposeSize(ovalW, ovalH),
            style = Stroke(width = BORDER_WIDTH_DP.dp.toPx())
        )
    }
}

@Composable
@Suppress("UnusedParameter")
private fun FaceStatusControls(
    state: FaceCameraState,
    mode: FaceCameraMode,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        when (val s = state) {
            FaceCameraState.Scanning -> {
                StatusText(stringResource(R.string.face_camera_verify_hint))
            }

            is FaceCameraState.AwaitingLiveness -> {
                val instruction = when (s.challenge) {
                    LivenessChallenge.BLINK -> stringResource(R.string.face_liveness_blink)
                    LivenessChallenge.TURN_LEFT -> stringResource(R.string.face_liveness_turn_left)
                    LivenessChallenge.TURN_RIGHT -> stringResource(R.string.face_liveness_turn_right)
                }
                StatusText(instruction, YELLOW)
                Text(
                    text = "${s.secondsLeft}s",
                    color = YELLOW.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            is FaceCameraState.Enrolling -> {
                val stepText = when (s.step) {
                    EnrollmentStep.FRONTAL -> stringResource(R.string.face_camera_step_frontal)
                    EnrollmentStep.TURN_LEFT -> stringResource(R.string.face_camera_step_left)
                    EnrollmentStep.TURN_RIGHT -> stringResource(R.string.face_camera_step_right)
                }
                StatusText(stepText)
            }

            FaceCameraState.Processing -> {
                CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = Color.White
            )
            }

            FaceCameraState.Enrolled -> {
                StatusText(stringResource(R.string.face_camera_enrolled), GREEN)
            }

            is FaceCameraState.NoMatch -> {
                StatusText(s.message, RED)
                TextButton(onClick = onRetry) {
                    Text(stringResource(R.string.face_camera_retry), color = Color.White)
                }
            }

            is FaceCameraState.Success -> {
                StatusText(
                stringResource(R.string.face_camera_success, s.username), GREEN
            )
            }
        }
        TextButton(onClick = onBack, modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = stringResource(R.string.face_camera_use_password),
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun StatusText(text: String, color: Color = Color.White) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 32.dp)
    )
}

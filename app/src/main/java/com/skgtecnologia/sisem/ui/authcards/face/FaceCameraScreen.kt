@file:Suppress("TooManyFunctions")

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
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import timber.log.Timber
import java.util.concurrent.Executors
import androidx.compose.ui.geometry.Size as ComposeSize

private const val CAMERA_WIDTH = 640
private const val CAMERA_HEIGHT = 480
private const val OVAL_WIDTH_FRACTION = 0.65f
private const val OVAL_HEIGHT_RATIO = 1.35f
private const val OVERLAY_ALPHA = 0.55f
private const val BORDER_WIDTH_DP = 3
private const val MIN_FACE_SIZE = 0.25f
private const val BRACKET_MARGIN_FRACTION = 0.02f
private const val BRACKET_ARM_FRACTION = 0.18f
private const val BRACKET_WIDTH_DP = 4
private const val DASH_ON = 22f
private const val DASH_OFF = 18f
private const val STATUS_CIRCLE_SIZE_DP = 168
private const val STATUS_LOADER_SIZE_DP = 48
private const val LOADING_CIRCLE_ALPHA = 0.45f

@Suppress("MagicNumber")
private val GREEN = Color(0xFF4CAF50)

@Suppress("MagicNumber")
private val RED = Color(0xFFF44336)

@Suppress("MagicNumber")
private val YELLOW = Color(0xFFFFEB3B)

@Suppress("MagicNumber")
private val BLUE = Color(0xFF2D9CDB)

private enum class StatusCircleKind { LOADING, SUCCESS, FAILURE }

@SuppressLint("UnsafeOptInUsageError")
@Suppress("LongMethod")
@Composable
fun FaceCameraScreen(
    onVerified: (username: String) -> Unit,
    onEnrolled: () -> Unit,
    onBack: () -> Unit,
    viewModel: FaceCameraViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsState()
    val guidance by viewModel.guidance.collectAsState()
    val bannerModel by viewModel.banner.collectAsState()

    val detector = remember {
        FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .setMinFaceSize(MIN_FACE_SIZE)
                .build()
        )
    }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        CameraPreview(
            lifecycleOwner = lifecycleOwner,
            detector = detector,
            executor = analysisExecutor,
            onFaceDetected = viewModel::onFaceDetected
        )
        FaceScannerOverlay(state = state)
        state.toStatusCircleKind()?.let { StatusCircleOverlay(kind = it) }
        ScannerHeader()
        FaceStatusControls(
            state = state,
            guidance = guidance,
            mode = viewModel.mode,
            actions = FaceStatusActions(onRetry = viewModel::reset, onBack = onBack),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
        )
    }

    OnBannerHandler(uiModel = bannerModel) {
        viewModel.consumeBanner()
        when (val s = state) {
            is FaceCameraState.Success -> onVerified(s.username)
            FaceCameraState.Enrolled -> onEnrolled()
            is FaceCameraState.NoMatch -> viewModel.reset()
            else -> Unit
        }
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
private fun FaceScannerOverlay(state: FaceCameraState) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val ovalW = size.width * OVAL_WIDTH_FRACTION
        val ovalH = ovalW * OVAL_HEIGHT_RATIO
        val left = (size.width - ovalW) / 2f
        val top = (size.height - ovalH) / 2f

        drawRect(color = Color.Black.copy(alpha = OVERLAY_ALPHA))
        if (state.showsPositioningGuides()) {
            drawOval(
                color = Color.Black,
                topLeft = Offset(left, top),
                size = ComposeSize(ovalW, ovalH),
                blendMode = BlendMode.Clear
            )
            drawOval(
                color = BLUE,
                topLeft = Offset(left, top),
                size = ComposeSize(ovalW, ovalH),
                style = Stroke(
                    width = BORDER_WIDTH_DP.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(DASH_ON, DASH_OFF))
                )
            )
        }
        drawCornerBrackets(left, top, ovalW, ovalH)
    }
}

private fun DrawScope.drawCornerBrackets(left: Float, top: Float, ovalW: Float, ovalH: Float) {
    val margin = size.width * BRACKET_MARGIN_FRACTION
    val l = left - margin
    val t = top - margin
    val r = left + ovalW + margin
    val b = top + ovalH + margin
    val arm = ovalW * BRACKET_ARM_FRACTION
    val stroke = BRACKET_WIDTH_DP.dp.toPx()
    drawLine(BLUE, Offset(l, t), Offset(l + arm, t), stroke, StrokeCap.Round)
    drawLine(BLUE, Offset(l, t), Offset(l, t + arm), stroke, StrokeCap.Round)
    drawLine(BLUE, Offset(r, t), Offset(r - arm, t), stroke, StrokeCap.Round)
    drawLine(BLUE, Offset(r, t), Offset(r, t + arm), stroke, StrokeCap.Round)
    drawLine(BLUE, Offset(l, b), Offset(l + arm, b), stroke, StrokeCap.Round)
    drawLine(BLUE, Offset(l, b), Offset(l, b - arm), stroke, StrokeCap.Round)
    drawLine(BLUE, Offset(r, b), Offset(r - arm, b), stroke, StrokeCap.Round)
    drawLine(BLUE, Offset(r, b), Offset(r, b - arm), stroke, StrokeCap.Round)
}

@Composable
private fun BoxScope.StatusCircleOverlay(kind: StatusCircleKind) {
    Box(
        modifier = Modifier.align(Alignment.Center).size(STATUS_CIRCLE_SIZE_DP.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            when (kind) {
                StatusCircleKind.LOADING -> {
                    drawCircle(BLUE.copy(alpha = LOADING_CIRCLE_ALPHA), radius, center)
                }

                StatusCircleKind.SUCCESS -> {
                    drawCircle(BLUE, radius, center)
                    drawCheckMark(center, radius)
                }

                StatusCircleKind.FAILURE -> {
                    drawCircle(RED, radius, center)
                    drawCrossMark(center, radius)
                }
            }
        }
        if (kind == StatusCircleKind.LOADING) {
            CircularProgressIndicator(
                modifier = Modifier.size(STATUS_LOADER_SIZE_DP.dp),
                color = Color.White
            )
        }
    }
}

@Suppress("MagicNumber")
private fun DrawScope.drawCheckMark(center: Offset, radius: Float) {
    val stroke = radius * 0.14f
    val p1 = Offset(center.x - radius * 0.36f, center.y + radius * 0.02f)
    val p2 = Offset(center.x - radius * 0.08f, center.y + radius * 0.30f)
    val p3 = Offset(center.x + radius * 0.40f, center.y - radius * 0.26f)
    drawLine(Color.White, p1, p2, stroke, StrokeCap.Round)
    drawLine(Color.White, p2, p3, stroke, StrokeCap.Round)
}

@Suppress("MagicNumber")
private fun DrawScope.drawCrossMark(center: Offset, radius: Float) {
    val stroke = radius * 0.14f
    val d = radius * 0.30f
    drawLine(
        Color.White,
        Offset(center.x - d, center.y - d),
        Offset(center.x + d, center.y + d),
        stroke,
        StrokeCap.Round
    )
    drawLine(
        Color.White,
        Offset(center.x - d, center.y + d),
        Offset(center.x + d, center.y - d),
        stroke,
        StrokeCap.Round
    )
}

@Composable
private fun BoxScope.ScannerHeader() {
    Column(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .statusBarsPadding()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.face_camera_scanner_title),
            color = Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.face_camera_scanner_subtitle),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

private class FaceStatusActions(
    val onRetry: () -> Unit,
    val onBack: () -> Unit
)

@Composable
private fun FaceStatusControls(
    state: FaceCameraState,
    guidance: FaceGuidance,
    mode: FaceCameraMode,
    actions: FaceStatusActions,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        when (val s = state) {
            FaceCameraState.Scanning -> {
                PositioningGuidanceText(guidance)
            }

            is FaceCameraState.AwaitingLiveness -> {
                if (guidance == FaceGuidance.GOOD) {
                    LivenessInstruction(s)
                } else {
                    PositioningGuidanceText(guidance)
                }
            }

            is FaceCameraState.Enrolling -> {
                StatusText(
                when (s.step) {
                    EnrollmentStep.FRONTAL -> stringResource(R.string.face_camera_step_frontal)
                    EnrollmentStep.TURN_LEFT -> stringResource(R.string.face_camera_step_left)
                    EnrollmentStep.TURN_RIGHT -> stringResource(R.string.face_camera_step_right)
                }
            )
            }

            FaceCameraState.Processing -> {
                StatusText(
                if (mode == FaceCameraMode.ENROLL) {
                    stringResource(R.string.face_camera_loading_enroll)
                } else {
                    stringResource(R.string.face_camera_detected)
                }
            )
            }

            FaceCameraState.Enrolled -> {
                StatusText(stringResource(R.string.face_camera_enroll_success), BLUE)
            }

            is FaceCameraState.Success -> {
                StatusText(stringResource(R.string.face_camera_success, s.username), BLUE)
            }

            is FaceCameraState.NoMatch -> {
                StatusText(s.message, RED)
                TextButton(onClick = actions.onRetry) {
                    Text(stringResource(R.string.face_camera_retry), color = Color.White)
                }
            }
        }
        TextButton(onClick = actions.onBack, modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = if (mode == FaceCameraMode.ENROLL) {
                    stringResource(R.string.face_camera_cancel)
                } else {
                    stringResource(R.string.face_camera_use_password)
                },
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun PositioningGuidanceText(guidance: FaceGuidance) {
    when (guidance) {
        FaceGuidance.NO_FACE -> {
            StatusText(stringResource(R.string.face_camera_guidance_no_face))
        }

        FaceGuidance.TOO_FAR -> {
            StatusText(stringResource(R.string.face_camera_guidance_move_closer), YELLOW)
        }

        FaceGuidance.TOO_CLOSE -> {
            StatusText(stringResource(R.string.face_camera_guidance_move_away), YELLOW)
        }

        FaceGuidance.GOOD -> {
            StatusText(stringResource(R.string.face_camera_guidance_perfect), GREEN)
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun LivenessInstruction(state: FaceCameraState.AwaitingLiveness) {
    val instruction = when (state.challenge) {
        LivenessChallenge.BLINK -> stringResource(R.string.face_liveness_blink)
        LivenessChallenge.TURN_LEFT -> stringResource(R.string.face_liveness_turn_left)
        LivenessChallenge.TURN_RIGHT -> stringResource(R.string.face_liveness_turn_right)
    }
    StatusText(instruction, YELLOW)
    Text(
        text = "${state.secondsLeft}s",
        color = YELLOW.copy(alpha = 0.8f),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(top = 4.dp)
    )
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

private fun FaceCameraState.showsPositioningGuides(): Boolean = when (this) {
    FaceCameraState.Scanning,
    is FaceCameraState.AwaitingLiveness,
    is FaceCameraState.Enrolling -> true

    else -> false
}

private fun FaceCameraState.toStatusCircleKind(): StatusCircleKind? = when (this) {
    FaceCameraState.Processing -> StatusCircleKind.LOADING
    FaceCameraState.Enrolled, is FaceCameraState.Success -> StatusCircleKind.SUCCESS
    is FaceCameraState.NoMatch -> StatusCircleKind.FAILURE
    else -> null
}

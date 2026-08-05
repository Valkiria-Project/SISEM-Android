package com.skgtecnologia.sisem.ui.authcards.face

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.mlkit.vision.face.Face
import com.skgtecnologia.sisem.commons.biometric.FaceCredentialStore
import com.skgtecnologia.sisem.commons.biometric.FaceEmbeddingHelper
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

enum class FaceCameraMode { ENROLL, VERIFY }

private const val FRONTAL_MIN = -12f
private const val FRONTAL_MAX = 12f
private const val TURN_MIN = 18f
private const val TURN_MAX = 55f

/** Enrollment steps — each requires a specific head angle. */
@Suppress("MagicNumber")
enum class EnrollmentStep(val eulerYMin: Float, val eulerYMax: Float) {
    FRONTAL(FRONTAL_MIN, FRONTAL_MAX),
    TURN_LEFT(TURN_MIN, TURN_MAX),
    TURN_RIGHT(-TURN_MAX, -TURN_MIN);

    fun isActive(eulerY: Float) = eulerY in eulerYMin..eulerYMax

    fun next(): EnrollmentStep? = when (this) {
        FRONTAL -> TURN_LEFT
        TURN_LEFT -> TURN_RIGHT
        TURN_RIGHT -> null
    }
}

sealed interface FaceCameraState {
    data object Scanning : FaceCameraState
    data class Enrolling(val step: EnrollmentStep, val captured: Int, val total: Int) : FaceCameraState
    data object Processing : FaceCameraState
    data class Success(val username: String, val navigationModel: FaceNavigationModel) : FaceCameraState
    data class NoMatch(val message: String) : FaceCameraState
    data object Enrolled : FaceCameraState
}

data class FaceNavigationModel(
    val isAdmin: Boolean = false,
    val isTurnComplete: Boolean = true,
    val requiresPreOperational: Boolean = false,
    val preOperationRole: OperationRole? = null,
    val requiresDeviceAuth: Boolean = false
)

private const val TOTAL_ENROLLMENT_STEPS = 3
private const val MIN_STABLE_FRAMES = 6
private const val LOG_SAMPLE = 8

@HiltViewModel
class FaceCameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val faceCredentialStore: FaceCredentialStore
) : ViewModel() {

    private val route = savedStateHandle.toRoute<AuthRoute.FaceCameraRoute>()
    val mode: FaceCameraMode = if (route.mode == "ENROLL") FaceCameraMode.ENROLL else FaceCameraMode.VERIFY
    val enrollUsername: String? = route.username.ifBlank { null }
    private val loggedOutRole: String = route.loggedOutRole

    private val _state = MutableStateFlow<FaceCameraState>(
        if (mode == FaceCameraMode.ENROLL) {
            FaceCameraState.Enrolling(EnrollmentStep.FRONTAL, 0, TOTAL_ENROLLMENT_STEPS)
        } else {
            FaceCameraState.Scanning
        }
    )
    val state: StateFlow<FaceCameraState> = _state

    private var isProcessing = false
    private val capturedEmbeddings = mutableListOf<FloatArray>()
    private var currentStep = EnrollmentStep.FRONTAL

    // Stability: face must hold the correct position for this many consecutive
    // frames before a capture is accepted — prevents premature or blurry captures.
    private var stableFrameCount = 0
    private val minStableFrames = MIN_STABLE_FRAMES

    // ── Main entry point ─────────────────────────────────────────────────────

    @Suppress("ReturnCount")
    fun onFaceDetected(face: Face) {
        if (isProcessing) return

        // 1. Reject if the face is too far from the camera (bounding box too small)
        if (face.boundingBox.width() < FaceEmbeddingHelper.MIN_FACE_PX) {
            stableFrameCount = 0
            return
        }

        val embedding = FaceEmbeddingHelper.extractEmbedding(face) ?: run {
            stableFrameCount = 0
            return
        }

        if (mode == FaceCameraMode.ENROLL) {
            // 2. Reject if face is not at the required angle for the current step
            if (!currentStep.isActive(face.headEulerAngleY)) {
                stableFrameCount = 0
                return
            }
            // 3. Require N consecutive stable frames before accepting the capture
            stableFrameCount++
            if (stableFrameCount < minStableFrames) return
            stableFrameCount = 0
        }

        isProcessing = true
        viewModelScope.launch {
            if (mode == FaceCameraMode.ENROLL) {
                handleEnrollStep(face, embedding)
            } else {
                _state.update { FaceCameraState.Processing }
                verify(embedding)
            }
        }
    }

    // ── Enrollment (multi-angle) ──────────────────────────────────────────────

    @Suppress("ReturnCount")
    private fun handleEnrollStep(face: Face, embedding: FloatArray) {
        val eulerY = face.headEulerAngleY
        if (!currentStep.isActive(eulerY)) {
            isProcessing = false
            return
        }

        capturedEmbeddings.add(embedding)
        val captured = capturedEmbeddings.size
        val next = currentStep.next()

        if (next == null) {
            // All steps done — validate uniqueness then save
            val username = enrollUsername
            if (username == null) {
                _state.update { FaceCameraState.NoMatch("Usuario no especificado.") }
                isProcessing = false
                return
            }
            // Reject if this face is already enrolled under another user
            val conflict = findConflictingUser(capturedEmbeddings, excludeUsername = username)
            if (conflict != null) {
                Timber.w("Enrollment rejected: face matches existing user $conflict")
                _state.update {
                    FaceCameraState.NoMatch(
                        "Este rostro ya está registrado para otro tripulante. " +
                            "Cada persona debe registrar su propio rostro."
                    )
                }
                isProcessing = false
                return
            }
            faceCredentialStore.storeEmbeddings(username, capturedEmbeddings.toList())
            capturedEmbeddings.forEachIndexed { idx, emb ->
                Timber.d(
                    "[FaceEnroll] $username angle[$idx] size=${emb.size} " +
                        "values=${emb.take(LOG_SAMPLE).joinToString { "%.4f".format(it) }}..."
                )
            }
            Timber.d("[FaceEnroll] Stored ${capturedEmbeddings.size} embeddings for $username")
            faceCredentialStore.dumpToLog()
            _state.update { FaceCameraState.Enrolled }
        } else {
            currentStep = next
            _state.update {
                FaceCameraState.Enrolling(currentStep, captured, TOTAL_ENROLLMENT_STEPS)
            }
        }
        isProcessing = false
    }

    // ── Verification (1:N with role check) ───────────────────────────────────

    @Suppress("ReturnCount")
    private fun verify(embedding: FloatArray) {
        val enrolled = faceCredentialStore.enrolledUsernames()
        if (enrolled.isEmpty()) {
            _state.update { FaceCameraState.NoMatch("No hay rostros registrados.") }
            isProcessing = false
            return
        }

        // Find best match across all stored embeddings (multiple angles)
        var bestUsername = ""
        var bestSim = -1f
        Timber.d("[FaceVerify] Incoming embedding size=${embedding.size}")
        for (username in enrolled) {
            val storedEmbeddings = faceCredentialStore.getEmbeddings(username)
            Timber.d("[FaceVerify] Comparing with $username (${storedEmbeddings.size} stored embeddings)")
            storedEmbeddings.forEachIndexed { idx, stored ->
                val sim = FaceEmbeddingHelper.similarity(embedding, stored)
                Timber.d("[FaceVerify]   angle[$idx] sim=${"%.4f".format(sim)} (stored.size=${stored.size})")
                if (sim > bestSim) {
                    bestSim = sim
                    bestUsername = username
                }
            }
        }
        Timber.d(
            "[FaceVerify] Best: $bestUsername sim=${"%.4f".format(bestSim)} " +
                "threshold=${FaceEmbeddingHelper.MATCH_THRESHOLD}"
        )
        faceCredentialStore.dumpToLog()

        if (bestSim < FaceEmbeddingHelper.MATCH_THRESHOLD) {
            _state.update { FaceCameraState.NoMatch("Rostro no reconocido. Usa tu contraseña.") }
            isProcessing = false
            return
        }

        // Security: if there is a role restriction (post-logout), validate it
        if (loggedOutRole.isNotBlank()) {
            val matchedRole = faceCredentialStore.getRole(bestUsername).orEmpty()
            val roleMatches = matchedRole.equals(loggedOutRole, ignoreCase = true)
            if (!roleMatches) {
                val required = OperationRole.getRoleByName(loggedOutRole)?.humanizedName ?: loggedOutRole
                _state.update {
                    FaceCameraState.NoMatch(
                        "Rostro reconocido pero este acceso requiere un tripulante de tipo $required."
                    )
                }
                isProcessing = false
                return
            }
        }

        Timber.d("Face verified: $bestUsername (sim=$bestSim)")
        _state.update {
            FaceCameraState.Success(username = bestUsername, navigationModel = FaceNavigationModel())
        }
        isProcessing = false
    }

    // ── Public helpers ───────────────────────────────────────────────────────

    fun reset() {
        isProcessing = false
        capturedEmbeddings.clear()
        currentStep = EnrollmentStep.FRONTAL
        stableFrameCount = 0
        _state.update {
            if (mode == FaceCameraMode.ENROLL) {
                FaceCameraState.Enrolling(EnrollmentStep.FRONTAL, 0, TOTAL_ENROLLMENT_STEPS)
            } else {
                FaceCameraState.Scanning
            }
        }
    }

    fun storeRefreshToken(username: String, refreshToken: String) {
        faceCredentialStore.storeRefreshToken(username, refreshToken)
    }

    fun storeRole(username: String, role: String) {
        faceCredentialStore.storeRole(username, role)
    }

    /**
     * Returns the username of an already-enrolled user whose face matches any of
     * [newEmbeddings], or null if no conflict is found.
     * [excludeUsername] is skipped so re-enrolling the same user doesn't self-block.
     */
    private fun findConflictingUser(newEmbeddings: List<FloatArray>, excludeUsername: String): String? =
        faceCredentialStore.enrolledUsernames()
            .filter { it != excludeUsername }
            .firstOrNull { otherUsername ->
                val stored = faceCredentialStore.getEmbeddings(otherUsername)
                newEmbeddings.any { newEmb ->
                    stored.any { storedEmb ->
                        FaceEmbeddingHelper.similarity(newEmb, storedEmb) >= FaceEmbeddingHelper.MATCH_THRESHOLD
                    }
                }
            }

    fun hasEnrolledFace(username: String): Boolean = faceCredentialStore.hasEmbedding(username)

    fun enrolledUsernames(): List<String> = faceCredentialStore.enrolledUsernames()
}

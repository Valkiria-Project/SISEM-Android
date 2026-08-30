package com.skgtecnologia.sisem.ui.authcards.face

import android.graphics.Bitmap
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.mlkit.vision.face.Face
import com.skgtecnologia.sisem.commons.biometric.FaceCredentialStore
import com.skgtecnologia.sisem.commons.biometric.FaceEmbeddingHelper
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.usecases.GetConflictingActiveSession
import com.skgtecnologia.sisem.domain.model.banner.faceEnrollmentSuccessBanner
import com.skgtecnologia.sisem.domain.model.banner.faceErrorBanner
import com.skgtecnologia.sisem.domain.model.banner.faceVerificationSuccessBanner
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

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

/** Random action the user must perform to prove they are a live person (anti-spoofing). */
enum class LivenessChallenge { BLINK, TURN_LEFT, TURN_RIGHT }

/** Real-time positioning feedback shown while the scanner looks for a well-framed face. */
enum class FaceGuidance { NO_FACE, TOO_FAR, TOO_CLOSE, GOOD }

sealed interface FaceCameraState {
    data object Scanning : FaceCameraState
    data class Enrolling(val step: EnrollmentStep, val captured: Int, val total: Int) : FaceCameraState
    data class AwaitingLiveness(val challenge: LivenessChallenge, val secondsLeft: Int) : FaceCameraState
    data object Processing : FaceCameraState
    data class Success(val username: String) : FaceCameraState
    data class NoMatch(val message: String, val descriptionAnnotated: AnnotatedString? = null) : FaceCameraState
    data object Enrolled : FaceCameraState
}

private const val TOTAL_ENROLLMENT_STEPS = 3
private const val MIN_STABLE_FRAMES = 6
private const val LOG_SAMPLE = 8

// Distance guidance — face box width as a fraction of the frame width.
private const val FACE_MIN_FRACTION = 0.38f
private const val FACE_MAX_FRACTION = 0.72f

// Liveness detection
private const val MIN_STABLE_FOR_LIVENESS = 3 // frames before issuing challenge
private const val LIVENESS_TIMEOUT_SECONDS = 8
private const val BLINK_CLOSED_THRESHOLD = 0.3f // eye open probability → "closed"
private const val BLINK_OPEN_THRESHOLD = 0.7f // eye open probability → "open" after blink
private const val TURN_LIVENESS_THRESHOLD = 20f // degrees of head rotation
private const val LIVENESS_TICK_MS = 1_000L // countdown interval

@HiltViewModel
@Suppress("TooManyFunctions")
class FaceCameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val faceCredentialStore: FaceCredentialStore,
    private val faceEmbeddingHelper: FaceEmbeddingHelper,
    private val uploadBiometric: com.skgtecnologia.sisem.domain.biometric.usecases.UploadBiometric,
    private val uploadBiometricByDocument: com.skgtecnologia.sisem.domain.biometric.usecases.UploadBiometricByDocument,
    private val getConflictingActiveSession: GetConflictingActiveSession
) : ViewModel() {

    private val route = savedStateHandle.toRoute<AuthRoute.FaceCameraRoute>()
    val mode: FaceCameraMode = if (route.mode == "ENROLL") FaceCameraMode.ENROLL else FaceCameraMode.VERIFY
    val enrollUsername: String? = route.username.ifBlank { null }
    private val enrollDocument: String? = route.document.ifBlank { null }
    private val loggedOutRole: String = route.loggedOutRole
    private val targetRole: String = route.targetRole

    // Pre-pick the enroll challenge so it can seed the initial UI state synchronously.
    private val initialEnrollChallenge =
        if (mode == FaceCameraMode.ENROLL) LivenessChallenge.entries.random() else null

    private val _state = MutableStateFlow<FaceCameraState>(
        if (mode == FaceCameraMode.ENROLL) {
            FaceCameraState.AwaitingLiveness(initialEnrollChallenge!!, LIVENESS_TIMEOUT_SECONDS)
        } else {
            FaceCameraState.Scanning
        }
    )
    val state: StateFlow<FaceCameraState> = _state

    private val _banner = MutableStateFlow<BannerUiModel?>(null)
    val banner: StateFlow<BannerUiModel?> = _banner

    private val _guidance = MutableStateFlow(FaceGuidance.NO_FACE)
    val guidance: StateFlow<FaceGuidance> = _guidance

    private var isProcessing = false
    private val capturedEmbeddings = mutableListOf<FloatArray>()
    private var currentStep = EnrollmentStep.FRONTAL

    // Stability for enrollment: face must hold position for N consecutive frames.
    private var stableFrameCount = 0
    private val minStableFrames = MIN_STABLE_FRAMES

    // ── Liveness detection (ENROLL + VERIFY) ─────────────────────────────────

    private enum class VerifyPhase { DETECTING, AWAITING_LIVENESS, VERIFYING }
    private enum class EnrollPhase { LIVENESS, CAPTURING }

    private var verifyPhase = VerifyPhase.DETECTING
    private var enrollPhase = EnrollPhase.LIVENESS
    private var stableFaceFrames = 0
    private var currentLivenessChallenge: LivenessChallenge? = null
    private var eyesWereClosed = false
    private var livenessJob: Job? = null

    init {
        if (mode == FaceCameraMode.ENROLL) {
            currentLivenessChallenge = initialEnrollChallenge
            startEnrollLivenessCountdown(initialEnrollChallenge!!)
        }
        viewModelScope.launch {
            _state.collect { newState ->
                if (newState is FaceCameraState.NoMatch) {
                    _banner.update {
                        faceErrorBanner(newState.message).mapToUi()
                            .copy(descriptionAnnotated = newState.descriptionAnnotated)
                    }
                }
            }
        }
    }

    // ── Main entry point ─────────────────────────────────────────────────────

    @Suppress("ReturnCount")
    fun onFaceDetected(face: Face, bitmap: Bitmap) {
        if (isProcessing) return

        if (face.boundingBox.width() < FaceEmbeddingHelper.MIN_FACE_PX) {
            _guidance.update { FaceGuidance.TOO_FAR }
            stableFrameCount = 0
            stableFaceFrames = 0
            return
        }
        _guidance.update { faceGuidanceFor(face, bitmap) }

        if (mode == FaceCameraMode.ENROLL) {
            handleEnrollFrame(face, bitmap)
        } else {
            handleVerifyFrame(face, bitmap)
        }
    }

    private fun faceGuidanceFor(face: Face, bitmap: Bitmap): FaceGuidance {
        val fraction = face.boundingBox.width().toFloat() / bitmap.width.coerceAtLeast(1)
        return when {
            fraction < FACE_MIN_FRACTION -> FaceGuidance.TOO_FAR
            fraction > FACE_MAX_FRACTION -> FaceGuidance.TOO_CLOSE
            else -> FaceGuidance.GOOD
        }
    }

    // ── Verify with liveness challenge ───────────────────────────────────────

    @Suppress("ReturnCount")
    private fun handleVerifyFrame(face: Face, bitmap: Bitmap) {
        when (verifyPhase) {
            VerifyPhase.DETECTING -> {
                stableFaceFrames++
                if (stableFaceFrames >= MIN_STABLE_FOR_LIVENESS) startLivenessChallenge()
            }

            VerifyPhase.AWAITING_LIVENESS -> {
                val challenge = currentLivenessChallenge ?: return
                if (!isChallengePassed(challenge, face)) return
                // Challenge passed — extract embedding and verify identity
                verifyPhase = VerifyPhase.VERIFYING
                livenessJob?.cancel()
                val embedding = faceEmbeddingHelper.extractEmbedding(face, bitmap) ?: return
                isProcessing = true
                viewModelScope.launch {
                    _state.update { FaceCameraState.Processing }
                    verify(embedding)
                }
            }

            VerifyPhase.VERIFYING -> {
                Unit
            } // isProcessing gates further frames
        }
    }

    // ── Enroll with liveness challenge ───────────────────────────────────────

    @Suppress("ReturnCount")
    private fun handleEnrollFrame(face: Face, bitmap: Bitmap) {
        when (enrollPhase) {
            EnrollPhase.LIVENESS -> {
                val challenge = currentLivenessChallenge ?: return
                if (!isChallengePassed(challenge, face)) return
                // Challenge passed — begin multi-angle capture
                enrollPhase = EnrollPhase.CAPTURING
                livenessJob?.cancel()
                livenessJob = null
                Timber.d("[Liveness] Enroll challenge passed — starting capture")
                _state.update { FaceCameraState.Enrolling(EnrollmentStep.FRONTAL, 0, TOTAL_ENROLLMENT_STEPS) }
            }

            EnrollPhase.CAPTURING -> {
                if (!currentStep.isActive(face.headEulerAngleY)) {
                    stableFrameCount = 0
                    return
                }
                stableFrameCount++
                if (stableFrameCount < minStableFrames) return
                stableFrameCount = 0
                val embedding = faceEmbeddingHelper.extractEmbedding(face, bitmap) ?: return
                isProcessing = true
                viewModelScope.launch { handleEnrollStep(face, embedding) }
            }
        }
    }

    private fun startEnrollLivenessChallenge() {
        val challenge = LivenessChallenge.entries.random()
        currentLivenessChallenge = challenge
        startEnrollLivenessCountdown(challenge)
    }

    private fun startEnrollLivenessCountdown(challenge: LivenessChallenge) {
        eyesWereClosed = false
        enrollPhase = EnrollPhase.LIVENESS
        Timber.d("[Liveness] Enroll challenge issued: $challenge")

        livenessJob?.cancel()
        livenessJob = viewModelScope.launch {
            var remaining = LIVENESS_TIMEOUT_SECONDS
            while (remaining > 0) {
                _state.update { FaceCameraState.AwaitingLiveness(challenge, remaining) }
                delay(LIVENESS_TICK_MS)
                remaining--
            }
            Timber.w("[Liveness] Enroll liveness timeout")
            _state.update {
                FaceCameraState.NoMatch("No se pudo verificar que eres una persona real. Inténtalo de nuevo.")
            }
            resetLivenessState()
        }
    }

    private fun startLivenessChallenge() {
        val challenge = LivenessChallenge.entries.random()
        currentLivenessChallenge = challenge
        eyesWereClosed = false
        verifyPhase = VerifyPhase.AWAITING_LIVENESS
        Timber.d("[Liveness] Challenge issued: $challenge")

        livenessJob?.cancel()
        livenessJob = viewModelScope.launch {
            var remaining = LIVENESS_TIMEOUT_SECONDS
            while (remaining > 0) {
                _state.update { FaceCameraState.AwaitingLiveness(challenge, remaining) }
                delay(LIVENESS_TICK_MS)
                remaining--
            }
            Timber.w("[Liveness] Timeout — challenge not completed")
            _state.update {
                FaceCameraState.NoMatch(
                    "No se pudo verificar que eres una persona real. Inténtalo de nuevo."
                )
            }
            resetLivenessState()
        }
    }

    @Suppress("MagicNumber")
    private fun isChallengePassed(challenge: LivenessChallenge, face: Face): Boolean =
        when (challenge) {
            LivenessChallenge.BLINK -> {
                val leftOpen = face.leftEyeOpenProbability ?: 1f
                val rightOpen = face.rightEyeOpenProbability ?: 1f
                if (leftOpen < BLINK_CLOSED_THRESHOLD && rightOpen < BLINK_CLOSED_THRESHOLD) {
                    eyesWereClosed = true
                }
                eyesWereClosed && leftOpen > BLINK_OPEN_THRESHOLD && rightOpen > BLINK_OPEN_THRESHOLD
            }

            // eulerY > 0 → face points camera-right = user's head turned to their left
            LivenessChallenge.TURN_LEFT -> {
                face.headEulerAngleY > TURN_LIVENESS_THRESHOLD
            }

            LivenessChallenge.TURN_RIGHT -> {
                face.headEulerAngleY < -TURN_LIVENESS_THRESHOLD
            }
        }

    private fun resetLivenessState() {
        verifyPhase = VerifyPhase.DETECTING
        enrollPhase = EnrollPhase.LIVENESS
        stableFaceFrames = 0
        currentLivenessChallenge = null
        eyesWereClosed = false
        livenessJob = null
    }

    // ── Enrollment (multi-angle) ──────────────────────────────────────────────

    private suspend fun handleEnrollStep(face: Face, embedding: FloatArray) {
        val eulerY = face.headEulerAngleY
        if (!currentStep.isActive(eulerY)) {
            isProcessing = false
            return
        }

        capturedEmbeddings.add(embedding)
        val captured = capturedEmbeddings.size
        val next = currentStep.next()

        if (next == null) {
            finishEnrollment()
        } else {
            currentStep = next
            _state.update {
                FaceCameraState.Enrolling(currentStep, captured, TOTAL_ENROLLMENT_STEPS)
            }
            isProcessing = false
        }
    }

    private suspend fun finishEnrollment() {
        // Líder APH flow: upload to the backend by document, no local storage.
        enrollDocument?.let { document ->
            enrollByDocument(document)
            return
        }
        enrollLocally()
    }

    private suspend fun enrollByDocument(document: String) {
        _state.update { FaceCameraState.Processing }
        uploadBiometricByDocument(document, capturedEmbeddings.toList())
            .onSuccess {
                _state.update { FaceCameraState.Enrolled }
                _banner.update { faceEnrollmentSuccessBanner().mapToUi() }
            }
            .onFailure {
                Timber.w(it, "[FaceEnroll] Upload by document failed for $document")
                _state.update {
                    FaceCameraState.NoMatch(
                        "No se pudo registrar la biometría. Inténtalo de nuevo."
                    )
                }
            }
        isProcessing = false
    }

    @Suppress("ReturnCount")
    private suspend fun enrollLocally() {
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
        viewModelScope.launch { uploadBiometric(username) }
        _state.update { FaceCameraState.Enrolled }
        _banner.update { faceEnrollmentSuccessBanner().mapToUi() }
        isProcessing = false
    }

    // ── Verification (1:N with role check) ───────────────────────────────────

    @Suppress("ReturnCount", "LongMethod")
    private suspend fun verify(embedding: FloatArray) {
        val enrolled = faceCredentialStore.enrolledUsernames()
        if (enrolled.isEmpty()) {
            _state.update { FaceCameraState.NoMatch("Usuario no reconocido. Usa tu contraseña.") }
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

        // Security: a matched face already logged in under a different role must not silently
        // switch sessions — the crew member has to know their session is still active elsewhere.
        val conflictingSession = getConflictingActiveSession(bestUsername, targetRole)
        if (conflictingSession != null) {
            val activeRoleName = OperationRole.getRoleByName(conflictingSession.role)?.humanizedName
                ?: conflictingSession.role
            val nameUser = conflictingSession.nameUser
            _state.update {
                FaceCameraState.NoMatch(
                    message = "$nameUser ya tiene una sesión activa como $activeRoleName.",
                    descriptionAnnotated = buildAnnotatedString {
                        append("El tripulante ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(nameUser) }
                        append(" ya tiene una sesión activa como ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(activeRoleName) }
                        append(".")
                    }
                )
            }
            isProcessing = false
            return
        }

        Timber.d("Face verified: $bestUsername (sim=$bestSim)")
        _state.update {
            FaceCameraState.Success(username = bestUsername)
        }
        _banner.update { faceVerificationSuccessBanner(bestUsername).mapToUi() }
        isProcessing = false
    }

    // ── Public helpers ───────────────────────────────────────────────────────

    fun consumeBanner() {
        _banner.update { null }
    }

    fun reset() {
        isProcessing = false
        capturedEmbeddings.clear()
        currentStep = EnrollmentStep.FRONTAL
        stableFrameCount = 0
        _guidance.update { FaceGuidance.NO_FACE }
        livenessJob?.cancel()
        resetLivenessState()
        if (mode == FaceCameraMode.ENROLL) {
            startEnrollLivenessChallenge()
        } else {
            _state.update { FaceCameraState.Scanning }
        }
    }

    fun storeRefreshToken(username: String, refreshToken: String) {
        viewModelScope.launch { faceCredentialStore.storeRefreshToken(username, refreshToken) }
    }

    fun storeRole(username: String, role: String) {
        viewModelScope.launch { faceCredentialStore.storeRole(username, role) }
    }

    /**
     * Returns the username of an already-enrolled user whose face matches any of
     * [newEmbeddings], or null if no conflict is found.
     * [excludeUsername] is skipped so re-enrolling the same user doesn't self-block.
     */
    private suspend fun findConflictingUser(
        newEmbeddings: List<FloatArray>,
        excludeUsername: String
    ): String? =
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

    suspend fun hasEnrolledFace(username: String): Boolean =
        faceCredentialStore.hasEmbedding(username)

    suspend fun enrolledUsernames(): List<String> = faceCredentialStore.enrolledUsernames()
}

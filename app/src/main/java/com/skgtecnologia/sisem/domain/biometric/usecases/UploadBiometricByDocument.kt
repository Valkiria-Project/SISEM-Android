package com.skgtecnologia.sisem.domain.biometric.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import javax.inject.Inject

/**
 * Uploads the líder APH biometric enrollment to the cloud, identified only by
 * document number (no token/username). Used by the signature &amp; biometric flow.
 */
class UploadBiometricByDocument @Inject constructor(
    private val biometricRepository: BiometricRepository
) {

    @CheckResult
    suspend operator fun invoke(
        document: String,
        embeddings: List<FloatArray>
    ): Result<Unit> = biometricRepository.uploadByDocument(document, embeddings)
}

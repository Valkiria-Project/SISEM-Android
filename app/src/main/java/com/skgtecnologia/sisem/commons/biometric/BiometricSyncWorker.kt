package com.skgtecnologia.sisem.commons.biometric

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

private const val MAX_RETRY_ATTEMPTS = 3

/**
 * Background worker that retries a failed biometric upload to the cloud.
 * Scheduled by [UploadBiometric] when the immediate attempt fails.
 */
@HiltWorker
class BiometricSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val biometricRepository: BiometricRepository
) : CoroutineWorker(context, params) {

    companion object {
        const val KEY_USERNAME = "biometric_username"
    }

    override suspend fun doWork(): Result {
        val username = inputData.getString(KEY_USERNAME) ?: return Result.failure()
        Timber.d("[BiometricSync] Attempt $runAttemptCount for $username")

        return biometricRepository.uploadToCloud(username).fold(
            onSuccess = {
                Timber.d("[BiometricSync] Success for $username")
                Result.success()
            },
            onFailure = {
                if (runAttemptCount < MAX_RETRY_ATTEMPTS) Result.retry() else Result.failure()
            }
        )
    }
}

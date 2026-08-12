package com.skgtecnologia.sisem.domain.biometric.usecases

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.skgtecnologia.sisem.commons.biometric.BiometricSyncWorker
import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val BACKOFF_DELAY_SECONDS = 30L

class UploadBiometric @Inject constructor(
    @ApplicationContext private val context: Context,
    private val biometricRepository: BiometricRepository
) {

    /**
     * Attempts to upload biometrics to the cloud immediately.
     * If the upload fails (no connection, server error), a WorkManager job is scheduled
     * to retry with exponential backoff when connectivity is restored.
     */
    suspend operator fun invoke(username: String) {
        val result = biometricRepository.uploadToCloud(username)
        if (result.isSuccess) {
            Timber.d("[Biometric] Uploaded to cloud: $username")
        } else {
            Timber.w("[Biometric] Upload failed for $username — scheduling retry via WorkManager")
            WorkManager.getInstance(context).enqueueUniqueWork(
                "biometric_sync_$username",
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<BiometricSyncWorker>()
                    .setInputData(workDataOf(BiometricSyncWorker.KEY_USERNAME to username))
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .setBackoffCriteria(
                        BackoffPolicy.EXPONENTIAL,
                        BACKOFF_DELAY_SECONDS,
                        TimeUnit.SECONDS
                    )
                    .build()
            )
        }
    }
}

package com.skgtecnologia.sisem

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.skgtecnologia.sisem.commons.logging.CrashFileHandler
import com.skgtecnologia.sisem.commons.logging.FileLoggingTree
import com.skgtecnologia.sisem.data.offline.outbox.OutboxSyncTrigger
import com.skgtecnologia.sisem.data.offline.screen.IncidentScreenPrefetcher
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class SisemApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var outboxSyncTrigger: OutboxSyncTrigger

    @Inject
    lateinit var incidentScreenPrefetcher: IncidentScreenPrefetcher

    // Lives as long as the process: the outbox and the active incident have to be watched
    // whatever screen is open.
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        outboxSyncTrigger.start(applicationScope)
        incidentScreenPrefetcher.start(applicationScope)

        Timber.plant(FileLoggingTree(this))
        CrashFileHandler.install()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        FirebaseApp.initializeApp(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.w(task.exception, "Fetching FCM registration token failed")
                    return@OnCompleteListener
                }
                Timber.d("FCM registration token: ${task.result}")
            }
        )

        MapboxNavigationApp.setup {
            NavigationOptions.Builder(this).build()
        }
    }
}

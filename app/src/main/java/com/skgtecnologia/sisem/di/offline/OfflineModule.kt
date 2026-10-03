package com.skgtecnologia.sisem.di.offline

import android.content.Context
import androidx.room.Room
import com.skgtecnologia.sisem.commons.connectivity.AndroidNetworkMonitor
import com.skgtecnologia.sisem.commons.connectivity.NetworkMonitor
import com.skgtecnologia.sisem.commons.security.BlobCipher
import com.skgtecnologia.sisem.commons.security.KeystoreBlobCipher
import com.skgtecnologia.sisem.data.offline.OfflineDatabase
import com.skgtecnologia.sisem.data.offline.outbox.OutboxStore
import com.skgtecnologia.sisem.data.offline.outbox.OutboxSyncScheduler
import com.skgtecnologia.sisem.data.offline.outbox.WorkManagerOutboxSyncScheduler
import com.skgtecnologia.sisem.data.offline.screen.ScreenCacheStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OfflineModule {

    @Binds
    internal abstract fun bindsNetworkMonitor(monitor: AndroidNetworkMonitor): NetworkMonitor

    @Binds
    internal abstract fun bindsBlobCipher(cipher: KeystoreBlobCipher): BlobCipher

    @Binds
    internal abstract fun bindsOutboxSyncScheduler(
        scheduler: WorkManagerOutboxSyncScheduler
    ): OutboxSyncScheduler

    companion object {

        // noBackupFilesDir rather than cacheDir: the system may clear the cache when storage runs
        // low — exactly when a device that has been offline for hours needs these copies — and
        // nothing here should end up in a cloud backup.
        @Provides
        @Singleton
        internal fun providesScreenCacheStore(
            @ApplicationContext context: Context,
            cipher: BlobCipher
        ): ScreenCacheStore = ScreenCacheStore(
            directory = File(context.noBackupFilesDir, "offline/screens"),
            cipher = cipher
        )

        // No destructive fallback, unlike SisemDatabase: see OfflineDatabase.
        @Provides
        @Singleton
        internal fun providesOfflineDatabase(@ApplicationContext context: Context): OfflineDatabase =
            Room.databaseBuilder(context, OfflineDatabase::class.java, "sisem-offline.db").build()

        @Provides
        @Singleton
        internal fun providesOutboxStore(
            @ApplicationContext context: Context,
            database: OfflineDatabase,
            cipher: BlobCipher
        ): OutboxStore = OutboxStore(
            dao = database.outboxDao(),
            bodies = File(context.noBackupFilesDir, "offline/outbox"),
            cipher = cipher
        )
    }
}

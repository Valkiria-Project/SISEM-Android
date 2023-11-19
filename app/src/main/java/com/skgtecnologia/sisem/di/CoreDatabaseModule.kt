package com.skgtecnologia.sisem.di

import android.content.Context
import androidx.room.Room
import com.skgtecnologia.sisem.data.auth.cache.dao.AccessTokenDao
import com.skgtecnologia.sisem.data.cache.SisemDatabase
import com.skgtecnologia.sisem.data.notification.cache.dao.NotificationDao
import com.skgtecnologia.sisem.data.operation.cache.dao.OperationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoreDatabaseModule {

    @Singleton
    @Provides
    fun provideSisemDatabase(@ApplicationContext appContext: Context): SisemDatabase {
        return Room.databaseBuilder(appContext, SisemDatabase::class.java, "sisem.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAccessTokenDao(sisemDatabase: SisemDatabase): AccessTokenDao {
        return sisemDatabase.accessTokenDao()
    }

    @Provides
    fun provideNotificationDao(sisemDatabase: SisemDatabase): NotificationDao {
        return sisemDatabase.notificationDao()
    }

    @Provides
    fun provideOperationDao(sisemDatabase: SisemDatabase): OperationDao {
        return sisemDatabase.configDao()
    }
}

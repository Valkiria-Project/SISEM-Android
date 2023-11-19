package com.skgtecnologia.sisem.di.notification

import com.skgtecnologia.sisem.data.notification.NotificationRepositoryImpl
import com.skgtecnologia.sisem.domain.notification.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationRepositoryModule {

    @Binds
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository
}

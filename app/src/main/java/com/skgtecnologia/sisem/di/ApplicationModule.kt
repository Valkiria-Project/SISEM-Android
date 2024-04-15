package com.skgtecnologia.sisem.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.skgtecnologia.sisem.commons.location.DefaultLocationProvider
import com.skgtecnologia.sisem.commons.location.LocationProvider
import com.skgtecnologia.sisem.data.notification.NotificationsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(application: Application): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun providesNotificationsManager(application: Application): NotificationsManager =
        NotificationsManager(application.applicationContext)

    @Provides
    @Singleton
    fun providesLocationProvider(
        application: Application,
        client: FusedLocationProviderClient
    ): LocationProvider = DefaultLocationProvider(application.applicationContext, client)
}

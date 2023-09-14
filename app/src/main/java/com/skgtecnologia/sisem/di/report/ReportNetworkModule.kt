package com.skgtecnologia.sisem.di.report

import com.skgtecnologia.sisem.data.report.remote.ReportApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object ReportNetworkModule {

    @Provides
    internal fun provideReportApi(@BearerAuthentication retrofit: Retrofit): ReportApi =
        retrofit.create(ReportApi::class.java)
}

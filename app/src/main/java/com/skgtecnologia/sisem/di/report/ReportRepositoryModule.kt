package com.skgtecnologia.sisem.di.report

import com.skgtecnologia.sisem.data.report.ReportRepositoryImpl
import com.skgtecnologia.sisem.domain.report.ReportRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ReportRepositoryModule {

    @Binds
    abstract fun bindReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl
    ): ReportRepository
}

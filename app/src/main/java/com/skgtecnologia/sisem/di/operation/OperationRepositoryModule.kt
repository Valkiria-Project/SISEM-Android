package com.skgtecnologia.sisem.di.operation

import com.skgtecnologia.sisem.data.operation.OperationRepositoryImpl
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class OperationRepositoryModule {

    @Binds
    abstract fun bindOperationRepository(
        operationRepositoryImpl: OperationRepositoryImpl
    ): OperationRepository
}

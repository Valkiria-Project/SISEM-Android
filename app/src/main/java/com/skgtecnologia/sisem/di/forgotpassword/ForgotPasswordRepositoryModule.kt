package com.skgtecnologia.sisem.di.forgotpassword

import com.skgtecnologia.sisem.data.forgotpassword.ForgotPasswordRepositoryImpl
import com.skgtecnologia.sisem.domain.forgotpassword.ForgotPasswordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ForgotPasswordRepositoryModule {

    @Binds
    abstract fun bindForgotPasswordRepository(
        forgotPasswordRepositoryImpl: ForgotPasswordRepositoryImpl
    ): ForgotPasswordRepository
}

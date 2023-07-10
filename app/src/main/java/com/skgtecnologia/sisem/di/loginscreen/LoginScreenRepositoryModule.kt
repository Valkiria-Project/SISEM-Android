package com.skgtecnologia.sisem.di.loginscreen

import com.skgtecnologia.sisem.data.loginscreen.LoginScreenRepositoryImpl
import com.skgtecnologia.sisem.domain.loginscreen.LoginScreenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LoginScreenRepositoryModule {

    @Binds
    abstract fun bindLoginScreenRepositoryImpl(
        loginScreenRepositoryImpl: LoginScreenRepositoryImpl
    ): LoginScreenRepository
}

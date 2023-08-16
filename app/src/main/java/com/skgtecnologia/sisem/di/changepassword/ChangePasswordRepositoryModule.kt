package com.skgtecnologia.sisem.di.changepassword

import com.skgtecnologia.sisem.data.changepassword.ChangePasswordRepositoryImpl
import com.skgtecnologia.sisem.domain.changepassword.ChangePasswordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ChangePasswordRepositoryModule {

    @Binds
    abstract fun bindChangePasswordRepository(
        changePasswordRepositoryImpl: ChangePasswordRepositoryImpl
    ): ChangePasswordRepository
}

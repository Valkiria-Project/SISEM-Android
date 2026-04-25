package com.skgtecnologia.sisem.di.signature

import com.skgtecnologia.sisem.data.signature.SignatureRepositoryImpl
import com.skgtecnologia.sisem.domain.signature.SignatureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SignatureRepositoryModule {

    @Binds
    abstract fun bindSignatureRepository(
        signatureRepositoryImpl: SignatureRepositoryImpl
    ): SignatureRepository
}

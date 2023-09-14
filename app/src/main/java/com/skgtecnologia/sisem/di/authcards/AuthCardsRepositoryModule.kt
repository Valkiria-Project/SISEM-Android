package com.skgtecnologia.sisem.di.authcards

import com.skgtecnologia.sisem.data.authcards.AuthCardsRepositoryImpl
import com.skgtecnologia.sisem.domain.authcards.AuthCardsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthCardsRepositoryModule {

    @Binds
    abstract fun bindAuthCardsRepository(
        authCardsRepositoryImpl: AuthCardsRepositoryImpl
    ): AuthCardsRepository
}

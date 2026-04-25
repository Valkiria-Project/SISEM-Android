package com.skgtecnologia.sisem.di.sendemail

import com.skgtecnologia.sisem.data.sendemail.SendEmailRepositoryImpl
import com.skgtecnologia.sisem.domain.sendemail.SendEmailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SendEmailRepositoryModule {

    @Binds
    abstract fun bindSendEmailRepository(
        sendEmailRepositoryImpl: SendEmailRepositoryImpl
    ): SendEmailRepository
}

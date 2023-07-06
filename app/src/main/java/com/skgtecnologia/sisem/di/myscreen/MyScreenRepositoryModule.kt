package com.skgtecnologia.sisem.di.myscreen

import com.skgtecnologia.sisem.data.myscreen.MyScreenRepositoryImpl
import com.skgtecnologia.sisem.domain.myscreen.MyScreenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MyScreenRepositoryModule {

    @Binds
    abstract fun bindMyScreenRepositoryImpl(
        myScreenRepositoryImpl: MyScreenRepositoryImpl
    ): MyScreenRepository
}

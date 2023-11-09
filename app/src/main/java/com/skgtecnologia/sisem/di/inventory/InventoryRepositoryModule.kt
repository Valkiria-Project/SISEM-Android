package com.skgtecnologia.sisem.di.inventory

import com.skgtecnologia.sisem.data.inventory.InventoryRepositoryImpl
import com.skgtecnologia.sisem.domain.inventory.InventoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class InventoryRepositoryModule {

    @Binds
    abstract fun bindInventoryRepository(
        inventoryRepositoryImpl: InventoryRepositoryImpl
    ): InventoryRepository
}

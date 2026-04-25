package com.skgtecnologia.sisem.domain.inventory.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.inventory.InventoryRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class GetInventoryInitialScreen @Inject constructor(
    private val inventoryRepository: InventoryRepository
) {

    @CheckResult
    suspend operator fun invoke(serial: String): Result<ScreenModel> = resultOf {
        inventoryRepository.getInventoryInitialScreen(serial)
    }
}

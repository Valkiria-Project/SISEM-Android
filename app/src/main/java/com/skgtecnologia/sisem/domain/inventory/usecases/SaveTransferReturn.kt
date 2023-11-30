package com.skgtecnologia.sisem.domain.inventory.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.inventory.InventoryRepository
import javax.inject.Inject

class SaveTransferReturn @Inject constructor(
    private val inventoryRepository: InventoryRepository
) {

    @CheckResult
    suspend operator fun invoke(
        fieldsValues: Map<String, String>,
        dropDownValues: Map<String, String>,
        chipSelectionValues: Map<String, String>,
        labelValues: Map<String, String>
    ): Result<Unit> = resultOf {
        inventoryRepository.saveTransferReturn(
            fieldsValues = fieldsValues,
            dropDownValues = dropDownValues,
            chipSelectionValues = chipSelectionValues,
            labelValues = labelValues
        )
    }
}

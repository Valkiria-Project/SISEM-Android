package com.skgtecnologia.sisem.domain.preoperational

import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface PreOperationalRepository {

    suspend fun getPreOperationalScreen(operationRole: OperationRole): ScreenModel

    suspend fun sendPreOperational()
}

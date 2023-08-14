package com.skgtecnologia.sisem.domain.preoperational

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface PreOperationalRepository {

    suspend fun getPreOperationalScreen(): ScreenModel

    suspend fun savePreOperational()
}

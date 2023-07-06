package com.skgtecnologia.sisem.domain.myscreen

import com.skgtecnologia.sisem.domain.myscreen.model.MyScreenModel

interface MyScreenRepository {

    suspend fun fetchMyScreen(): Long

    suspend fun getMyScreen(myScreenIdentifier: Long): MyScreenModel
}

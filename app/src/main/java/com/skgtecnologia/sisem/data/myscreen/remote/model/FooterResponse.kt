package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.myscreen.model.FooterModel

data class FooterResponse(
    val buttonList: List<ButtonResponse>? = null,
)

fun FooterResponse.mapToDomain(): FooterModel = FooterModel(
    buttonModelList = buttonList?.map { it.mapToDomain() } ?: error("buttonList cannot be null")
)

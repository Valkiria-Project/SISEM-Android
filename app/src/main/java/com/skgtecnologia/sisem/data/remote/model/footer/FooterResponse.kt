package com.skgtecnologia.sisem.data.remote.model.footer

import com.skgtecnologia.sisem.data.remote.model.body.ButtonResponse
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel

data class FooterResponse(
    val buttonList: List<ButtonResponse>? = null,
)

fun FooterResponse.mapToDomain(): FooterModel = FooterModel(
    buttonModelList = buttonList?.map { it.mapToDomain() as ButtonModel } ?: error("Footer buttonList cannot be null")
)

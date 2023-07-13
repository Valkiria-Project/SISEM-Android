package com.skgtecnologia.sisem.data.model.footer

import com.skgtecnologia.sisem.data.model.bodyrow.ButtonResponse
import com.skgtecnologia.sisem.domain.model.bodyrow.ButtonModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel

data class FooterResponse(
    val buttonList: List<ButtonResponse>? = null,
)

fun FooterResponse.mapToDomain(): FooterModel = FooterModel(
    buttonModelList = buttonList?.map { it.mapToDomain() as ButtonModel } ?: error("buttonList cannot be null")
)

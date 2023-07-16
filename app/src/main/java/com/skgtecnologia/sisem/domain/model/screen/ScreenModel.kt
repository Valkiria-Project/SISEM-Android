package com.skgtecnologia.sisem.domain.model.screen

import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel
import com.skgtecnologia.sisem.domain.model.header.HeaderModel

data class ScreenModel(
    val header: HeaderModel? = null,
    val body: List<BodyRowModel>,
    val footer: FooterModel? = null
)

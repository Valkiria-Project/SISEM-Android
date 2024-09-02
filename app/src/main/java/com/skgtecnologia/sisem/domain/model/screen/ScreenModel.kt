package com.skgtecnologia.sisem.domain.model.screen

import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.footer.FooterUiModel
import com.valkiria.uicomponents.components.header.HeaderUiModel

data class ScreenModel(
    val header: HeaderUiModel? = null,
    val body: List<BodyRowModel>,
    val footer: FooterUiModel? = null
)

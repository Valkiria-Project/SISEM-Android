package com.skgtecnologia.sisem.domain.model.screen

import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.headerbody.HeaderUiModel
import com.valkiria.uicomponents.model.ui.footer.FooterUiModel

data class ScreenModel(
    val header: HeaderUiModel? = null,
    val body: List<BodyRowModel>,
    val footer: FooterUiModel? = null
)

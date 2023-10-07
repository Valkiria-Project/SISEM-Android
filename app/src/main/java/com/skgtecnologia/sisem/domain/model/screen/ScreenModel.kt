package com.skgtecnologia.sisem.domain.model.screen

import com.valkiria.uicomponents.model.ui.body.HeaderUiModel
import com.valkiria.uicomponents.model.ui.footer.FooterUiModel
import com.valkiria.uicomponents.model.ui.body.BodyRowModel

data class ScreenModel(
    val header: HeaderUiModel? = null,
    val body: List<BodyRowModel>,
    val footer: FooterUiModel? = null
)

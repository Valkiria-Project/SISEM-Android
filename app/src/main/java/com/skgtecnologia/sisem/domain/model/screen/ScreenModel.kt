package com.skgtecnologia.sisem.domain.model.screen

import com.valkiria.uicomponents.model.ui.body.HeaderUiModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel
import com.valkiria.uicomponents.model.ui.body.BodyRowModel

data class ScreenModel(
    val header: HeaderUiModel? = null,
    val body: List<BodyRowModel>,
    val footer: FooterModel? = null
)

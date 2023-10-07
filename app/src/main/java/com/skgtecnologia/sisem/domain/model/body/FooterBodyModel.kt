package com.skgtecnologia.sisem.domain.model.body

import com.skgtecnologia.sisem.domain.model.footer.FooterModel
import com.valkiria.uicomponents.model.ui.body.BodyRowType
import com.valkiria.uicomponents.model.ui.body.ButtonModel

data class FooterBodyModel(
    val identifier: String = "FOOTER",
    val leftButton: ButtonModel,
    val rightButton: ButtonModel? = null
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FOOTER
}

fun FooterBodyModel.mapToSection() = FooterModel(
    leftButton = leftButton,
    rightButton = rightButton
)

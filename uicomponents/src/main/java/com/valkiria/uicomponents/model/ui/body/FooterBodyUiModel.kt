package com.valkiria.uicomponents.model.ui.body

import com.valkiria.uicomponents.model.ui.footer.FooterUiModel

data class FooterBodyUiModel(
    val identifier: String = "FOOTER",
    val leftButton: ButtonUiModel,
    val rightButton: ButtonUiModel? = null
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FOOTER
}

fun FooterBodyUiModel.mapToSection() = FooterUiModel(
    leftButton = leftButton,
    rightButton = rightButton
)

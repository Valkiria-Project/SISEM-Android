package com.valkiria.uicomponents.components.footer

import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.button.ButtonUiModel

data class FooterBodyUiModel(
    override val identifier: String = "FOOTER",
    val leftButton: ButtonUiModel,
    val rightButton: ButtonUiModel? = null
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FOOTER
}

fun FooterBodyUiModel.mapToSection() = FooterUiModel(
    leftButton = leftButton,
    rightButton = rightButton
)

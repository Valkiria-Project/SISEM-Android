package com.valkiria.uicomponents.components.body

import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.model.ui.footer.FooterUiModel

data class FooterBodyUiModel(
    val identifier: String = "FOOTER",
    val leftButton: com.valkiria.uicomponents.components.body.ButtonUiModel,
    val rightButton: com.valkiria.uicomponents.components.body.ButtonUiModel? = null
) : com.valkiria.uicomponents.components.body.BodyRowModel {

    override val type: BodyRowType = BodyRowType.FOOTER
}

fun com.valkiria.uicomponents.components.body.FooterBodyUiModel.mapToSection() = FooterUiModel(
    leftButton = leftButton,
    rightButton = rightButton
)

package com.valkiria.uicomponents.components.termsandconditions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

data class TermsAndConditionsUiModel(
    override val identifier: String = "T&C",
    val visibility: Boolean = true,
    val required: Boolean = false,
    val links: List<Link> = emptyList(),
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.TERMS_AND_CONDITIONS
}

package com.valkiria.uicomponents.model.ui.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.link.Link

data class TermsAndConditionsUiModel(
    val identifier: String?,
    val links: List<Link> = emptyList(),
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.TERMS_AND_CONDITIONS
}

package com.skgtecnologia.sisem.domain.model.bodyrow

import com.skgtecnologia.sisem.domain.model.props.ButtonSize
import com.skgtecnologia.sisem.domain.model.props.ButtonStyle
import com.skgtecnologia.sisem.domain.model.props.MarginsModel
import com.skgtecnologia.sisem.domain.model.props.OnClick
import com.skgtecnologia.sisem.domain.model.props.mapToUiModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.BUTTON
import com.valkiria.uicomponents.props.button.ButtonUiModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ButtonModelNew(
    val identifier: String,
    val label: String,
    val style: ButtonStyle,
    val onClick: OnClick,
    val size: ButtonSize,
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = BUTTON
}

fun ButtonModelNew.mapToUiModel(): ButtonUiModel {
    return ButtonUiModel(
        label = label,
        style = style.mapToUiModel(),
        onClick = onClick.mapToUiModel(),
        size = size.mapToUiModel(),
        margins = margins?.mapToUiModel()
    )
}

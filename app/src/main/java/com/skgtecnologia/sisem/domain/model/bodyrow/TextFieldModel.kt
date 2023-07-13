package com.skgtecnologia.sisem.domain.model.bodyrow

import com.skgtecnologia.sisem.domain.model.props.KeyboardType
import com.skgtecnologia.sisem.domain.model.props.MarginsModel
import com.skgtecnologia.sisem.domain.model.props.ValidationModel
import com.skgtecnologia.sisem.domain.model.props.mapToUiModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.TEXT_FIELD
import com.valkiria.uicomponents.props.textfield.TextFieldUiModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextFieldModel(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    val keyboardType: KeyboardType,
    val validations: List<ValidationModel>,
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = TEXT_FIELD
}

fun TextFieldModel.mapToUiModel(): TextFieldUiModel {
    return TextFieldUiModel(
        identifier = identifier,
        icon = icon,
        hint = hint,
        keyboardType = keyboardType.mapToUiModel(),
        validations = validations.map { it.mapToUiModel() },
        margins = margins?.mapToUiModel()
    )
}

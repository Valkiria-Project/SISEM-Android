package com.skgtecnologia.sisem.domain.myscreen.model

import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextFieldModel(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    val keyboardType: KeyBoardType,
    val validations: List<ValidationModel>,
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = BodyRowType.TEXT_FIELD
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

package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowType
import com.skgtecnologia.sisem.domain.myscreen.model.KeyBoardType
import com.skgtecnologia.sisem.domain.myscreen.model.TextFieldModel

data class TextFieldResponse(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    val keyboardType: KeyBoardType,
    val validations: List<ValidationResponse>,
    val margins: MarginsResponse?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.TEXT_FIELD

    override fun mapToDomain(): BodyRowModel = TextFieldModel(
        identifier = identifier.orEmpty(),
        icon = icon.orEmpty(),
        hint = hint.orEmpty(),
        keyboardType = keyboardType,
        validations = validations.map { it.mapToDomain() },
        margins = margins?.mapToDomain()
    )
}

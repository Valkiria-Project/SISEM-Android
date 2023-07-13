package com.skgtecnologia.sisem.data.model.props

import com.skgtecnologia.sisem.domain.model.props.ValidationModel

data class ValidationResponse(
    val regex: String?,
    val message: String?
)

fun ValidationResponse.mapToDomain(): ValidationModel {
    return ValidationModel(
        regex = regex.orEmpty(),
        message = message.orEmpty()
    )
}

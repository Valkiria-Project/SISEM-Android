package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.core.model.props.ValidationModel

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

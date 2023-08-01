package com.skgtecnologia.sisem.domain.model.error

import com.valkiria.uicomponents.components.errorbanner.ErrorUiModel

data class ErrorModel(
    val icon: String,
    val title: String,
    val description: String
) : RuntimeException()

fun Throwable.mapToUi(): ErrorUiModel = (this as? ErrorModel)?.mapToUi() ?: ErrorUiModel(
    icon = "Default icon", // FIXME: Add Default
    title = "Default title", // FIXME: Add Default
    description = "Default description" // FIXME: Add Default
)

private fun ErrorModel.mapToUi() = ErrorUiModel(
    icon = icon,
    title = title,
    description = description
)

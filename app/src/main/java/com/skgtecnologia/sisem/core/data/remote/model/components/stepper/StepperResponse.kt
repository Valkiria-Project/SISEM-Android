package com.skgtecnologia.sisem.core.data.remote.model.components.stepper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.core.data.remote.model.screen.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.stepper.StepperUiModel

@JsonClass(generateAdapter = true)
data class StepperResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "options") val options: Map<String, String>?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.STEPPER

    override fun mapToUi(): StepperUiModel = StepperUiModel(
        identifier = identifier ?: error("Stepper identifier cannot be null"),
        options = options ?: error("Stepper options cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}

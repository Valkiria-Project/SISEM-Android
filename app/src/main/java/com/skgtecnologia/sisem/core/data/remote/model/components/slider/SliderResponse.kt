package com.skgtecnologia.sisem.core.data.remote.model.components.slider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.core.data.remote.model.screen.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.slider.SliderUiModel

@JsonClass(generateAdapter = true)
data class SliderResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "min") val min: Int?,
    @Json(name = "max") val max: Int?,
    @Json(name = "selected") val selected: Int?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.SLIDER

    override fun mapToUi(): SliderUiModel = SliderUiModel(
        identifier = identifier ?: error("Slider identifier cannot be null"),
        min = min ?: error("Slider min cannot be null"),
        max = max ?: error("Slider max cannot be null"),
        selected = selected ?: 0,
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}

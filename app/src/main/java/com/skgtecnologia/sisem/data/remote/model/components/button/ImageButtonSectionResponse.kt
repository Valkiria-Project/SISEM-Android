package com.skgtecnologia.sisem.data.remote.model.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.button.ImageButtonSectionUiModel

@JsonClass(generateAdapter = true)
data class ImageButtonSectionResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "options") val options: List<ImageButtonOptionResponse>?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.IMAGE_BUTTON_SECTION

    override fun mapToUi(): ImageButtonSectionUiModel = ImageButtonSectionUiModel(
        identifier = identifier ?: error("ImageButtonSection identifier cannot be null"),
        options = options?.map { it.mapToUi() }
            ?: error("ImageButtonSection options cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}

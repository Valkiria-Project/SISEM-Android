package com.skgtecnologia.sisem.data.remote.model.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUI
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.button.ImageButtonUiModel

@JsonClass(generateAdapter = true)
data class ImageButtonResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "image") val image: String?,
    @Json(name = "selected") val selected: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.IMAGE_BUTTON

    override fun mapToUi(): ImageButtonUiModel = ImageButtonUiModel(
        identifier = identifier ?: error("ImageButton identifier cannot be null"),
        title = title?.mapToUI(),
        image = image ?: error("ImageButton image cannot be null"),
        selected = selected ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}

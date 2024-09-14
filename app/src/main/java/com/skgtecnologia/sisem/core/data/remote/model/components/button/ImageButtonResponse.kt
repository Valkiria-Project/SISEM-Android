package com.skgtecnologia.sisem.core.data.remote.model.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.core.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.core.data.remote.model.components.label.mapToUi
import com.skgtecnologia.sisem.core.data.remote.model.screen.BodyRowResponse
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
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.IMAGE_BUTTON

    override fun mapToUi(): ImageButtonUiModel = ImageButtonUiModel(
        identifier = identifier ?: error("ImageButton identifier cannot be null"),
        title = title?.mapToUi(),
        image = image ?: error("ImageButton image cannot be null"),
        selected = selected ?: false,
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}

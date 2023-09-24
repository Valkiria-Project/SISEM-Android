package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.ImageButtonModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageButtonResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "image") val image: String?,
    @Json(name = "selected") val selected: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.IMAGE_BUTTON

    override fun mapToDomain(): ImageButtonModel = ImageButtonModel(
        identifier = identifier ?: error("ImageButton identifier cannot be null"),
        image = image ?: error("ImageButton image cannot be null"),
        selected = selected ?: error("ImageButton selected cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}

package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.ImageButtonOptionResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToDomain
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.ImageButtonSectionModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageButtonSectionResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "options") val options: List<ImageButtonOptionResponse>?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.IMAGE_BUTTON_SECTION

    override fun mapToDomain(): ImageButtonSectionModel = ImageButtonSectionModel(
        identifier = identifier ?: error("ImageButtonSection identifier cannot be null"),
        options = options?.map { it.mapToDomain() }
            ?: error("ImageButtonSection options cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}

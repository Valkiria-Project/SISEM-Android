package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.FindingModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FindingResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "option") val option: SegmentedSwitchResponse?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.FINDING

    override fun mapToDomain(): FindingModel = FindingModel(
        identifier = identifier ?: error("Finding identifier cannot be null"),
        option = option?.mapToDomain() ?: error("Finding option cannot be null"),
        modifier = modifier ?: Modifier
    )
}

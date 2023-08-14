package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.DetailedInfoBrickResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToUi
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.DetailedInfoListModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class DetailedInfoListResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "details") val details: List<DetailedInfoBrickResponse>?,
    @Json(name = "label_text_style") val labelTextStyle: TextStyle?,
    @Json(name = "text_text_style") val textTextStyle: TextStyle?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.DETAILED_INFO_LIST

    override fun mapToDomain(): DetailedInfoListModel = DetailedInfoListModel(
        identifier = identifier ?: error("Detailed info list identifier cannot be null"),
        details = details?.map { it.mapToUi() }
            ?: error("Detailed info list details cannot be null"),
        labelTextStyle = labelTextStyle
            ?: error("Detailed info list labelTextStyle cannot be null"),
        textTextStyle = textTextStyle
            ?: error("Detailed info list textTextStyle cannot be null"),
        modifier = modifier ?: Modifier
    )
}

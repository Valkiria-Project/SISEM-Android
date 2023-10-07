package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.DetailedInfoResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoListUiModel
import com.valkiria.uicomponents.model.props.TextStyle

@JsonClass(generateAdapter = true)
data class DetailedInfoListResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "details") val details: List<DetailedInfoResponse>?,
    @Json(name = "label_text_style") val labelTextStyle: TextStyle?,
    @Json(name = "text_text_style") val textTextStyle: TextStyle?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.DETAILED_INFO_LIST

    override fun mapToUi(): DetailedInfoListUiModel = DetailedInfoListUiModel(
        identifier = identifier ?: error("Detailed info list identifier cannot be null"),
        details = details?.map { it.mapToUi() }
            ?: error("Detailed info list details cannot be null"),
        labelTextStyle = labelTextStyle
            ?: error("Detailed info list labelTextStyle cannot be null"),
        textTextStyle = textTextStyle
            ?: error("Detailed info list textTextStyle cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}

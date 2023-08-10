package com.skgtecnologia.sisem.data.auth.remote.model

import com.skgtecnologia.sisem.domain.auth.model.TurnModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TurnResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "date") val date: String,
    @Json(name = "is_complete") val isComplete: Boolean
)

fun TurnResponse.mapToDomain(): TurnModel = TurnModel(
    id = id,
    date = date,
    isComplete = isComplete
)

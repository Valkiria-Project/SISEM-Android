package com.skgtecnologia.sisem.data.remote.model.error

import com.skgtecnologia.sisem.data.remote.model.bricks.banner.BannerResponse

data class ErrorResponse(
    val errorMessage: String,
    val bannerModel: BannerResponse? = null
) : Throwable()

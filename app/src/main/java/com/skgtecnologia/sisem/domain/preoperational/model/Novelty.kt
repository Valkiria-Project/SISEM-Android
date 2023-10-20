package com.skgtecnologia.sisem.domain.preoperational.model

import android.os.Parcelable
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Novelty(
    val idPreoperational: String,
    val novelty: String,
    val images: List<ImageModel>
) : Parcelable

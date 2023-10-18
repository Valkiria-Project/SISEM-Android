package com.skgtecnologia.sisem.domain.report.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageModel(
    val fileName: String,
    val file: String
) : Parcelable

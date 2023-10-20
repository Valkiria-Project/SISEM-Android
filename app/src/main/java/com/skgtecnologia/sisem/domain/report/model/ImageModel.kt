package com.skgtecnologia.sisem.domain.report.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class ImageModel(
    val fileName: String,
    val file: File
) : Parcelable

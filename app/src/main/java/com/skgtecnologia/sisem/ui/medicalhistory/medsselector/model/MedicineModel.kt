package com.skgtecnologia.sisem.ui.medicalhistory.medsselector.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MedicineModel(
    val title: String,
    val date: String,
    val specifications: List<String>
) : Parcelable

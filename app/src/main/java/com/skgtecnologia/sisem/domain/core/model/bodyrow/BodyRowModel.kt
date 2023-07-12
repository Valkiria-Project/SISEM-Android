package com.skgtecnologia.sisem.domain.core.model.bodyrow

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface BodyRowModel : Parcelable {
    val type: BodyRowType
}

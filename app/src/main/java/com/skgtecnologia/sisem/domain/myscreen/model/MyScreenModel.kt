package com.skgtecnologia.sisem.domain.myscreen.model

import android.os.Parcelable
import com.skgtecnologia.sisem.domain.core.model.bodyrow.BodyRowModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyScreenModel(
    val id: Long? = null,
    val type: ScreenType,
    val headerModel: HeaderModel,
    val body: List<BodyRowModel>,
    val footerModel: FooterModel
) : Parcelable

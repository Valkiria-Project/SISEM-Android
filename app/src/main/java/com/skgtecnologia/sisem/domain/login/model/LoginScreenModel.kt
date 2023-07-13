package com.skgtecnologia.sisem.domain.login.model

import android.os.Parcelable
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginScreenModel(
    val body: List<BodyRowModel>
) : Parcelable

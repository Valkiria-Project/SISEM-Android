package com.skgtecnologia.sisem.domain.loginscreen.model

import android.os.Parcelable
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginScreenModel(
		val body: List<BodyRowModel>
) : Parcelable

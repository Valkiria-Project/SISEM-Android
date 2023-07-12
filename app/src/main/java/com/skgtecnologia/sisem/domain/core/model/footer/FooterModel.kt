package com.skgtecnologia.sisem.domain.core.model.footer

import android.os.Parcelable
import com.skgtecnologia.sisem.domain.core.model.bodyrow.ButtonModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class FooterModel(
    val buttonModelList: List<ButtonModel>
) : Parcelable

package com.skgtecnologia.sisem.domain.myscreen.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class BodyRowType : Parcelable {
    BUTTON,
    CHIP,
    CROSS_SELLING,
    LABEL,
    MESSAGE,
    PAYMENT_METHOD_INFO,
    SECTION,
    TERMS_AND_CONDITIONS,
    TEXT_FIELD
}

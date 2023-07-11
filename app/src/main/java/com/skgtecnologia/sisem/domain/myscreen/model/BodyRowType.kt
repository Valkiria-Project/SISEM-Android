package com.skgtecnologia.sisem.domain.myscreen.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class BodyRowType : Parcelable {
    BUTTON,
    CHIP,
    CROSS_SELLING,
    MESSAGE,
    PAYMENT_METHOD_INFO,
    RICH_LABEL,
    SECTION,
    TERMS_AND_CONDITIONS,
    TEXT_FIELD
}

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
    PASSWORD_TEXT_FIELD,
    RICH_LABEL,
    TERMS_AND_CONDITIONS,
    TEXT_FIELD
}

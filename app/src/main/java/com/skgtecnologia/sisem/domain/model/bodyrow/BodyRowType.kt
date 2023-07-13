package com.skgtecnologia.sisem.domain.model.bodyrow

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class BodyRowType : Parcelable {
    BUTTON,
    CHIP,
    LABEL,
    LABELED_SWITCH,
    PASSWORD_TEXT_FIELD,
    RICH_LABEL,
    TERMS_AND_CONDITIONS,
    TEXT_FIELD
}

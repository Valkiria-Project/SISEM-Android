package com.skgtecnologia.sisem.domain.login.model

import java.util.Locale

enum class LoginLink {
    PRIVACY_POLICY,
    TERMS_AND_CONDITIONS;

    companion object {
        fun getLinkByName(link: String): LoginLink =
            enumValueOf<LoginLink>(link.uppercase(Locale.ROOT))
    }
}

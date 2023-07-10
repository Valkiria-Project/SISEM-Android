package com.skgtecnologia.sisem.domain.myscreen.model

enum class OnClickType {
    FORGOT_PASSWORD,
    LOGIN;

    fun mapToUiModel(): com.valkiria.uicomponents.components.button.OnClickType {
        return when (this) {
            FORGOT_PASSWORD -> com.valkiria.uicomponents.components.button.OnClickType.FORGOT_PASSWORD
            LOGIN -> com.valkiria.uicomponents.components.button.OnClickType.LOGIN
        }
    }
}
package com.skgtecnologia.sisem.domain.model.props

enum class OnClickType {
    FORGOT_PASSWORD,
    LOGIN;

    // FIXME
    fun mapToUiModel(): com.valkiria.uicomponents.components.button.OnClickType {
        return when (this) {
            FORGOT_PASSWORD ->
                com.valkiria.uicomponents.components.button.OnClickType.FORGOT_PASSWORD

            LOGIN -> com.valkiria.uicomponents.components.button.OnClickType.LOGIN
        }
    }
}

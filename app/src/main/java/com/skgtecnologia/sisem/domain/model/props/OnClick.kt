package com.skgtecnologia.sisem.domain.model.props

enum class OnClick {
    FORGOT_PASSWORD,
    LOGIN;

    // FIXME
    fun mapToUiModel(): com.valkiria.uicomponents.props.button.OnClickType {
        return when (this) {
            FORGOT_PASSWORD -> com.valkiria.uicomponents.props.button.OnClickType.FORGOT_PASSWORD
            LOGIN -> com.valkiria.uicomponents.props.button.OnClickType.LOGIN
        }
    }
}

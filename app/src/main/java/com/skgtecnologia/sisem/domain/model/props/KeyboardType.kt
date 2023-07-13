package com.skgtecnologia.sisem.domain.model.props

enum class KeyboardType {
    EMAIL,
    NUMBER,
    PASSWORD,
    PHONE,
    TEXT;

    // FIXME
    fun mapToUiModel(): com.valkiria.uicomponents.props.textfield.KeyBoardUiType {
        return when (this) {
            EMAIL -> com.valkiria.uicomponents.props.textfield.KeyBoardUiType.EMAIL
            NUMBER -> com.valkiria.uicomponents.props.textfield.KeyBoardUiType.NUMBER
            PASSWORD -> com.valkiria.uicomponents.props.textfield.KeyBoardUiType.PASSWORD
            PHONE -> com.valkiria.uicomponents.props.textfield.KeyBoardUiType.PHONE
            TEXT -> com.valkiria.uicomponents.props.textfield.KeyBoardUiType.TEXT
        }
    }
}

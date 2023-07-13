package com.skgtecnologia.sisem.domain.model.props

enum class KeyboardType {
    EMAIL,
    NUMBER,
    PASSWORD,
    PHONE,
    TEXT;

    // FIXME
    fun mapToUiModel(): com.valkiria.uicomponents.components.textfield.KeyBoardUiType {
        return when (this) {
            EMAIL -> com.valkiria.uicomponents.components.textfield.KeyBoardUiType.EMAIL
            NUMBER -> com.valkiria.uicomponents.components.textfield.KeyBoardUiType.NUMBER
            PASSWORD -> com.valkiria.uicomponents.components.textfield.KeyBoardUiType.PASSWORD
            PHONE -> com.valkiria.uicomponents.components.textfield.KeyBoardUiType.PHONE
            TEXT -> com.valkiria.uicomponents.components.textfield.KeyBoardUiType.TEXT
        }
    }
}

package com.skgtecnologia.sisem.domain.myscreen.model

enum class KeyboardType {
    TEXT,
    NUMBER,
    EMAIL,
    PHONE,
    PASSWORD;

    fun mapToUiModel(): com.valkiria.uicomponents.components.textfield.KeyBoardUiType {
        return when (this) {
            TEXT -> com.valkiria.uicomponents.components.textfield.KeyBoardUiType.TEXT
            NUMBER -> com.valkiria.uicomponents.components.textfield.KeyBoardUiType.NUMBER
            EMAIL -> com.valkiria.uicomponents.components.textfield.KeyBoardUiType.EMAIL
            PHONE -> com.valkiria.uicomponents.components.textfield.KeyBoardUiType.PHONE
            PASSWORD -> com.valkiria.uicomponents.components.textfield.KeyBoardUiType.PASSWORD
        }
    }
}
package com.skgtecnologia.sisem.domain.model.props

import com.valkiria.uicomponents.props.KeyBoardUiType

enum class KeyboardType {
    EMAIL,
    NUMBER,
    PASSWORD,
    PHONE,
    TEXT;

    // FIXME
    fun mapToUiModel(): KeyBoardUiType {
        return when (this) {
            EMAIL -> KeyBoardUiType.EMAIL
            NUMBER -> KeyBoardUiType.NUMBER
            PASSWORD -> KeyBoardUiType.PASSWORD
            PHONE -> KeyBoardUiType.PHONE
            TEXT -> KeyBoardUiType.TEXT
        }
    }
}

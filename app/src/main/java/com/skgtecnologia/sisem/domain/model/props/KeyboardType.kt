package com.skgtecnologia.sisem.domain.model.props

import com.valkiria.uicomponents.props.KeyBoardType

enum class KeyboardType {
    EMAIL,
    NUMBER,
    PASSWORD,
    PHONE,
    TEXT;

    // FIXME
    fun mapToUiModel(): KeyBoardType {
        return when (this) {
            EMAIL -> KeyBoardType.EMAIL
            NUMBER -> KeyBoardType.NUMBER
            PASSWORD -> KeyBoardType.PASSWORD
            PHONE -> KeyBoardType.PHONE
            TEXT -> KeyBoardType.TEXT
        }
    }
}

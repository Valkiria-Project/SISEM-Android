package com.skgtecnologia.sisem.domain.model.footer

import com.skgtecnologia.sisem.domain.model.body.ButtonModel

data class FooterModel(
    val leftButton: ButtonModel,
    val rightButton: ButtonModel?
)

package com.skgtecnologia.sisem.commons.extensions

import kotlin.contracts.contract

fun validateOrThrow(validation: Boolean, exceptionCallback: () -> Exception) {
    contract {
        returns() implies validation
    }
    if (!validation) {
        throw exceptionCallback()
    }
}

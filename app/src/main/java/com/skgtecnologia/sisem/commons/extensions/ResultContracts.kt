package com.skgtecnologia.sisem.commons.extensions

import kotlin.contracts.contract

fun validateOrThrow(value: Boolean, exception: () -> Exception) {
    contract {
        returns() implies value
    }
    if (!value) {
        throw exception()
    }
}

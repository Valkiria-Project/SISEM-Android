package com.skgtecnologia.sisem.commons.extensions

import kotlin.contracts.contract

fun check(validation: Boolean, exception: () -> Exception) {
    contract {
        returns() implies validation
    }
    if (!validation) {
        throw exception()
    }
}

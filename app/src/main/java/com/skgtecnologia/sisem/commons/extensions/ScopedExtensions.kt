package com.skgtecnologia.sisem.commons.extensions

fun <F : Any, S : Any, R : Any> biLet(first: F?, second: S?, block: (F, S) -> R?): R? =
    if (first != null && second != null) {
        block(first, second)
    } else {
        null
    }

package com.valkiria.uicomponents.utlis

import android.content.Context

@Suppress("DiscouragedApi", "SwallowedException", "TooGenericExceptionCaught")
fun Context.getResourceIdByName(name: String, defType: DefType) = try {
    val resourceId = resources.getIdentifier(name, defType.type, packageName)
    check(resourceId != 0)
    resourceId
} catch (e: Exception) {
    null
}

enum class DefType(val type: String) {
    DRAWABLE("drawable")
}

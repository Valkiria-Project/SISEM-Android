package com.valkiria.uicomponents.utlis

import android.content.Context

enum class DefType(val type: String) {
    DRAWABLE("drawable")
}

@Suppress("DiscouragedApi")
fun Context.getResourceIdByName(name: String, defType: DefType) = try {
    val resourceId = resources.getIdentifier(name, defType.type, packageName)
    check(resourceId != 0)
    resourceId
} catch (e: Exception) {
    null
}

package com.valkiria.uicomponents.components.humanbody

data class HumanBodyUi(
    val type: String,
    val area: String,
    val wounds: List<String>
)

enum class HumanBodyType {
    BACK,
    FRONT
}

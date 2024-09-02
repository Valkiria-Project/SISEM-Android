package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.chip.ChipStyle
import com.valkiria.uicomponents.components.chip.ChipUiModel
import com.valkiria.uicomponents.components.label.TextStyle

fun getLoginChipUiModel(): ChipUiModel {
    return ChipUiModel(
        identifier = "LOGIN_CHIP",
        icon = "ic_ambulance",
        text = "5421244",
        textStyle = TextStyle.HEADLINE_5,
        style = ChipStyle.PRIMARY,
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 0.dp,
            top = 20.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    )
}

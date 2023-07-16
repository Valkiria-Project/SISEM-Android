package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.chip.ChipUiModel
import com.valkiria.uicomponents.props.ChipStyle
import com.valkiria.uicomponents.props.TextStyle

fun getLoginChipUiModel(): ChipUiModel {
    /*
    {
        "identifier":"LOGIN_CODE_CHIP",
        "icon":"ic_ambulance",
        "text":"5421244",
        "text_style":"HEADLINE_5",
        "style":"PRIMARY",
        "type":"CHIP",
        "margins":{"top":20,"left":0,"right":0,"bottom":0}
    }
    */
    return ChipUiModel(
        icon = "ic_ambulance",
        text = "5421244",
        textStyle = TextStyle.HEADLINE_5,
        style = ChipStyle.PRIMARY,
        modifier = Modifier.padding(
            start = 0.dp,
            top = 20.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    )
}

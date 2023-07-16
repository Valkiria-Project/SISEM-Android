package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.props.TextStyle

fun getLoginWelcomeRichLabelUiModel(): RichLabelUiModel {
    /*
    {
       "identifier":"LOGIN_TITLE",
       "text":"Bienvenido al <b>SISEM</b>",
       "text_style":"HEADLINE_1",
       "type":"RICH_LABEL",
       "margins":{
            "top":20,
            "left":20,
            "right":20,
            "bottom":0
        }
    }
    */
    return RichLabelUiModel(
        text = "<font color=\"#FFFFFF\">Bienvenido al <b>SISEM</b></font>",
        textStyle = TextStyle.HEADLINE_1,
        modifier = Modifier.padding(
            start = 20.dp,
            top = 20.dp,
            end = 20.dp,
            bottom = 0.dp
        )
    )
}

package com.valkiria.uicomponents.mocks

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.props.TextStyle

fun getLoginRichLabelUiModel(): RichLabelUiModel {
    /*
    {
       "identifier":"LOGIN_TITLE",
       "text":"Bienvenido al <b>SISEM</b>",
       "text_style":"HEADLINE_1",
       "type":"RICH_LABEL",
       "margins":{"top":20,"left":20,"right":20,"bottom":0}
    }
    */
    return RichLabelUiModel(
        text = "<font color=\"#FFFFFF\">Bienvenido al <b>SISEM</b></font>",
        textStyle = TextStyle.HEADLINE_1,
        modifier = Modifier
    )
}

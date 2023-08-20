package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.props.TextStyle

fun getLoginWelcomeRichLabelUiModel(): RichLabelUiModel {
    return RichLabelUiModel(
        text = "<font color=\"#FFFFFF\">Bienvenido al <b>SISEM</b></font>",
        textStyle = TextStyle.HEADLINE_1,
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 20.dp,
            top = 20.dp,
            end = 20.dp,
            bottom = 0.dp
        )
    )
}

fun getAlprazolamRichLabelUiModel(): RichLabelUiModel {
    return RichLabelUiModel(
        text = "<font color=\"#FFFFFF\"><b>Alprazolam</b><br>TAB 0.5 MG *",
        textStyle = TextStyle.HEADLINE_6,
        arrangement = Arrangement.Start
    )
}

fun getDiazepamRichLabelUiModel(): RichLabelUiModel {
    return RichLabelUiModel(
        text = "<font color=\"#FFFFFF\"><b>Diazepam</b><br>Sol iny 1 MG/MLAMP1*",
        textStyle = TextStyle.HEADLINE_6,
        arrangement = Arrangement.Start
    )
}

@file:Suppress("MagicNumber")

package com.valkiria.uicomponents.model.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.body.RichLabelUiModel
import kotlin.random.Random

fun getLoginWelcomeRichLabelUiModel(): RichLabelUiModel {
    return RichLabelUiModel(
        identifier = Random(100).toString(),
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
        identifier = Random(100).toString(),
        text = "<font color=\"#FFFFFF\"><b>Alprazolam</b><br>TAB 0.5 MG *",
        textStyle = TextStyle.HEADLINE_6,
        arrangement = Arrangement.Start
    )
}

fun getDiazepamRichLabelUiModel(): RichLabelUiModel {
    return RichLabelUiModel(
        identifier = Random(100).toString(),
        text = "<font color=\"#FFFFFF\"><b>Diazepam</b><br>Sol iny 1 MG/MLAMP1*",
        textStyle = TextStyle.HEADLINE_6,
        arrangement = Arrangement.Start
    )
}

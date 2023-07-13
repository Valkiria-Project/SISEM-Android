package com.valkiria.uicomponents.components.richlabel

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.HtmlCompat
import com.valkiria.uicomponents.components.chip.ChipComponent
import com.valkiria.uicomponents.components.chip.ChipUiModel
import com.valkiria.uicomponents.props.ChipStyleUi
import com.valkiria.uicomponents.props.TextStyleUi
import com.valkiria.uicomponents.theme.lobsterTwoFontFamily
import com.valkiria.uicomponents.theme.montserratFontFamily

@Composable
fun RichLabelComponent(
    uiModel: RichLabelUiModel,
    modifier: Modifier = Modifier
) {
    Text(
        text = HtmlCompat.fromHtml(uiModel.text, HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
        modifier = modifier
    )
}

@Preview
@Composable
fun ChipComponentPreview() {
    /*
    {
        "identifier":"LOGIN_TITLE",
        "text":"Bienvenido al <b>SISEM</b>",
        "text_style":"HEADLINE_1",
        "type":"RICH_LABEL",
        "margins":{"top":20,"left":20,"right":20,"bottom":0}
    }
    */
    val chipUiModel = ChipUiModel(
        icon = "ic_ambulance",
        text = "5421244",
        textStyle = TextStyleUi.HEADLINE_5,
        style = ChipStyleUi.PRIMARY,
        margins = null
    )
    Column {
        ChipComponent(
            uiModel = chipUiModel,
            fontFamily = montserratFontFamily
        )
        ChipComponent(
            uiModel = chipUiModel,
            fontFamily = lobsterTwoFontFamily
        )
    }
}

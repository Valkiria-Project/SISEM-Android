package com.valkiria.uicomponents.components.richlabel

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ireward.htmlcompose.HtmlText
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.theme.Green40
import timber.log.Timber

@Composable
fun RichLabelComponent(
    uiModel: RichLabelUiModel,
    modifier: Modifier = Modifier
) {
    HtmlText(
        text = "<font color=\"#FFFFFF\">${uiModel.text}</font>",
        style = MaterialTheme.typography.headlineSmall,
        linkClicked = { link ->
            Timber.tag("linkClicked").d(link)
        }
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF00A650
)
@Composable
fun RichLabelComponentPreview() {
    /*
    {
        "identifier":"LOGIN_TITLE",
        "text":"Bienvenido al <b>SISEM</b>",
        "text_style":"HEADLINE_1",
        "type":"RICH_LABEL",
        "margins":{"top":20,"left":20,"right":20,"bottom":0}
    }
    */
    val richLabelUiModel = RichLabelUiModel(
        text = "<font color=\"#FFFFFF\">Bienvenido al <b>SISEM</b></font>",
        textStyle = TextStyle.HEADLINE_1,
        modifier = Modifier
    )
    Column(
        modifier = Modifier.background(Green40)
    ) {
        RichLabelComponent(
            uiModel = richLabelUiModel,
            modifier = Modifier.padding(16.dp)
        )
        RichLabelComponent(
            uiModel = richLabelUiModel,
            modifier = Modifier.padding(0.dp)
        )
    }
}

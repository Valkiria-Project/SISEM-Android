package com.valkiria.uicomponents.components.richlabel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ireward.htmlcompose.HtmlText
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.props.toTextStyle
import timber.log.Timber

@Composable
fun RichLabelComponent(
    uiModel: RichLabelUiModel,
    modifier: Modifier = Modifier
) {
    HtmlText(
        text = "<font color=\"#FFFFFF\">${uiModel.text}</font>",
        style = uiModel.textStyle.toTextStyle(),
        linkClicked = { link ->
            Timber.tag("linkClicked").d(link)
        }
    )
}

@Preview(showBackground = true)
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
    LazyColumn(
        modifier = Modifier.background(Color.DarkGray),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            RichLabelComponent(
                uiModel = richLabelUiModel,
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            RichLabelComponent(
                uiModel = richLabelUiModel.copy(
                    textStyle = TextStyle.HEADLINE_2
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            RichLabelComponent(
                uiModel = richLabelUiModel.copy(
                    textStyle = TextStyle.HEADLINE_3
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            RichLabelComponent(
                uiModel = richLabelUiModel.copy(
                    textStyle = TextStyle.HEADLINE_4
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            RichLabelComponent(
                uiModel = richLabelUiModel.copy(
                    textStyle = TextStyle.HEADLINE_5
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            RichLabelComponent(
                uiModel = richLabelUiModel.copy(
                    textStyle = TextStyle.HEADLINE_6
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            RichLabelComponent(
                uiModel = richLabelUiModel.copy(
                    textStyle = TextStyle.HEADLINE_7
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            RichLabelComponent(
                uiModel = richLabelUiModel.copy(
                    textStyle = TextStyle.HEADLINE_8
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            RichLabelComponent(
                uiModel = richLabelUiModel.copy(
                    textStyle = TextStyle.BODY_1
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            RichLabelComponent(
                uiModel = richLabelUiModel.copy(
                    textStyle = TextStyle.BUTTON_1
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            RichLabelComponent(
                uiModel = richLabelUiModel.copy(
                    textStyle = TextStyle.BUTTON_2
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

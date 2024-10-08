package com.valkiria.uicomponents.components.richlabel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ireward.htmlcompose.HtmlText
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.mocks.getLoginWelcomeRichLabelUiModel
import timber.log.Timber

@Composable
fun RichLabelComponent(
    uiModel: RichLabelUiModel
) {
    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement
    ) {
        HtmlText(
            text = uiModel.text,
            style = uiModel.textStyle.toTextStyle(),
            linkClicked = { link ->
                Timber.tag("linkClicked").d(link)
            }
        )
    }
}

@Suppress("LongMethod")
@Preview(showBackground = true)
@Composable
fun RichLabelComponentPreview() {
    LazyColumn(
        modifier = Modifier.background(Color.DarkGray),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            RichLabelComponent(
                uiModel = getLoginWelcomeRichLabelUiModel()
            )
        }
        item {
            RichLabelComponent(
                uiModel = getLoginWelcomeRichLabelUiModel().copy(
                    textStyle = TextStyle.HEADLINE_2
                )
            )
        }
        item {
            RichLabelComponent(
                uiModel = getLoginWelcomeRichLabelUiModel().copy(
                    textStyle = TextStyle.HEADLINE_3
                )
            )
        }
        item {
            RichLabelComponent(
                uiModel = getLoginWelcomeRichLabelUiModel().copy(
                    textStyle = TextStyle.HEADLINE_4
                )
            )
        }
        item {
            RichLabelComponent(
                uiModel = getLoginWelcomeRichLabelUiModel().copy(
                    textStyle = TextStyle.HEADLINE_5
                )
            )
        }
        item {
            RichLabelComponent(
                uiModel = getLoginWelcomeRichLabelUiModel().copy(
                    textStyle = TextStyle.HEADLINE_6
                )
            )
        }
        item {
            RichLabelComponent(
                uiModel = getLoginWelcomeRichLabelUiModel().copy(
                    textStyle = TextStyle.HEADLINE_7
                )
            )
        }
        item {
            RichLabelComponent(
                uiModel = getLoginWelcomeRichLabelUiModel().copy(
                    textStyle = TextStyle.HEADLINE_8
                )
            )
        }
        item {
            RichLabelComponent(
                uiModel = getLoginWelcomeRichLabelUiModel().copy(
                    textStyle = TextStyle.BODY_1
                )
            )
        }
        item {
            RichLabelComponent(
                uiModel = getLoginWelcomeRichLabelUiModel().copy(
                    textStyle = TextStyle.BUTTON_1
                )
            )
        }
        item {
            RichLabelComponent(
                uiModel = getLoginWelcomeRichLabelUiModel().copy(
                    textStyle = TextStyle.BUTTON_2
                )
            )
        }
    }
}

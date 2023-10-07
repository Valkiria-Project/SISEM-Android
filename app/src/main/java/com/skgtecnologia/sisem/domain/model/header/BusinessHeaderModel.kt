package com.skgtecnologia.sisem.domain.model.header

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.body.HeaderUiModel
import com.valkiria.uicomponents.model.props.TextModel
import com.valkiria.uicomponents.model.props.TextStyle

fun addFindingHeader(
    titleText: String,
    subtitleText: String,
    leftIcon: String
): HeaderUiModel = HeaderUiModel(
    identifier = "ADD_FINDING_HEADER",
    title = TextModel(
        text = titleText,
        textStyle = TextStyle.HEADLINE_1
    ),
    subtitle = TextModel(
        text = subtitleText,
        textStyle = TextStyle.HEADLINE_5
    ),
    leftIcon = leftIcon,
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)

fun findingsHeader(
    titleText: String,
    subtitleText: String,
    leftIcon: String
): HeaderUiModel = HeaderUiModel(
    identifier = "FINDINGS_HEADER",
    title = TextModel(
        text = titleText,
        textStyle = TextStyle.HEADLINE_1
    ),
    subtitle = TextModel(
        text = subtitleText,
        textStyle = TextStyle.HEADLINE_5
    ),
    leftIcon = leftIcon,
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)

fun imagesConfirmationHeader(
    titleText: String,
    subtitleText: String,
    leftIcon: String
): HeaderUiModel = HeaderUiModel(
    identifier = "IMAGES_CONFIRMATION_HEADER",
    title = TextModel(
        text = titleText,
        textStyle = TextStyle.HEADLINE_1
    ),
    subtitle = TextModel(
        text = subtitleText,
        textStyle = TextStyle.HEADLINE_5
    ),
    leftIcon = leftIcon,
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)

fun woundsHeader(
    titleText: String,
    subtitleText: String,
    leftIcon: String
): HeaderUiModel = HeaderUiModel(
    identifier = "WOUNDS_HEADER",
    title = TextModel(
        text = titleText,
        textStyle = TextStyle.HEADLINE_1
    ),
    subtitle = TextModel(
        text = subtitleText,
        textStyle = TextStyle.HEADLINE_5
    ),
    leftIcon = leftIcon,
    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
)

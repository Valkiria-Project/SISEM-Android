package com.skgtecnologia.sisem.domain.model.header

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.header.HeaderUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel

fun addFindingHeader(
    titleText: String,
    subtitleText: String,
    leftIcon: String
): HeaderUiModel = HeaderUiModel(
    identifier = "ADD_FINDING_HEADER",
    title = TextUiModel(
        text = titleText,
        textStyle = TextStyle.HEADLINE_1
    ),
    subtitle = TextUiModel(
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
    title = TextUiModel(
        text = titleText,
        textStyle = TextStyle.HEADLINE_1
    ),
    subtitle = TextUiModel(
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
    title = TextUiModel(
        text = titleText,
        textStyle = TextStyle.HEADLINE_1
    ),
    subtitle = TextUiModel(
        text = subtitleText,
        textStyle = TextStyle.HEADLINE_5
    ),
    leftIcon = leftIcon,
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)

fun signaturePadHeader(
    titleText: String,
    subtitleText: String,
    leftIcon: String
): HeaderUiModel = HeaderUiModel(
    identifier = "SIGNATURE_PAD_HEADER",
    title = TextUiModel(
        text = titleText,
        textStyle = TextStyle.HEADLINE_1
    ),
    subtitle = TextUiModel(
        text = subtitleText,
        textStyle = TextStyle.HEADLINE_5
    ),
    leftIcon = leftIcon,
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)

fun emptyStretcherRetentionHeader(): HeaderUiModel = HeaderUiModel(
    identifier = "EMPTY_STRETCHER_RETENTION_HEADER",
    title = TextUiModel(
        text = "Registrar retención de camilla",
        textStyle = TextStyle.HEADLINE_1
    ),
    subtitle = TextUiModel(
        text = "Seleccione el paciente para la retención de camilla",
        textStyle = TextStyle.HEADLINE_5
    ),
    leftIcon = "ic_back",
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)

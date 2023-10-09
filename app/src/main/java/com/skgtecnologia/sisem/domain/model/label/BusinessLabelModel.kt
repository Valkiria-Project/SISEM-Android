package com.skgtecnologia.sisem.domain.model.label

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.TextStyle

fun addFilesHint(
    text: String,
): LabelUiModel = LabelUiModel(
    identifier = "ADD_FILES_HINT",
    text = text,
    textStyle = TextStyle.HEADLINE_3,
    arrangement = Arrangement.Start,
    modifier = Modifier.padding(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = 0.dp
    )
)

fun addReportDescription(
    text: String,
): LabelUiModel = LabelUiModel(
    identifier = "ADD_REPORT_DESCRIPTION",
    text = text,
    textStyle = TextStyle.HEADLINE_3,
    arrangement = Arrangement.Start,
    modifier = Modifier.padding(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = 0.dp
    )
)

fun addReportTopic(
    text: String,
): LabelUiModel = LabelUiModel(
    identifier = "ADD_REPORT_TOPIC",
    text = text,
    textStyle = TextStyle.HEADLINE_3,
    arrangement = Arrangement.Start,
    modifier = Modifier.padding(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = 0.dp
    )
)

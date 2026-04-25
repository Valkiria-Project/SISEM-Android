package com.skgtecnologia.sisem.domain.model.label

import android.graphics.Color.parseColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.TextStyle

private const val ROUNDED_CORNER_SHAPE_PERCENTAGE = 90

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

fun emptyStretcherRetentionMessage(): LabelUiModel = LabelUiModel(
    identifier = "EMPTY_STRETCHER_RETENTION_DESCRIPTION",
    text = "No hay incidentes aún para el turno actual",
    textStyle = TextStyle.HEADLINE_5,
    arrangement = Arrangement.Start,
    modifier = Modifier.padding(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = 0.dp
    )
)

fun sendEmailLabels(
    text: String,
    textColor: String = "#77797E"
): LabelUiModel = LabelUiModel(
    identifier = "FROM_SEND_EMAIL",
    text = text,
    textColor = textColor,
    textStyle = TextStyle.HEADLINE_5,
    arrangement = Arrangement.Start,
    modifier = Modifier.padding(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = 0.dp
    )
)

fun sendEmailContent(
    text: String
): LabelUiModel = LabelUiModel(
    identifier = "FROM_SEND_CONTENT_EMAIL",
    text = text,
    textStyle = TextStyle.HEADLINE_5,
    arrangement = Arrangement.Start,
    modifier = Modifier
        .padding(
            start = 20.dp,
            top = 40.dp,
            end = 20.dp,
            bottom = 0.dp
        )
        .border(
            BorderStroke(2.dp, Color(parseColor("#42A4FA"))),
            RoundedCornerShape(ROUNDED_CORNER_SHAPE_PERCENTAGE)
        )
        .padding(horizontal = 20.dp, vertical = 4.dp)
)

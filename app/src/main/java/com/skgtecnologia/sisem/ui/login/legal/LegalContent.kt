package com.skgtecnologia.sisem.ui.login.legal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.login.model.LegalContentModel
import com.valkiria.uicomponents.components.label.toTextStyle

@Composable
fun LegalContent(
    uiModel: LegalContentModel
) {
    Row(
        modifier = Modifier.padding(20.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        uiModel.icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(42.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = uiModel.title,
            style = uiModel.titleTextStyle.toTextStyle()
        )
    }
    uiModel.subtitle?.let {
        Text(
            text = it,
            modifier = Modifier.padding(20.dp),
            style = uiModel.subtitleTextStyle?.toTextStyle() ?: TextStyle.Default
        )
    }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Text(
            text = uiModel.text,
            modifier = Modifier.padding(20.dp),
            style = uiModel.textStyle.toTextStyle()
        )
    }
}

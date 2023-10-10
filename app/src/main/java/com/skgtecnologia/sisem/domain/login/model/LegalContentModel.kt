package com.skgtecnologia.sisem.domain.login.model

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.valkiria.uicomponents.components.label.TextStyle

data class LegalContentModel(
    val icon: ImageVector? = null,
    val title: String,
    val titleTextStyle: TextStyle,
    val subtitle: String? = null,
    val subtitleTextStyle: TextStyle? = null,
    val text: String,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
)

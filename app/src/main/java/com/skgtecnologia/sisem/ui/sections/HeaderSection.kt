package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Composable
fun HeaderSection(
    headerModel: HeaderModel,
    modifier: Modifier
) {
    Column(
        modifier = headerModel.modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconResourceId = LocalContext.current.getResourceIdByName(
                headerModel.leftIcon.orEmpty(), DefType.DRAWABLE
            )

            iconResourceId?.let {
                Icon(
                    painter = painterResource(id = iconResourceId),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(42.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            LabelComponent(
                uiModel = LabelUiModel(
                    text = headerModel.title.text,
                    textStyle = headerModel.title.textStyle
                )
            )
        }

        headerModel.subtitle?.let {
            LabelComponent(
                uiModel = LabelUiModel(
                    text = it.text,
                    textStyle = it.textStyle,
                )
            )
        }
    }
}

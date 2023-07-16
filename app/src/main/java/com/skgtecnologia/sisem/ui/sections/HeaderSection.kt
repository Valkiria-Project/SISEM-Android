package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.valkiria.uicomponents.components.richlabel.RichLabelComponent
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Composable
fun HeaderSection(
    headerModel: HeaderModel,
    modifier: Modifier
) {
    Column(
        modifier = headerModel.modifier.fillMaxWidth(),
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
                        .padding(end = 13.dp)
                        .size(42.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            headerModel.title?.let {
                RichLabelComponent(
                    uiModel = RichLabelUiModel(
                        text = it.text,
                        textStyle = it.textStyle,
                    )
                )
            }
        }

        headerModel.subtitle?.let {
            RichLabelComponent(
                uiModel = RichLabelUiModel(
                    text = it.text,
                    textStyle = it.textStyle,
                )
            )
        }
    }
}
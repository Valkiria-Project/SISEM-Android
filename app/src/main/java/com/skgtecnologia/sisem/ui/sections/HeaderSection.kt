package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.valkiria.uicomponents.model.ui.body.HeaderUiModel
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.model.ui.label.LabelUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Suppress("LongMethod")
@Composable
fun HeaderSection(
    headerUiModel: HeaderUiModel,
    modifier: Modifier = Modifier,
    onAction: (actionInput: UiAction) -> Unit = {}
) {
    Column(
        modifier = headerUiModel.modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val leftIconResourceId = LocalContext.current.getResourceIdByName(
                headerUiModel.leftIcon.orEmpty(), DefType.DRAWABLE
            )

            leftIconResourceId?.let {
                Icon(
                    painter = painterResource(id = leftIconResourceId),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { onAction(HeaderUiAction.GoBack) }
                        .padding(end = 12.dp)
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            LabelComponent(
                uiModel = LabelUiModel(
                    text = headerUiModel.title.text,
                    textStyle = headerUiModel.title.textStyle,
                    arrangement = Arrangement.Start
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            val rightIconResourceId = LocalContext.current.getResourceIdByName(
                headerUiModel.rightIcon.orEmpty(), DefType.DRAWABLE
            )

            rightIconResourceId?.let {
                Icon(
                    painter = painterResource(id = rightIconResourceId),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        headerUiModel.subtitle?.let {
            LabelComponent(
                uiModel = LabelUiModel(
                    text = it.text,
                    textStyle = it.textStyle,
                    modifier = Modifier.padding(top = 12.dp)
                )
            )
        }
    }
}

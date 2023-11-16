package com.valkiria.uicomponents.components.label

import android.graphics.Color.parseColor
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.mocks.getDeviceAuthLicensePlateLabelUiModel
import com.valkiria.uicomponents.mocks.getDeviceAuthSerialLabelUiModel
import com.valkiria.uicomponents.mocks.getMediaActionsFileUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName
import timber.log.Timber

@Composable
fun LabelComponent(
    uiModel: LabelUiModel,
    modifier: Modifier = Modifier,
    onAction: (id: String) -> Unit = {}
) {
    Row(
        modifier = uiModel.modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = uiModel.arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = uiModel.text,
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            color = Color(parseColor(uiModel.textColor)),
            style = uiModel.textStyle.toTextStyle()
        )

        val rightIconResourceId = LocalContext.current.getResourceIdByName(
            uiModel.rightIcon.orEmpty(), DefType.DRAWABLE
        )

        rightIconResourceId?.let {
            Icon(
                imageVector = ImageVector.vectorResource(id = rightIconResourceId),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        Timber.d("Clicked on ${uiModel.text}")
                        onAction(uiModel.identifier)
                    },
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun LabelComponentPreview() {
    Column {
        LabelComponent(
            uiModel = getDeviceAuthSerialLabelUiModel(),
            modifier = Modifier.padding(16.dp)
        )
        LabelComponent(
            uiModel = getDeviceAuthLicensePlateLabelUiModel(),
            modifier = Modifier.padding(0.dp)
        )
        LabelComponent(
            uiModel = getMediaActionsFileUiModel(),
            modifier = Modifier.padding(0.dp)
        )
    }
}

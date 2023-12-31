package com.valkiria.uicomponents.components.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.mocks.getLoginChipUiModel
import com.valkiria.uicomponents.utlis.DefType.DRAWABLE
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Composable
fun ChipComponent(
    uiModel: ChipUiModel,
    onAction: (value: String) -> Unit = { }
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.icon.orEmpty(), DRAWABLE
    )

    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement
    ) {
        AssistChip(
            onClick = { onAction(uiModel.text) },
            label = {
                Text(
                    text = uiModel.text,
                    style = uiModel.textStyle.toTextStyle()
                )
            },
            leadingIcon = {
                iconResourceId?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = iconResourceId),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            },
            shape = RoundedCornerShape(25.dp),
            colors = uiModel.style.toChipColors(),
            border = uiModel.style.toChipBorder()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChipComponentPreview() {
    Column(
        modifier = Modifier
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        ChipComponent(
            uiModel = getLoginChipUiModel()
        )
        ChipComponent(
            uiModel = getLoginChipUiModel().copy(
                style = ChipStyle.SECONDARY
            )
        )
    }
}

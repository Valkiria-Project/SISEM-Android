package com.valkiria.uicomponents.components.detailedinfolist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.mocks.getPreOperationalDetailedInfoListUiModel
import com.valkiria.uicomponents.props.TabletWidth
import com.valkiria.uicomponents.props.toTextStyle
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Composable
fun DetailedInfoListComponent(
    uiModel: DetailedInfoListUiModel,
    isTablet: Boolean = false
) {
    uiModel.details.forEach { detailedInfoUiModel ->
        Row(
            modifier = if (isTablet) {
                uiModel.modifier.width(TabletWidth)
            } else {
                uiModel.modifier.fillMaxWidth()
            },
            horizontalArrangement = uiModel.arrangement
        ) {
            Column {
                Text(
                    text = detailedInfoUiModel.label,
                    style = uiModel.labelTextStyle.toTextStyle()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val iconResourceId = LocalContext.current.getResourceIdByName(
                        detailedInfoUiModel.icon, DefType.DRAWABLE
                    )

                    iconResourceId?.let {
                        Icon(
                            painter = painterResource(id = iconResourceId),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = detailedInfoUiModel.text,
                        style = uiModel.textTextStyle.toTextStyle()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailedInfoListComponentPreview() {
    Column(
        modifier = Modifier
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        DetailedInfoListComponent(
            uiModel = getPreOperationalDetailedInfoListUiModel()
        )
    }
}

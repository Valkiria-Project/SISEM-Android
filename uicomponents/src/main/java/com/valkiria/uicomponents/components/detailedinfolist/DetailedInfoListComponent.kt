package com.valkiria.uicomponents.components.detailedinfolist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.theme.UiComponentsTheme
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Composable
fun DetailedInfoListComponent(
    uiModel: DetailedInfoListUiModel,
    modifier: Modifier = Modifier
) {
    uiModel.details.map { detailedInfoUiModel ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Column {
                Text(text = detailedInfoUiModel.label)
                Row {
                    val iconResourceId = LocalContext.current.getResourceIdByName(
                        detailedInfoUiModel.icon, DefType.DRAWABLE
                    )

                    iconResourceId?.let {
                        Icon(
                            painter = painterResource(id = iconResourceId),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(text = detailedInfoUiModel.text)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailedInfoListComponentPreview() {
    UiComponentsTheme {
        val detailedInfoListUiModel = DetailedInfoListUiModel( // FIXME: Create mock for this
            details = listOf(
                DetailedInfoUiModel(
                    label = "A",
                    icon = "A",
                    text = "A",
                )
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            DetailedInfoListComponent(
                uiModel = detailedInfoListUiModel,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

package com.valkiria.uicomponents.components.detailedinfolist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.theme.UiComponentsTheme

@Composable
fun DetailedInfoListComponent(
    uiModel: DetailedInfoListUiModel,
    modifier: Modifier = Modifier
) {
    uiModel.details.map { detailedInfoUiModel ->
        Text(
            text = detailedInfoUiModel.text,
//            style = uiModel.textStyle.toTextStyle(),
//            modifier = uiModel.modifier.then(modifier)
        )
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

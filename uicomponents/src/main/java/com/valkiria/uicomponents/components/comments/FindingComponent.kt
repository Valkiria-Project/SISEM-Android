package com.valkiria.uicomponents.components.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchComponent
import com.valkiria.uicomponents.mocks.getPreOperationalOilFindingUiModel
import com.valkiria.uicomponents.theme.UiComponentsTheme

@Composable
fun FindingComponent(
    uiModel: FindingUiModel
) {
    SegmentedSwitchComponent(uiModel = uiModel.option)
//    Row(
//        modifier = Modifier
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Surface(
//            modifier = Modifier.fillMaxWidth(),
//            shape = MaterialTheme.shapes.medium.copy(bottomStart = CornerSize(4.dp)),
//            color = MaterialTheme.colorScheme.secondary
//        ) {
//            Column {
//                Text(
//                    "Hola",
//                    modifier = Modifier.padding(12.dp),
//                    color = MaterialTheme.colorScheme.onBackground
//                )
//
//                if (2 == 1) {
//                    val iconResourceId = LocalContext.current.getResourceIdByName(
//                        "ic_comment", DefType.DRAWABLE
//                    )
//
//                    iconResourceId?.let {
//                        Icon(
//                            painter = painterResource(id = iconResourceId),
//                            contentDescription = null,
//                            modifier = Modifier
//                                .padding(end = 12.dp, bottom = 12.dp)
//                                .size(20.dp)
//                                .align(Alignment.End),
//                            tint = MaterialTheme.colorScheme.onBackground
//                        )
//                    }
//                } else if (2 > 1) {
//                    Surface(
//                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
//                        shape = MaterialTheme.shapes.medium.copy(bottomStart = CornerSize(4.dp)),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//                        Text(
//                            "Parce",
//                            modifier = Modifier.padding(12.dp),
//                            color = MaterialTheme.colorScheme.onBackground
//                        )
//                    }
//                }
//            }
//        }
//    }
}

@Preview(showBackground = true)
@Composable
fun FindingComponentPreview() {
    UiComponentsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            FindingComponent(uiModel = getPreOperationalOilFindingUiModel())
        }
    }
}

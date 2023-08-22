package com.valkiria.uicomponents.components.chipoptions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.bricks.FilterChipView
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.mocks.getPreOperationalChipOptionsUiModel
import com.valkiria.uicomponents.props.TextStyle
import timber.log.Timber

@Suppress("UnusedPrivateMember")
@Composable
fun ChipOptionsComponent(
    uiModel: ChipOptionsUiModel,
    isTablet: Boolean = false,
    onAction: (selected: String?, isSelection: Boolean) -> Unit
) {
    Column(
        modifier = uiModel.modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        LabelComponent(
            uiModel = LabelUiModel(
                text = uiModel.title,
                textStyle = uiModel.textStyle
            )
        )

        FlowRow(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            uiModel.items.forEach { chipOption ->
                FilterChipView(
                    text = chipOption.name,
                    textStyle = TextStyle.BUTTON_1,
                    onAction = { selected, isSelection ->
                        onAction(selected, isSelection)
                    }
                )

//            FilterChip(
//                selected = selected,
//                onClick = { selected = !selected },
//                label = { Text(chipText) },
//                modifier = Modifier.padding(end = 18.dp),
//            )
            }
        }
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
    }
}

@Preview(showBackground = true)
@Composable
fun ChipOptionsComponentPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        ChipOptionsComponent(
            uiModel = getPreOperationalChipOptionsUiModel()
        ) { selected, isSelection ->
            Timber.d("Selected $selected and is $isSelection")
        }
    }
}

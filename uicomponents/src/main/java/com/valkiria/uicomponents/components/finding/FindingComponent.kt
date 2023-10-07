package com.valkiria.uicomponents.components.finding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchComponent
import com.valkiria.uicomponents.model.mocks.getPreOperationalOilFindingUiModel
import com.valkiria.uicomponents.components.body.FindingUiModel
import timber.log.Timber

@Composable
fun FindingComponent(
    uiModel: com.valkiria.uicomponents.components.body.FindingUiModel,
    onAction: (id: String, status: Boolean) -> Unit
) {
    SegmentedSwitchComponent(uiModel = uiModel.segmentedSwitchUiModel) { id, status ->
        onAction(id, status)
    }
}

@Preview(showBackground = true)
@Composable
fun FindingComponentPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        FindingComponent(uiModel = getPreOperationalOilFindingUiModel()) { id, status ->
            Timber.d("Finding with id $id set to $status")
        }
    }
}

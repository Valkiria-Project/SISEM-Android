package com.valkiria.uicomponents.components.finding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.bricks.banner.finding.FindingsDetailUiModel
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchComponent
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedValueComponent
import com.valkiria.uicomponents.mocks.getPreOperationalOilFindingUiModel
import timber.log.Timber

@Composable
fun FindingComponent(
    uiModel: FindingUiModel,
    onAction: (id: String, status: Boolean, findingDetail: FindingsDetailUiModel?) -> Unit
) {
    if (uiModel.segmentedValueUiModel != null) {
        SegmentedValueComponent(
            modifier = uiModel.modifier,
            uiModel = uiModel.segmentedValueUiModel,
            hasFinding = uiModel.findingDetail != null
        ) {
            onAction(uiModel.identifier, true, uiModel.findingDetail?.copy(title = it))
        }
    } else {
        uiModel.segmentedSwitchUiModel?.let {
            SegmentedSwitchComponent(uiModel = it) { id, status, _, _ ->
                onAction(id, status, null)
            }
        }
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
        FindingComponent(uiModel = getPreOperationalOilFindingUiModel()) { id, status, _ ->
            Timber.d("Finding with id $id set to $status")
        }
    }
}

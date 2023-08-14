@file:Suppress("MagicNumber")

package com.valkiria.uicomponents.mocks

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.segmentedswitch.OptionUiModel
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.props.TextStyle.BUTTON_2
import kotlin.random.Random

fun getDeviceAuthSegmentedSwitchUiModel(): SegmentedSwitchUiModel {
    return SegmentedSwitchUiModel(
        identifier = Random(100).toString(),
        text = "Prueba algo de texto y un poco más",
        textStyle = TextStyle.HEADLINE_5,
        options = listOf(
            OptionUiModel(
                text = "No",
                textStyle = BUTTON_2,
                color = "blueColor"
            ),
            OptionUiModel(
                text = "Si",
                textStyle = BUTTON_2,
                color = "red"
            )
        ),
        modifier = Modifier
    )
}

fun getPreOperationalOilSegmentedSwitchUiModel(): SegmentedSwitchUiModel {
    return SegmentedSwitchUiModel(
        identifier = Random(100).toString(),
        text = "Aceite del motor",
        textStyle = TextStyle.HEADLINE_4,
        options = listOf(
            OptionUiModel(
                text = "Óptimo",
                textStyle = BUTTON_2,
                color = "blueColor"
            ),
            OptionUiModel(
                text = "Hallazgo",
                textStyle = BUTTON_2,
                color = "red"
            )
        ),
        modifier = Modifier
    )
}

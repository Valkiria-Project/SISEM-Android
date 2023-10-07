@file:Suppress("MagicNumber")

package com.valkiria.uicomponents.model.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.segmentedswitch.OptionUiModel
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.props.TextStyle.BUTTON_2
import com.valkiria.uicomponents.model.ui.body.SegmentedSwitchUiModel
import kotlin.random.Random

fun getDeviceAuthSegmentedSwitchUiModel(): SegmentedSwitchUiModel {
    return SegmentedSwitchUiModel(
        identifier = Random(100).toString(),
        text = "Prueba algo de texto y un poco más",
        textStyle = TextStyle.HEADLINE_5,
        options = listOf(
            OptionUiModel(
                text = "Si",
                textStyle = BUTTON_2,
                color = "blueColor"
            ),
            OptionUiModel(
                text = "No",
                textStyle = BUTTON_2,
                color = "red"
            )
        ),
        arrangement = Arrangement.Center,
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
        arrangement = Arrangement.Center,
        modifier = Modifier
    )
}

package com.valkiria.uicomponents.components.chip

import androidx.compose.ui.Modifier
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import com.valkiria.uicomponents.props.ChipStyle
import com.valkiria.uicomponents.props.TextStyle
import org.junit.Rule
import org.junit.Test

class ChipComponentPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false, screenHeight = 1),
        renderingMode = RenderingMode.V_SCROLL
    )

    @Test
    fun snapRichLabelComponent() {
        paparazziRule.snapshot {
            val chipUiModel = ChipUiModel(
                icon = "ic_ambulance",
                text = "5421244",
                textStyle = TextStyle.HEADLINE_5,
                style = ChipStyle.PRIMARY,
                modifier = Modifier
            )
            ChipComponent(uiModel = chipUiModel)
        }
    }
}

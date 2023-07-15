package com.valkiria.uicomponents.components.textfield

import androidx.compose.ui.Modifier
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import com.valkiria.uicomponents.components.chip.ChipComponent
import com.valkiria.uicomponents.components.chip.ChipUiModel
import com.valkiria.uicomponents.props.ChipStyle.PRIMARY
import com.valkiria.uicomponents.props.TextStyle.HEADLINE_5
import org.junit.Rule
import org.junit.Test

class TextFieldComponentPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false, screenHeight = 1),
        renderingMode = V_SCROLL
    )

    @Test
    fun snapRichLabelComponent() {
        paparazziRule.snapshot {
            val chipUiModel = ChipUiModel(
                icon = "ic_ambulance",
                text = "5421244",
                textStyle = HEADLINE_5,
                style = PRIMARY,
                modifier = Modifier
            )
            ChipComponent(uiModel = chipUiModel)
        }
    }
}

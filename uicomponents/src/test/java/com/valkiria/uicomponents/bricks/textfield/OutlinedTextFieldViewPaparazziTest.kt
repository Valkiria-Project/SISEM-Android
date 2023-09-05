package com.valkiria.uicomponents.bricks.textfield

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.valkiria.uicomponents.bricks.textfield.OutlinedTextFieldViewPreview
import org.junit.Rule
import org.junit.Test

class OutlinedTextFieldViewPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false, screenHeight = 1),
        renderingMode = SessionParams.RenderingMode.V_SCROLL
    )

    @Test
    fun snapFilledTextFieldView() {
        paparazziRule.snapshot {
            OutlinedTextFieldViewPreview()
        }
    }
}

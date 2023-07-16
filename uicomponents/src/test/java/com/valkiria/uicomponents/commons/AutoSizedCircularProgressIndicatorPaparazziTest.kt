package com.valkiria.uicomponents.commons

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import org.junit.Rule
import org.junit.Test

class AutoSizedCircularProgressIndicatorPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false, screenHeight = 1),
        renderingMode = RenderingMode.V_SCROLL
    )

    @Test
    fun snapAutoSizedCircularProgressIndicator() {
        paparazziRule.snapshot {
            AutoSizedCircularProgressIndicator()
        }
    }
}

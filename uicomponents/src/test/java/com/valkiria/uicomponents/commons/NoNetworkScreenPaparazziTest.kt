package com.valkiria.uicomponents.commons

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

class NoNetworkScreenPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false, screenHeight = 1),
        renderingMode = SessionParams.RenderingMode.V_SCROLL
    )

    @Test
    fun snapNoNetworkScreen() {
        paparazziRule.snapshot {
            NoNetworkScreen {
                Timber.d("no-op")
            }
        }
    }
}

package com.valkiria.uicomponents.components.loader

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import com.valkiria.uicomponents.bricks.loader.LoaderComponentPreview
import org.junit.Rule
import org.junit.Test

class LoaderComponentPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false),
        renderingMode = V_SCROLL
    )

    @Test
    fun snapLoaderComponent() {
        paparazziRule.snapshot {
            LoaderComponentPreview()
        }
    }
}

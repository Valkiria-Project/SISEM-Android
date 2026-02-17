package com.valkiria.uicomponents.components.textfield

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import org.junit.Rule
import org.junit.Test

class PasswordTextFieldComponentPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false),
        renderingMode = V_SCROLL
    )

    @Test
    fun snapLoginPasswordTextFieldComponent() {
        paparazziRule.snapshot {
            PasswordTextFieldComponentPreview()
        }
    }
}

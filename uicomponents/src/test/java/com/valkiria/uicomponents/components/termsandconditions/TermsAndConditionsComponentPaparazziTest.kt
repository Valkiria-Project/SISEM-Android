package com.valkiria.uicomponents.components.termsandconditions

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test

class TermsAndConditionsComponentPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false),
        renderingMode = SessionParams.RenderingMode.V_SCROLL
    )

    @Test
    fun snapLoginTermsAndConditionsComponent() {
        paparazziRule.snapshot {
            TermsAndConditionsComponentPreview()
        }
    }
}

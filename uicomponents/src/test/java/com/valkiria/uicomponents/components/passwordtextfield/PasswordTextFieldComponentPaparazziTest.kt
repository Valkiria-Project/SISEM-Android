package com.valkiria.uicomponents.components.passwordtextfield

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import com.valkiria.uicomponents.mocks.getLoginPasswordTextFieldUiModel
import org.junit.Rule
import org.junit.Test

class PasswordTextFieldComponentPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false, screenHeight = 1),
        renderingMode = V_SCROLL
    )

    @Test
    fun snapRichLabelComponent() {
        paparazziRule.snapshot {
            PasswordTextFieldComponent(uiModel = getLoginPasswordTextFieldUiModel())
        }
    }
}
package com.valkiria.uicomponents.components.label

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import com.valkiria.uicomponents.mocks.getDeviceAuthLicensePlateLabelUiModel
import com.valkiria.uicomponents.mocks.getDeviceAuthSerialLabelUiModel
import org.junit.Rule
import org.junit.Test

class LabelComponentPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false, screenHeight = 1),
        renderingMode = V_SCROLL
    )

    @Test
    fun snapDeviceAuthSerialLabelComponent() {
        paparazziRule.snapshot {
            LabelComponent(uiModel = getDeviceAuthSerialLabelUiModel())
        }
    }

    @Test
    fun snapDeviceAuthLicensePlateLabelComponent() {
        paparazziRule.snapshot {
            LabelComponent(uiModel = getDeviceAuthLicensePlateLabelUiModel())
        }
    }
}

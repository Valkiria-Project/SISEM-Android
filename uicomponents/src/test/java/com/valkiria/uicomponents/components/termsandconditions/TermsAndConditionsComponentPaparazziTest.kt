package com.valkiria.uicomponents.components.termsandconditions

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.valkiria.uicomponents.mocks.getLoginTermsAndConditionsUiModel
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

class TermsAndConditionsComponentPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false, screenHeight = 1),
        renderingMode = SessionParams.RenderingMode.V_SCROLL
    )

    @Test
    fun snapLoginTermsAndConditionsComponent() {
        paparazziRule.snapshot {
            TermsAndConditionsComponent(
                uiModel = getLoginTermsAndConditionsUiModel()
            ) { link ->
                Timber.d("Handle $link clicked")
            }
        }
    }
}

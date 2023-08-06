package com.valkiria.uicomponents.components.bottomsheet

import androidx.compose.material3.Text
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

class BottomSheetComponentPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false, screenHeight = 1),
        renderingMode = V_SCROLL
    )

    @Test
    fun snapLoginTermsBottomSheetComponent() {
        paparazziRule.snapshot {
            BottomSheetComponent(
                content = {
                    Text(text = "prueba")
                }
            ) {
                Timber.d("Dismissed")
            }
        }
    }

    @Test
    fun snapLoginPrivacyBottomSheetComponent() {
        paparazziRule.snapshot {
            BottomSheetComponent(
                content = {
                    Text(text = "prueba")
                }
            ) {
                Timber.d("Dismissed")
            }
        }
    }
}

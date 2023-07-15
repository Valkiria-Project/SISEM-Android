package com.valkiria.uicomponents.components.richlabel

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import org.junit.Rule
import org.junit.Test


class RichLabelComponentPaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false, screenHeight = 1),
        renderingMode = RenderingMode.V_SCROLL
    )

    @Test
    fun snapRichLabelComponent() {
//        paparazziRule.snapshot {
//            val richLabelUiModel = RichLabelUiModel(
//                text = "<font color=\"#FFFFFF\">Bienvenido al <b>SISEM</b></font>",
//                textStyle = TextStyle.HEADLINE_1,
//                modifier = Modifier
//            )
//            RichLabelComponent(uiModel = richLabelUiModel)
//        }
    }
}

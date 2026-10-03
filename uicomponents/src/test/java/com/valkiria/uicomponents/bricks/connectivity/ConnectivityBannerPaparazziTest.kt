package com.valkiria.uicomponents.bricks.connectivity

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class ConnectivityBannerPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false)
    )

    @Test
    fun snapOffline() {
        paparazzi.snapshot { ConnectivityBannerOfflinePreview() }
    }

    @Test
    fun snapOfflineWithPending() {
        paparazzi.snapshot { ConnectivityBannerOfflinePendingPreview() }
    }

    @Test
    fun snapSyncing() {
        paparazzi.snapshot { ConnectivityBannerSyncingPreview() }
    }

    @Test
    fun snapRejected() {
        paparazzi.snapshot { ConnectivityBannerRejectedPreview() }
    }
}

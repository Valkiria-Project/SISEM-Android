package com.skgtecnologia.sisem.commons.connectivity

import android.net.NetworkCapabilities
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ConnectivityTest {

    private fun capabilities(internet: Boolean, validated: Boolean) = mockk<NetworkCapabilities> {
        every { hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns internet
        every { hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) } returns validated
    }

    @Test
    fun `a validated network with internet is online`() {
        assertEquals(Connectivity.ONLINE, capabilities(internet = true, validated = true).toConnectivity())
    }

    @Test
    fun `a network Android could not validate is not taken for offline`() {
        assertEquals(
            Connectivity.UNVALIDATED,
            capabilities(internet = true, validated = false).toConnectivity()
        )
    }

    @Test
    fun `a network without internet is offline`() {
        assertEquals(
            Connectivity.OFFLINE,
            capabilities(internet = false, validated = false).toConnectivity()
        )
    }
}

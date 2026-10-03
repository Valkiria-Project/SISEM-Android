package com.skgtecnologia.sisem.commons.connectivity

import android.net.NetworkCapabilities

/**
 * What the device can tell about its connection. Only [OFFLINE] is acted on without trying:
 * the other two still get a real request, because whether it gets through is the only answer
 * that counts.
 */
enum class Connectivity {

    /** A network Android has confirmed reaches the internet. */
    ONLINE,

    /**
     * A network that offers internet but that Android could not confirm. Kept apart from
     * [OFFLINE] on purpose: SIMs on a private APN that blocks Google's connectivity check never
     * validate, yet still reach the SISEM services. Treating this as offline would leave those
     * ambulances permanently unable to send anything.
     */
    UNVALIDATED,

    /** No network at all: airplane mode, no signal, data switched off. */
    OFFLINE
}

internal fun NetworkCapabilities.toConnectivity(): Connectivity = when {
    !hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> Connectivity.OFFLINE
    hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) -> Connectivity.ONLINE
    else -> Connectivity.UNVALIDATED
}

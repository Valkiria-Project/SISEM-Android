package com.skgtecnologia.sisem.commons.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkMonitor {
    val connectivity: StateFlow<Connectivity>
}

/**
 * Follows the default network for the whole life of the process. Read synchronously by the
 * network interceptors, so the current value is always at hand without suspending.
 */
@Singleton
class AndroidNetworkMonitor @Inject constructor(
    @ApplicationContext context: Context
) : NetworkMonitor {

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    private val state = MutableStateFlow(currentConnectivity())

    override val connectivity: StateFlow<Connectivity> = state.asStateFlow()

    init {
        connectivityManager.registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    state.value = networkCapabilities.toConnectivity()
                }

                override fun onLost(network: Network) {
                    // The default network went away. If another takes its place it reports its
                    // own capabilities right after; until then there is nothing to talk to.
                    state.value = Connectivity.OFFLINE
                }
            }
        )
    }

    private fun currentConnectivity(): Connectivity =
        connectivityManager.activeNetwork
            ?.let { connectivityManager.getNetworkCapabilities(it) }
            ?.toConnectivity()
            ?: Connectivity.OFFLINE
}

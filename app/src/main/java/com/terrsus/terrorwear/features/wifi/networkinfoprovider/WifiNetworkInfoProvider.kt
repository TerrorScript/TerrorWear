package com.terrsus.terrorwear.features.wifi.networkinfoprovider

import kotlinx.coroutines.flow.Flow
import com.terrsus.terrorwear.features.wifi.domain.model.WifiNetworkInfo

/**
 * Abstraction for retrieving current Wi‑Fi network information.
 *
 * Implementations may use platform APIs (ConnectivityManager) or provide
 * synthetic data for testing and emulator environments.
 */
interface WifiNetworkInfoProvider {

    /**
     * Continuous stream of Wi‑Fi network information snapshots.
     */
    val networkInfo: Flow<WifiNetworkInfo>

    /**
     * Starts producing network information updates.
     */
    fun start()

    /**
     * Stops producing network information updates.
     */
    fun stop()
}
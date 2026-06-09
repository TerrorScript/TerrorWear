package com.terrsus.terrorwear.features.wifi.networkinfoprovider

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.terrsus.terrorwear.features.wifi.domain.model.WifiNetworkInfo

/**
 * Fake implementation used on emulators and in tests.
 *
 * Produces synthetic but realistic Wi‑Fi information without relying on
 * platform Wi‑Fi APIs, which are unavailable on Wear OS emulators.
 */
class WifiNetworkInfoProviderFake : WifiNetworkInfoProvider {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null

    private val _networkInfo = MutableStateFlow(
        WifiNetworkInfo(
            ssid = "TestNetwork",
            bssid = "00:11:22:33:44:55",
            rssi = -42,
            linkSpeed = 72,
            frequency = 2412,
            ipAddress = "192.168.0.123",
            gateway = "192.168.0.1",
            dns = "8.8.8.8",
            security = "WPA2"
        )
    )
    override val networkInfo = _networkInfo.asStateFlow()

    override fun start() {
        if (job != null) return

        job = scope.launch {
            while (true) {
                // Simulate slight RSSI fluctuations
                val base = _networkInfo.value
                val newRssi = (base.rssi + (-2..2).random())
                    .coerceIn(-80, -40)

                _networkInfo.value = base.copy(rssi = newRssi)
                delay(1500)
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }
}
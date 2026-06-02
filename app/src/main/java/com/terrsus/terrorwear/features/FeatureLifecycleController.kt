package com.terrsus.terrorwear.features

import com.terrsus.terrorwear.features.ble.connection.BleManager
import com.terrsus.terrorwear.features.sensors.SensorManager
import com.terrsus.terrorwear.features.wifi.WifiManager

enum class Feature { BLE, WIFI, SENSORS }

data class RouteInfo(
    val route: String,
    val features: Set<Feature>
)

class FeatureLifecycleController(
    private val ble: BleManager,
    private val wifi: WifiManager,
    private val sensors: SensorManager
) {

    fun onRouteChanged(old: RouteInfo?, new: RouteInfo) {
        val oldF = old?.features ?: emptySet()
        val newF = new.features

        // Stop features no longer needed
        (oldF - newF).forEach {
            when (it) {
                Feature.BLE -> ble.stop()
                Feature.WIFI -> wifi.stop()
                Feature.SENSORS -> sensors.stop()
            }
        }

        // Start features newly needed
        (newF - oldF).forEach {
            when (it) {
                Feature.BLE -> ble.start()
                Feature.WIFI -> wifi.start()
                Feature.SENSORS -> sensors.start()
            }
        }
    }
}

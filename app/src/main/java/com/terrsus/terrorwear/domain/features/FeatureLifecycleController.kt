package com.terrsus.terrorwear.domain.features

import android.util.Log
import com.terrsus.terrorwear.domain.features.Feature.*
import com.terrsus.terrorwear.features.ble.insecure.BleManager
import com.terrsus.terrorwear.features.sensors.SensorManager
import com.terrsus.terrorwear.features.wifi.manager.WifiManager
import com.terrsus.terrorwear.ui.navigation.Route

/**
 * Centralized hardware lifecycle controller.
 *
 * This class ensures that hardware subsystems (BLE, WiFi, Sensors, etc.)
 * are only active when the current screen explicitly requires them.
 *
 * Navigation → Route.features drives the lifecycle.
 *
 * Example:
 * - Navigating to TiltScreen activates SENSORS + HAPTICS
 * - Navigating away stops them automatically
 *
 * This prevents:
 * - double registration
 * - battery drain
 * - sensors running in background
 * - UI screens manually managing hardware
 */
class FeatureLifecycleController(
    private val bleProvider: () -> BleManager,
    private val wifiProvider: () -> WifiManager,
    private val sensorsProvider: () -> SensorManager
) {
    init {
        Log.i("TW/FeatureLifecycleController", "init")
    }

    /**
     * Called whenever navigation changes.
     *
     * @param oldRoute The previous route (may be null on first launch)
     * @param newRoute The new active route
     */
    fun onRouteChanged(oldRoute: Route?, newRoute: Route) {
        val oldFeatures = oldRoute?.features ?: emptySet()
        val newFeatures = newRoute.features

        Log.d(
            "TW/FeatureLifecycleController",
            "Route change: ${oldRoute?.path ?: "null"} → ${newRoute.path}"
        )

        // ------------------------------------------------------------
        // STOP features no longer needed
        // ------------------------------------------------------------
        (oldFeatures - newFeatures).forEach { feature ->
            Log.d(
                "TW/FeatureLifecycleController",
                "Disabling feature: $feature"
            )

            when (feature) {

                BLE -> bleProvider().stop()

                WIFI -> wifiProvider().stopAll()

                SENSORS -> sensorsProvider().stop()

                HEART_RATE -> {
                    // TODO: stop HR sensor listener
                }

                LOCATION -> {
                    // TODO: stop GNSS updates
                }

                MICROPHONE -> {
                    // TODO: stop audio recorder / voice input
                }

                HAPTICS -> {
                    // TODO: disable haptic feedback if needed
                }

                BAROMETER -> {
                    // TODO: stop barometer listener
                }

                MAGNETOMETER -> {
                    // TODO: stop magnetometer listener
                }

                TEMPERATURE -> {
                    // TODO: stop temperature sensor listener
                }

                FOREGROUND_SERVICE -> {
                    // TODO: stop foreground service
                }
            }
        }

        // ------------------------------------------------------------
        // START features newly required
        // ------------------------------------------------------------
        (newFeatures - oldFeatures).forEach { feature ->
            Log.d(
                "TW/FeatureLifecycleController",
                "Enabling feature: $feature"
            )

            when (feature) {

                BLE -> bleProvider().start()

                WIFI -> {
                    // wifi.startTcpServer() or similar
                }

                SENSORS -> sensorsProvider().start()

                HEART_RATE -> {
                    // TODO: start HR sensor listener
                }

                LOCATION -> {
                    // TODO: request GNSS updates
                }

                MICROPHONE -> {
                    // TODO: start audio recorder / voice input
                }

                HAPTICS -> {
                    // TODO: enable haptic feedback module
                }

                BAROMETER -> {
                    // TODO: start barometer listener
                }

                MAGNETOMETER -> {
                    // TODO: start magnetometer listener
                }

                TEMPERATURE -> {
                    // TODO: start temperature sensor listener
                }

                FOREGROUND_SERVICE -> {
                    // TODO: start foreground service
                }
            }
        }

        Log.d(
            "TW/FeatureLifecycleController",
            "Active features: $newFeatures"
        )
    }
}
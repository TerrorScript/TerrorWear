package com.terrsus.terrorwear.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.terrsus.terrorwear.domain.features.Feature
import com.terrsus.terrorwear.features.ble.insecure.BleManager
import com.terrsus.terrorwear.features.sensors.SensorManager
import com.terrsus.terrorwear.features.wifi.manager.WifiManager
import com.terrsus.terrorwear.util.DeviceUtils

/**
 * Centralized permission gatekeeper for hardware‑dependent features.
 *
 * This orchestrator determines whether all newly‑required features have the
 * permissions they need before the FeatureLifecycleController is allowed to
 * activate them. If any feature lacks its required permissions, activation is
 * aborted and the caller is notified via [onDenied].
 *
 * This placeholder implementation does NOT yet query the Android permission
 * system. Instead, it demonstrates the intended control flow:
 *
 *  - For each newly‑required feature, determine which permissions it needs
 *  - Ask the feature manager for its missing permissions
 *  - Attempt to request each missing permission
 *  - If any permission is denied, abort activation
 *  - If all permissions are granted, allow activation to proceed
 *
 * In the real implementation, this class will:
 *
 *  - query the platform for actual permission state
 *  - request missing permissions via an ActivityResultLauncher
 *  - resume activation once the user responds
 *  - abort navigation if the user denies any required permission
 *
 * @param bleProvider provides the BLE manager instance
 * @param wifiProvider provides the Wi‑Fi manager instance
 * @param sensorsProvider provides the sensor manager instance
 */
class PermissionOrchestrator(
    private val context: Context,
    private val bleProvider: () -> BleManager,
    private val wifiProvider: () -> WifiManager,
    private val sensorsProvider: () -> SensorManager,
    private val requestPermissionsCallback: suspend (List<Permission>) -> Boolean
) {
    /**
     * Requests a batch of permissions from the UI layer.
     *
     * This function does NOT launch permission dialogs itself. Instead, the UI
     * layer provides a suspend function that:
     *
     *  - launches RequestMultiplePermissions()
     *  - suspends until the user responds
     *  - returns true if ALL permissions are granted
     *  - returns false if ANY permission is denied
     *
     * @param permissions the list of permissions to request
     * @return true if all permissions are granted, false otherwise
     */
    private suspend fun requestPermissions(permissions: List<Permission>): Boolean {
        return requestPermissionsCallback(permissions)
    }


    /**
     * Checks whether the given permission is currently granted on the device.
     *
     * This method queries the Android permission system to determine if the
     * runtime permission represented by [permission] has been granted.
     *
     * In the real implementation, this will:
     *  - map the internal [Permission] object to its Android Manifest constant
     *  - call ContextCompat.checkSelfPermission()
     *  - return true if the permission is granted, false otherwise
     *
     * @param permission the internal permission representation to check
     * @return true if the permission is granted, false otherwise
     */
    private fun hasPermission(permission: Permission): Boolean {
        return ContextCompat.checkSelfPermission(
            context, permission.androidName
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks whether all permissions required by the given features are granted.
     *
     * This placeholder implementation always denies hardware permissions.
     * The real implementation will:
     *
     *  - ask each feature manager for its missing permissions
     *  - request each missing permission
     *  - return false if any permission is denied
     */
    fun getRequiredPermissions(features: Set<Feature>): List<Permission> {
        return features.flatMap { feature ->
            when (feature) {
                Feature.BLE -> if (DeviceUtils.isEmulator) emptyList()
                else bleProvider().requiredPermissions

                Feature.WIFI -> if (DeviceUtils.isEmulator) emptyList()
                else wifiProvider().requiredPermissions

                Feature.HEART_RATE -> emptyList() //sensorsProvider().requiredHeartRatePermissions
                Feature.LOCATION -> emptyList() //sensorsProvider().requiredLocationPermissions
                Feature.MICROPHONE -> emptyList() //sensorsProvider().requiredMicrophonePermissions
                Feature.SENSORS -> emptyList() //sensorsProvider().requiredSensorPermissions
                Feature.HAPTICS -> emptyList()
                Feature.BAROMETER -> emptyList() //sensorsProvider().requiredBarometerPermissions
                Feature.MAGNETOMETER -> emptyList() //sensorsProvider().requiredMagnetometerPermissions
                Feature.TEMPERATURE -> emptyList() //sensorsProvider().requiredTemperaturePermissions
                Feature.FOREGROUND_SERVICE -> listOf(Permission.ForegroundService)
            }
        }.distinct()
    }

    private suspend fun checkPermissions(features: Set<Feature>): Boolean {
        val requiredPermissions: List<Permission> = getRequiredPermissions(features)

        val missingPermissions = requiredPermissions.filter { !hasPermission(it) }

        if (missingPermissions.isEmpty()) return true

        return requestPermissions(missingPermissions)
    }

    /**
     * Ensures that all permissions required by the given features are granted.
     *
     * @param newlyRequiredFeatures the set of features that are about to be activated
     * @param onGranted invoked only if *all* required permissions are granted
     * @param onDenied invoked immediately if *any* required permission is missing
     */
    suspend fun ensurePermissions(
        newlyRequiredFeatures: Set<Feature>,
        onGranted: () -> Unit,
        onDenied: () -> Unit,
    ) {
        if (checkPermissions(newlyRequiredFeatures)) {
            onGranted()
        } else {
            onDenied()
        }
    }
}
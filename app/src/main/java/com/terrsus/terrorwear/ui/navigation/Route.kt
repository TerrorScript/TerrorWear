package com.terrsus.terrorwear.ui.navigation

import androidx.annotation.DrawableRes
import com.terrsus.terrorwear.R
import com.terrsus.terrorwear.domain.features.Feature

/**
 * Declarative navigation model for TerrorWear.
 *
 * Each Route defines:
 * - a unique navigation path
 * - a display name
 * - an optional summary (used in dashboard chips)
 * - an icon resource
 * - a module type (system/tool/game/debug)
 * - the hardware features required by this screen
 *
 * FeatureLifecycleController uses the `features` set to automatically
 * start and stop hardware subsystems (BLE, WiFi, Sensors, etc.)
 * when navigating between screens.
 */
sealed class Route(
    val path: String,
    val name: String,
    val summary: String? = "",
    @DrawableRes val icon: Int,
    val type: ModuleType,
    val features: Set<Feature> = emptySet()
) {

    // ------------------------------
    // Main
    // ------------------------------

    data object Dashboard : Route(
        path = "dashboard",
        name = "Dashboard",
        summary = "Overview & modules",
        icon = 0,
        type = ModuleType.NONE
    )

    // ------------------------------
    // Failure
    // ------------------------------

    object PermissionDenied : Route(
        path = "permissionDenied/{features}",
        name = "Permissions denied",
        summary = null,
        icon = 0,
        type = ModuleType.NONE
    ) {
        fun path(features: String) = "permissionDenied/$features"
    }


    // ------------------------------
    // System
    // ------------------------------

    data object Settings : Route(
        path = "settings",
        name = "Settings",
        summary = null,
        icon = R.drawable.baseline_settings_24,
        type = ModuleType.SYSTEM
    )

    // ------------------------------
    // Tools
    // ------------------------------

    data object ProgramAssist : Route(
        path = "program_assist",
        name = "Program Assist",
        summary = "Desktop control",
        icon = R.drawable.outline_computer_24,
        type = ModuleType.TOOL,
        features = setOf(Feature.BLE)
    )

    data object CameraRemote : Route(
        path = "camera_remote",
        name = "Camera Remote",
        summary = "Control phone camera via BLE",
        icon = R.drawable.baseline_linked_camera_24,
        type = ModuleType.TOOL,
        features = setOf(Feature.BLE)
    )

    data object Compass : Route(
        path = "compass",
        name = "Compass",
        summary = "Bearing, elevation",
        icon = R.drawable.baseline_sensors_24,
        type = ModuleType.TOOL,
        features = setOf(
            Feature.SENSORS,
            Feature.MAGNETOMETER,
            Feature.BAROMETER
        )
    )

    // ------------------------------
    // Games
    // ------------------------------

    data object Stratagem : Route(
        path = "stratagem",
        name = "HD2 Stratagem",
        summary = "Wrist flick as input",
        icon = R.drawable.outline_hand_gesture_24,
        type = ModuleType.GAME,
        features = setOf(
            Feature.BLE,
            Feature.SENSORS,
            Feature.HAPTICS
        )
    )

    data object Pong : Route(
        path = "pong",
        name = "Pong",
        summary = "Elite Ball Knowledge",
        icon = R.drawable.outline_network_ping_24,
        type = ModuleType.GAME,
        features = setOf(Feature.SENSORS)
    )

    data object Tilt : Route(
        path = "tilt",
        name = "Table Tilt",
        summary = "Tilt → Roll → Dodge!",
        icon = R.drawable.outline_sports_esports_24,
        type = ModuleType.GAME,
        features = setOf(
            Feature.SENSORS,
            Feature.HAPTICS
        )
    )

    // ------------------------------
    // Debug
    // ------------------------------

    data object Ble : Route(
        path = "BLE",
        name = "Bluetooth LE",
        summary = "Scan, Connect, Control",
        icon = R.drawable.baseline_bluetooth_24,
        type = ModuleType.DEBUG,
        features = setOf(Feature.BLE)
    )

    data object Gatt : Route(
        path = "gatt/{address}",
        name = "Gatt",
        summary = "Device services",
        icon = 0,
        type = ModuleType.DEBUG,
        features = setOf(Feature.BLE)
    ) {
        fun path(address: String) = "gatt/$address"
    }

    data object WifiInfo : Route(
        path = "WifiInfo",
        name = "WiFi utilities",
        summary = "UDP/TCP, ping tools",
        icon = R.drawable.baseline_wifi_24,
        type = ModuleType.DEBUG,
        features = setOf(Feature.WIFI)
    )
    data object WifiTools : Route(
        path = "WifiTools",
        name = "WiFi Tools",
        summary = null,
        icon = R.drawable.baseline_wifi_24,
        type = ModuleType.DEBUG,
        features = setOf(Feature.WIFI)
    )
    data object WifiLogs : Route(
        path = "WifiLogs",
        name = "WiFi Logs",
        summary = null,
        icon = R.drawable.baseline_wifi_24,
        type = ModuleType.DEBUG,
        features = setOf(Feature.WIFI)
    )

    data object Imu : Route(
        path = "Imu",
        name = "IMU",
        summary = "Inertial debugging",
        icon = R.drawable.outline_sensors_krx_24,
        type = ModuleType.DEBUG,
        features = setOf(
            Feature.SENSORS,
            Feature.MAGNETOMETER,
            Feature.BAROMETER,
            Feature.TEMPERATURE
        )
    )
}
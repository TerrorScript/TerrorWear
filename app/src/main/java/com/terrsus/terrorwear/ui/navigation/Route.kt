package com.terrsus.terrorwear.ui.navigation

import androidx.annotation.DrawableRes
import com.terrsus.terrorwear.R

sealed class Route(
    val path: String,
    val name: String,
    val summary: String? = "",
    @DrawableRes val icon: Int,
    val type: ModuleType
) {
    // ------------------------------
    // The main route, leading to the dashboard, from which the other routes are selected.
    // ------------------------------
    data object Dashboard : Route(
        "dashboard",
        "Dashboard",
        "Overview & modules",
        0,
        ModuleType.NONE
    )

    // ------------------------------
    // System
    // ------------------------------
    data object Settings : Route(
        "settings",
        "Settings",
        null,
        R.drawable.baseline_settings_24,
        ModuleType.SYSTEM
    )


    // ------------------------------
    // Tools
    // ------------------------------
    data object ProgramAssist : Route(
        "program_assist",
        "Program Assist",
        "Desktop control",
        R.drawable.outline_computer_24,
        ModuleType.TOOL
    )
    data object CameraRemote : Route(
        "camera_remote",
        "Camera Remote",
        "Remote camera control",
        R.drawable.baseline_linked_camera_24,
        ModuleType.TOOL
    )

    // ------------------------------
    // Games
    // ------------------------------
    data object Stratagem : Route(
        "stratagem",
        "HD2 Stratagem",
        "Wrist flick as input",
        R.drawable.outline_hand_gesture_24,
        ModuleType.GAME
    )
    data object Pong : Route(
        "pong",
        "Pong",
        "Elite Ball Knowledge",
        R.drawable.outline_network_ping_24,
        ModuleType.GAME
    )
    data object Tilt : Route(
        "tilt",
        "Table tilt",
        "Tilt → Roll → Dodge!",
        R.drawable.outline_sports_esports_24,
        ModuleType.GAME
    )

    // ------------------------------
    // Debug
    // ------------------------------
    data object Ble : Route(
        "BLE",
        "Bluetooth LE",
        "Scan, Connect, Control",
        R.drawable.baseline_bluetooth_24,
        ModuleType.DEBUG
    )
    data object Gatt : Route(
        "gatt/{address}",
        "Gatt",
        "Device services",
        0,
        ModuleType.DEBUG
    ) {
        fun create(address: String) = "gatt/$address"
    }
    data object Wifi : Route(
        "WiFi",
        "WiFi",
        "UDP/TCP, ping tools",
        R.drawable.baseline_wifi_24,
        ModuleType.DEBUG
    )

}
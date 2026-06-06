package com.terrsus.terrorwear.ui.navigation

import androidx.annotation.DrawableRes
import com.terrsus.terrorwear.R

sealed class Route(
    val path: String,
    val name: String,
    val summary: String = "",
    @DrawableRes val icon: Int,
    val type: ModuleType
) {

    data object Dashboard : Route(
        "dashboard",
        "Dashboard",
        "Overview & modules",
        0,
        ModuleType.NONE
    )

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
        "UDP/TCP tools",
        R.drawable.baseline_wifi_24,
        ModuleType.DEBUG
    )

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
        "Control phone camera",
        R.drawable.baseline_linked_camera_24,
        ModuleType.TOOL
    )

    data object Stratagem : Route(
        "stratagem",
        "Stratagem",
        "HD2 gesture input",
        R.drawable.outline_hand_gesture_24,
        ModuleType.GAME
    )

    data object Pong : Route(
        "pong",
        "Pong",
        "Classic game",
        R.drawable.outline_network_ping_24,
        ModuleType.GAME
    )

    data object Tilt : Route(
        "tilt",
        "Tilt",
        "Motion game",
        R.drawable.outline_sports_esports_24,
        ModuleType.GAME
    )
}
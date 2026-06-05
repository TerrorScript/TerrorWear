package com.terrsus.terrorwear.ui.navigation

sealed class Route(val route: String) {

    data object Dashboard : Route("dashboard")
    data object Stratagem : Route("stratagem")
    data object Ble : Route("BLE")
    data object Wifi : Route("WiFi")
    data object ProgramAssist : Route("program_assist")
    data object Pong : Route("pong")
    data object Tilt : Route("tilt")

    data object Gatt : Route("gatt/{address}") {
        fun create(address: String) = "gatt/$address"
    }
}
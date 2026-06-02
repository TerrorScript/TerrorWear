package com.terrsus.terrorwear.features.ble

data class BleDevice(
    val address: String,
    val name: String?,
    val rssi: Int
)

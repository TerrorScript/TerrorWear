package com.terrsus.terrorwear.features.ble.model

data class BleDevice(
    val address: String,
    val name: String?,
    val rssi: Int
)

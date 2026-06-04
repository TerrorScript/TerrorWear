package com.terrsus.terrorwear.domain.ble.model

data class BleDevice(
    val address: String,
    val name: String?,
    val rssi: Int
)
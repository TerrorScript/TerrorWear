package com.terrsus.terrorwear.features.ble.model

sealed class BleState {
    object Idle : BleState()
    object Scanning : BleState()
    data class Devices(val list: List<BleDevice>) : BleState()
    data class Error(val message: String) : BleState()
}

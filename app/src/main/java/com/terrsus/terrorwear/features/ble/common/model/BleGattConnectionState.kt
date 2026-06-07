package com.terrsus.terrorwear.features.ble.common.model

sealed class BleGattConnectionState {
    object Disconnected : BleGattConnectionState()
    object Connecting : BleGattConnectionState()
    object Connected : BleGattConnectionState()
    data class Failed(val reason: String) : BleGattConnectionState()
}
